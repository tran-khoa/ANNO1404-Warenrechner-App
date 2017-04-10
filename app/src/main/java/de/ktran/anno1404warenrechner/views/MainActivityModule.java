package de.ktran.anno1404warenrechner.views;

import android.support.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;

import dagger.Module;
import dagger.Provides;
import de.ktran.anno1404warenrechner.data.DataManager;
import de.ktran.anno1404warenrechner.views.main.GameListAdapter;
import de.ktran.anno1404warenrechner.views.main.MainActivity;

@Module
public class MainActivityModule {

    private final MainActivity activity;

    public MainActivityModule(@NonNull final MainActivity activity) {
        this.activity = activity;
    }

    @PerActivity
    @Provides
    MainActivity provideActivity() {
        return this.activity;
    }

    @Provides
    GameListAdapter provideGameListAdapter(MainActivity activity, EventBus bus, DataManager dataManager) {
        return new GameListAdapter(activity, bus, dataManager);
    }
}
