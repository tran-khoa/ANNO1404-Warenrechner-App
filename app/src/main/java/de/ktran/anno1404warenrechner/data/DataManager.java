package de.ktran.anno1404warenrechner.data;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Singleton;

import de.ktran.anno1404warenrechner.App;
import de.ktran.anno1404warenrechner.R;
import de.ktran.anno1404warenrechner.event.ChainsDetailResultEvent;
import de.ktran.anno1404warenrechner.event.ChainsResultEvent;
import de.ktran.anno1404warenrechner.event.DataResponseEvent;
import de.ktran.anno1404warenrechner.event.GameListResultEvent;
import de.ktran.anno1404warenrechner.event.GameNameChangedEvent;
import de.ktran.anno1404warenrechner.event.GameOpenedEvent;
import de.ktran.anno1404warenrechner.event.PopulationResultEvent;

@Singleton
public class DataManager {

    private final Gson gson;
    private final App app;
    private final SharedPreferences prefs;
    private final EventBus bus;

    private final List<Game> games = new ArrayList<>();
    private static int last_id = 0;

    public DataManager(App app, SharedPreferences prefs, Gson gson, EventBus bus) {
        this.prefs = prefs;
        this.app = app;
        this.gson = gson;
        this.bus = bus;

        bus.register(this);

        readFromStorage();
    }

    private void readFromStorage() {
        final Set<String> data = prefs.getStringSet(app.getString(R.string.prefkey_data), new HashSet<>());
        for (String json : data) {
            final Game game = gson.fromJson(json, Game.class);
            this.games.add(game);
            last_id = Math.max(last_id, game.getId());
        }
    }

    public Game getGameById(int id) {
        for (Game g : games) {
            if (g.getId() == id) return g;
        }

        throw new IllegalStateException();
    }

    private void commitChanges() {
        Set<String> saveString = new HashSet<>();

        for (Game g : games) {
            saveString.add(gson.toJson(g));
        }

        prefs.edit().putStringSet(app.getString(R.string.prefkey_data), saveString).apply();

    }

    /**
     * Sets population count. Negative amount implies that the given amount concerns built houses
     * @param game Game
     * @param p Population type
     * @param amount Amount (signed)
     */
    public void setPopulation(Game game, Population p, int amount) {
        if (amount < 0) {
            amount = -amount;
            amount = amount * p.houseSize;
        }

        game.setPopulation(p, amount);
        commitChanges();

        onGameDataChanged(game);
    }

    public boolean setBonus(Game game, ProductionBuilding building, int bonus) {
        if (game.bonus.get(building) == bonus) {
            return false;
        } else {
            game.bonus.put(building, bonus);
            commitChanges();

            onGameDataChanged(game);
            return true;
        }
    }

    public void createGame() {
        last_id = last_id + 1;

        games.add(
                Game.newGame(last_id, MessageFormat.format("Spiel {0}", last_id))
        );
        commitChanges();
        onGameMetaDataChanged();
    }

    /**
     * Remove game from list synchronously.
     * @param id game_id
     */
    public void removeGame(int id) {
        final Iterator<Game> it = games.iterator();
        while (it.hasNext())  {
            if (it.next().getId() == id) {
                it.remove();
                break;

            }
        }
        commitChanges();
        onGameMetaDataChanged();
    }

    public void gameOpened(Game game) {
        game.setLastOpened(new Date());
        commitChanges();

        onGameMetaDataChanged();
    }

    public void setGameTitle(Game game, String gameTitle) {
        game.setName(gameTitle);

        onGameMetaDataChanged();
        bus.post(new GameNameChangedEvent(game));
    }

    /**
     * Get sorted list. Preferred async.
     * @return Date-sorted list of games
     */
    private List<Game> getSortedList() {
        final ArrayList<Game> res = new ArrayList<>(games);
        Collections.sort(res, (o1, o2)  -> - (o1.getLastOpened().compareTo(o2.getLastOpened())));

        return res;
    }

    public void fetchChainsResults(@NonNull final Game game) {
        Task.doAsync(() -> {
            final Logic logic = new Logic(game);
            postResult(
                    new ChainsResultEvent(game, logic.calculateChains())
            );
        });
    }

    public void fetchChainsDetailResults(@NonNull final ProductionChain chain, @NonNull Game game) {
        Task.doAsync(() -> {
            final Logic logic = new Logic(game);

            final List<ProductionChain> chains = logic.calculateChains();
            ProductionChain resChain = null;
            for (ProductionChain c : chains) {
                if (c.getBuilding().equals(chain.getBuilding())) {
                    resChain = c;
                    break;
                }
            }

            if (resChain == null) {
                throw new IllegalStateException("Chain does not exist");
            }

            postResult(
                    new ChainsDetailResultEvent(logic.calculateChainDependencies(resChain), resChain)
            );
        });
    }

    public void fetchGameList() {
        Task.doAsync(() -> postResult(new GameListResultEvent(getSortedList())));
    }


    public void changeTotalCountOccidental(final Game game, final int totalCount) {
        Task.doAsync(() -> {
            Logic logic = new Logic(game);
            Map<Population, Integer> res = logic.calculateAscensionRightsOccidental(Math.abs(totalCount));

            for (Map.Entry<Population, Integer> e : res.entrySet()) {
                game.setPopulation(e.getKey(), e.getValue() * e.getKey().houseSize);
            }

            commitChanges();
            onGameDataChanged(game);
        });
    }

    public void changeTotalCountOriental(final Game game, final int totalCount) {
        Task.doAsync(() -> {
            Logic logic = new Logic(game);
            Map<Population, Integer> res = logic.calculateAscensionRightsOriental(Math.abs(totalCount));

            for (Map.Entry<Population, Integer> e : res.entrySet()) {
                game.setPopulation(e.getKey(), e.getValue() * e.getKey().houseSize);
            }

            commitChanges();
            onGameDataChanged(game);
        });
    }

    private void postResult(DataResponseEvent event) {
        bus.postSticky(event);
    }

    private void onGameDataChanged(Game game) {
        postResult(new PopulationResultEvent(game));
        fetchChainsResults(game);
        onGameMetaDataChanged();
    }

    private void onGameMetaDataChanged() {
        postResult(new GameListResultEvent(this.getSortedList()));
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onMessageEvent(GameOpenedEvent event) {
        event.getGame().setLastOpened(new Date());
        commitChanges();
    }
}
