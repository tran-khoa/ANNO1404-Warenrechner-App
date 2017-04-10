package de.ktran.anno1404warenrechner.views;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.ktran.anno1404warenrechner.data.DataManager;
import de.ktran.anno1404warenrechner.data.Game;
import de.ktran.anno1404warenrechner.data.ProductionBuilding;
import de.ktran.anno1404warenrechner.views.game.ChainsAdapter;
import de.ktran.anno1404warenrechner.views.game.ChainsDetailAdapter;
import de.ktran.anno1404warenrechner.views.game.ChainsDetailFragment;
import de.ktran.anno1404warenrechner.views.game.GameActivity;
import de.ktran.anno1404warenrechner.views.game.GameSectionsPagerAdapter;
import de.ktran.anno1404warenrechner.views.game.PopulationAdapter;

@Module
public class GameActivityModule {
    private final GameActivity activity;
    private final int gameId;

    private Game game;

    public GameActivityModule(GameActivity activity, int gameId) {
        this.activity = activity;
        this.gameId = gameId;
    }

    @PerActivity
    @Provides
    GameActivity provideActivity() {
        return activity;
    }

    @Provides
    Game provideGame(DataManager dataManager) {
        if (game == null) {
            game = dataManager.getGameById(gameId);
        }

        return game;
    }

    @Provides
    PopulationAdapter providePopulationAdapter(GameActivity activity, Game game, EventBus bus) {
        return new PopulationAdapter(game, activity, bus);
    }

    @Provides
    ChainsAdapter provideChainsAdapter(GameActivity activity, Game game, EventBus bus, DataManager dataManager) {
        return new ChainsAdapter(game, activity, bus, dataManager);
    }

    @Provides
    ChainsDetailAdapter provideChainsDetailAdapter(DataManager dataManager, Game game, GameActivity activity, EventBus bus) {
        return new ChainsDetailAdapter(dataManager, game, activity, bus);
    }
}
