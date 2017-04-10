package de.ktran.anno1404warenrechner;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Component;
import de.ktran.anno1404warenrechner.data.DataManager;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    Context context();
    App app();
    Gson gson();
    SharedPreferences sharedPreferences();
    EventBus bus();
    DataManager dataManager();
}
