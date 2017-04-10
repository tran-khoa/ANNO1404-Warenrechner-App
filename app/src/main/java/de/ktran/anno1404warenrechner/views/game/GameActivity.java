package de.ktran.anno1404warenrechner.views.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.sbgapps.simplenumberpicker.decimal.DecimalPickerHandler;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import de.ktran.anno1404warenrechner.App;
import de.ktran.anno1404warenrechner.AppModule;
import de.ktran.anno1404warenrechner.R;
import de.ktran.anno1404warenrechner.data.DataManager;
import de.ktran.anno1404warenrechner.data.Game;
import de.ktran.anno1404warenrechner.data.Population;
import de.ktran.anno1404warenrechner.data.ProductionChain;
import de.ktran.anno1404warenrechner.helpers.NumberDialog;
import de.ktran.anno1404warenrechner.views.BaseActivity;
import de.ktran.anno1404warenrechner.views.DaggerGameActivityComponent;
import de.ktran.anno1404warenrechner.views.GameActivityComponent;
import de.ktran.anno1404warenrechner.views.GameActivityModule;

public class GameActivity extends BaseActivity implements DecimalPickerHandler {

    public static final String BUNDLE_ID_KEY = "KEY_ID";
    public static final String BUNDLE_CHAIN_KEY = "KEY_CHAIN";

    public static final int DIALOG_ID_OCCIDENTAL = -1;
    public static final int DIALOG_ID_ORIENT = -2;

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

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            final Slide slide = new Slide(Gravity.END);
            slide.setDuration(333);
            detailFragment.setEnterTransition(slide);
            detailFragment.setExitTransition(slide);

            Transition t = TransitionInflater.from(this).inflateTransition(android.R.transition.move);
            detailFragment.setSharedElementEnterTransition(t);
        }


        final Bundle bundle = new Bundle();
        bundle.putString(GameActivity.BUNDLE_CHAIN_KEY, gson.toJson(chain));
        detailFragment.setArguments(bundle);


        getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(origin, ViewCompat.getTransitionName(origin))
                .replace(R.id.game_activity_parent, detailFragment)
                .addToBackStack(null).commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    protected void onPause() {
        super.onPause();

        dataManager.gameOpened(game);
    }

    public void showPopEditDialog(int popId) {

        final NumberDialog.Builder builder = new NumberDialog.Builder()
                .setReference(popId)
                .setNatural(true)
                .setTheme(com.sbgapps.simplenumberpicker.R.style.SimpleNumberPickerTheme);

        if (popId == DIALOG_ID_ORIENT || popId == DIALOG_ID_OCCIDENTAL) {
            builder.setSign(false);
            builder.setDefaultNegative(true);
        } else {
            builder.setSign(true);
            builder.setDefaultNegative(false);
        }

        final NumberDialog d = builder.create();
        d.show(getSupportFragmentManager(), "TAG_POP_DIALOG");
    }

    public void showNameEditDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.edit_dialog_title);

        @SuppressLint("InflateParams") final View parent = LayoutInflater.from(this).inflate(
                R.layout.edit_dialog, null, false
        );
        final EditText inputView = (EditText) parent.findViewById(R.id.game_dialog_edit_name);
        dialog.setView(parent);

        dialog.setPositiveButton(android.R.string.yes, (dialog1, which) -> {
            final String input = inputView.getText().toString();
            dataManager.setGameTitle(game, input);
        });

        dialog.setNegativeButton(android.R.string.no, (dialog1, which) -> dialog1.dismiss());

        dialog.show();
    }

    @Override
    public void onDecimalNumberPicked(int reference, float number) {
        switch (reference) {
            case DIALOG_ID_OCCIDENTAL:
                dataManager.changeTotalCountOccidental(game, (int) number);
                break;
            case DIALOG_ID_ORIENT:
                dataManager.changeTotalCountOriental(game, (int) number);
                break;
            default:
                dataManager.setPopulation(game, Population.values()[reference], (int) number);
                break;
        }
    }

    public GameActivityComponent component() {
        return mComponent;
    }
}
