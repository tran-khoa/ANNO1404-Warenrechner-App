package de.ktran.anno1404warenrechner.views;

import dagger.Component;
import de.ktran.anno1404warenrechner.AppComponent;
import de.ktran.anno1404warenrechner.views.game.ChainsDetailFragment;
import de.ktran.anno1404warenrechner.views.game.ChainsFragment;
import de.ktran.anno1404warenrechner.views.game.GameActivity;
import de.ktran.anno1404warenrechner.views.game.GameOverviewFragment;
import de.ktran.anno1404warenrechner.views.game.GameSettingsFragment;
import de.ktran.anno1404warenrechner.views.game.PopulationFragment;

@PerActivity
@Component(
        modules = {
                GameActivityModule.class
        }, dependencies = {
            AppComponent.class,
        }
)
public interface GameActivityComponent {
    void inject(GameActivity activity);

    void inject(PopulationFragment fragment);
    void inject(ChainsFragment fragment);
    void inject(GameOverviewFragment fragment);
    void inject(ChainsDetailFragment fragment);
    void inject(GameSettingsFragment fragment);
}
