package de.ktran.anno1404warenrechner.views;

import dagger.Component;
import de.ktran.anno1404warenrechner.AppComponent;
import de.ktran.anno1404warenrechner.views.main.MainActivity;

@PerActivity
@Component(
        dependencies = AppComponent.class,
        modules = MainActivityModule.class
)
public interface MainActivityComponent {
    void inject(MainActivity activity);
}
