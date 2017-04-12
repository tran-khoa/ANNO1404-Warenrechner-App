package de.ktran.anno1404warenrechner.data;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
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
import de.ktran.anno1404warenrechner.event.MaterialResultsEvent;
import de.ktran.anno1404warenrechner.event.PopulationResultEvent;
import de.ktran.anno1404warenrechner.helpers.JavaCompat;

@Singleton
public class DataManager {

    private final Gson gson;
    private final App app;
    private final SharedPreferences prefs;
    private final EventBus bus;

    private final List<Game> games = new ArrayList<>();
    private final List<ProductionBuilding> registeredGoods = new ArrayList<>();
    private static int last_id = 0;

    public DataManager(App app, SharedPreferences prefs, Gson gson, EventBus bus) {
        this.prefs = prefs;
        this.app = app;
        this.gson = gson;
        this.bus = bus;

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

    private void commitChanges() {
        final Set<String> saveString = new HashSet<>();
        for (Game g : games) saveString.add(gson.toJson(g));

        prefs.edit().putStringSet(app.getString(R.string.prefkey_data), saveString).apply();
    }

    public Game getGameById(int id) {
        for (Game g : games) {
            if (g.getId() == id) return g;
        }

        throw new IllegalStateException("Game with given id not found.");
    }

    private void onPopulationChange(Game game) {
        postResult(new PopulationResultEvent(game));
        fetchNeedsChainsResults(game);
        onGameMetaDataChanged();
    }

    private void onGameMetaDataChanged() {
        postResult(new GameListResultEvent(this.getSortedList()));
    }

    @Deprecated
    private void onGameDataChanged(Game game) {
        postResult(new PopulationResultEvent(game));
        fetchNeedsChainsResults(game);
        JavaCompat.forEach(registeredGoods, building -> DataManager.this.fetchChainsDetailResults(
                building.getProduces(), game)); //@todo implement
        onGameMetaDataChanged();
    }

    private void onProductionChainChanged(Game game, Goods goods) {
        if (goods.getType() == Goods.Type.NEEDS || goods.getType() == Goods.Type.INTERMEDIARY)
            fetchNeedsChainsResults(game);

    }

    /**
     * Sets population count. Negative amount implies that the given amount concerns built houses
     * @param game Game
     * @param p PopulationType type
     * @param amount Amount (signed)
     */
    public void setPopulation(Game game, PopulationType p, int amount) {
        if (amount < 0) {
            game.population().setHouseCount(p, Math.abs(amount));
        } else {
            game.population().setPopulationCount(p, amount);
        }
        commitChanges();

        onPopulationChange(game);
    }

    //@TODO make void
    public boolean setBonus(Game game, ProductionBuilding building, int bonus) {
        if (!game.setBonus(building, bonus)) return false;

        commitChanges();
        onProductionChainChanged(game, building.getProduces());
        if (Goods.isMaterial(building.getProduces())) postResult(new MaterialResultsEvent(game));

        return true;
    }

    public void setMaterialProduction(Game game, ProductionBuilding building, int value) {
        if (game.getMaterialProductionCount(building) == value) return;

        game.setOtherGoods(building, value);
        commitChanges();

        postResult(new MaterialResultsEvent(game));
    }

    public void setBeggarPrince(Game game, int rank) {
        if (rank == game.getBeggarPrince()) return;

        game.setBeggarPrince(rank);
        onGameDataChanged(game);
    }

    public void setEnvoysFavour(Game game, int rank) {
        if (rank == game.getEnvoysFavour()) return;

        game.setEnvoysFavour(rank);
        onGameDataChanged(game);
    }

    public void createGame() {
        last_id = last_id + 1;

        games.add(
                Game.newGame(last_id, app.getString(R.string.generic_game_title, last_id))
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
        game.gameTouched();
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

    public void fetchNeedsChainsResults(@NonNull final Game game) {
        Task.doAsync(() -> {
            final Logic logic = new Logic(game);
            postResult(
                    new ChainsResultEvent(game, logic.calculateAllNeedsChains())
            );
        });
    }


    public void fetchChainsDetailResults(@NonNull final Goods goods, @NonNull Game game) {
        Task.doAsync(() -> {
            final Logic logic = new Logic(game);

            postResult(
                    new ChainsDetailResultEvent(logic.calculateChainWithDependencies(goods), goods)
            );
        });
    }

    public void fetchGameList() {
        Task.doAsync(() -> postResult(new GameListResultEvent(getSortedList())));
    }

    public void changeTotalCountOccidental(final Game game, final int totalCount, final int maxPop) {
        Task.doAsync(() -> {
            Logic logic = new Logic(game);
            Map<PopulationType, Integer> res = logic.calculateAscensionRightsOccidental(Math.abs(totalCount), maxPop);

            for (Map.Entry<PopulationType, Integer> e : res.entrySet()) {
                game.population().setHouseCount(e.getKey(), e.getValue());
            }

            commitChanges();
            onGameDataChanged(game);
        });
    }

    public void changeTotalCountOriental(final Game game, final int totalCount, final int maxPop) {
        Task.doAsync(() -> {
            Logic logic = new Logic(game);
            Map<PopulationType, Integer> res = logic.calculateAscensionRightsOriental(Math.abs(totalCount), maxPop);

            for (Map.Entry<PopulationType, Integer> e : res.entrySet()) {
                game.population().setHouseCount(e.getKey(), e.getValue());
            }

            commitChanges();
            onGameDataChanged(game);
        });
    }

    private void postResult(DataResponseEvent event) {
        bus.postSticky(event);
    }

    public void registerUpdate(ProductionBuilding chain) {
        registeredGoods.add(chain);
    }

    public void unregisterUpdate(ProductionBuilding chain) {
        registeredGoods.remove(chain);
    }
}
