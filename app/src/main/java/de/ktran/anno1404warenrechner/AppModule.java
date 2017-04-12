package de.ktran.anno1404warenrechner;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.ktran.anno1404warenrechner.data.DataManager;

@Module
public class AppModule {
   private final App mApp;

    public AppModule(App application) {
        mApp = application;
    }

    @Provides
    @Singleton
    Context provideAppContext() {
        return this.mApp;
    }

    @Provides
    @Singleton
    App providesApp() {
        return mApp;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new Gson();
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(App app) {
        return app.getSharedPreferences(app.getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    EventBus provideBus() {
        return EventBus.builder()
                .logNoSubscriberMessages(false)
                .sendNoSubscriberEvent(false)
                .addIndex(new EventBusIndex()).build();
    }

    @Provides
    @Singleton
    DataManager provideDataManager(App app, SharedPreferences sharedPreferences, Gson gson, EventBus bus) {
        return new DataManager(app, sharedPreferences, gson, bus);
    }
}
