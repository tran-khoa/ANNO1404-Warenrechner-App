package de.ktran.anno1404warenrechner.views.game;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Gravity;
import android.view.View;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import de.ktran.anno1404warenrechner.App;
import de.ktran.anno1404warenrechner.R;
import de.ktran.anno1404warenrechner.data.DataManager;
import de.ktran.anno1404warenrechner.data.Game;
import de.ktran.anno1404warenrechner.data.PopulationType;
import de.ktran.anno1404warenrechner.data.ProductionChain;
import de.ktran.anno1404warenrechner.views.BaseActivity;
import de.ktran.anno1404warenrechner.views.DaggerGameActivityComponent;
import de.ktran.anno1404warenrechner.views.GameActivityComponent;
import de.ktran.anno1404warenrechner.views.GameActivityModule;
import de.ktran.anno1404warenrechner.views.PopulationNumberDialog;

public class GameActivity extends BaseActivity {

    public static final String BUNDLE_ID_KEY = "KEY_ID";
    public static final String BUNDLE_CHAIN_KEY = "KEY_CHAIN";

    private GameActivityComponent mComponent;

    private GameOverviewFragment overviewFragment;


    @Inject
    Gson gson;

    @Inject
    DataManager dataManager;

    @Inject
    EventBus bus;

    @Inject
    Game game;

    @Override
    public int getLayoutId() {
        return R.layout.activity_game_overview;
    }

    public static void startActivity(Context context, int id) {
        final Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(GameActivity.BUNDLE_ID_KEY, id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int gameId = getIntent().getExtras().getInt(BUNDLE_ID_KEY);

        this.mComponent = DaggerGameActivityComponent.builder()
                .appComponent(App.component(this))
                .gameActivityModule(new GameActivityModule(this, gameId)).build();

        this.mComponent.inject(this);

        if (savedInstanceState != null) {
            return;
        }

        overviewFragment = new GameOverviewFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.game_activity_parent, overviewFragment).commit();
    }

    public void toChainsDetail(ProductionChain chain, View origin) {
        final ChainsDetailFragment detailFragment = new ChainsDetailFragment();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Slide slide = new Slide(Gravity.END);
            slide.setDuration(333);
            detailFragment.setEnterTransition(slide);
            detailFragment.setExitTransition(slide);

            overviewFragment.setExitTransition(new Fade());

            Transition t = TransitionInflater.from(this).inflateTransition(android.R.transition.move);
            detailFragment.setSharedElementEnterTransition(t);
            detailFragment.setSharedElementReturnTransition(t);
        }


        final Bundle bundle = new Bundle();
        bundle.putString(GameActivity.BUNDLE_CHAIN_KEY, gson.toJson(chain));
        detailFragment.setArguments(bundle);


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.game_activity_parent, detailFragment)
                .addToBackStack(null)
                .addSharedElement(origin, ViewCompat.getTransitionName(origin))
                .commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    public void toOtherGoodsDetail(ProductionChain chain, View origin) {
        final MaterialDetailFragment detailFragment = new MaterialDetailFragment();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Slide slide = new Slide(Gravity.END);
            slide.setDuration(333);
            detailFragment.setEnterTransition(slide);
            detailFragment.setExitTransition(slide);

            overviewFragment.setExitTransition(new Fade());

            Transition t = TransitionInflater.from(this).inflateTransition(android.R.transition.move);
            detailFragment.setSharedElementEnterTransition(t);
            detailFragment.setSharedElementReturnTransition(t);
        }

        final Bundle bundle = new Bundle();
        bundle.putString(GameActivity.BUNDLE_CHAIN_KEY, gson.toJson(chain));
        detailFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.game_activity_parent, detailFragment)
                .addToBackStack(null)
                .addSharedElement(origin, ViewCompat.getTransitionName(origin))
                .commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    public void toSettings() {
        final GameSettingsFragment fragment = new GameSettingsFragment();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Slide slide = new Slide(Gravity.END);
            slide.setDuration(333);
            fragment.setEnterTransition(slide);
            fragment.setExitTransition(slide);
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.game_activity_parent, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onPause() {
        super.onPause();

        dataManager.gameOpened(game);
    }

    public void showPopEditDialog(PopulationType populationType) {
        final PopulationNumberDialog.Builder builder = new PopulationNumberDialog.Builder()
                .setPopulationType(populationType)
                .setForceHouse(false);

        final PopulationNumberDialog d = builder.create();
        d.show(getSupportFragmentManager(), "TAG_POP_DIALOG");
    }

    public void showHouseEditDialog(PopulationType.Civilization civilization) {
        final PopulationNumberDialog.Builder builder = new PopulationNumberDialog.Builder()
                .setPopulationType(civilization)
                .setForceHouse(true);

        final PopulationNumberDialog d = builder.create();
        d.show(getSupportFragmentManager(), "TAG_POP_DIALOG");
    }

    public GameActivityComponent component() {
        return mComponent;
    }
}
