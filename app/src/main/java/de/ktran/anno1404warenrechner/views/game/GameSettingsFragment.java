package de.ktran.anno1404warenrechner.views.game;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.ktran.anno1404warenrechner.R;
import de.ktran.anno1404warenrechner.data.DataManager;
import de.ktran.anno1404warenrechner.data.Game;
import de.ktran.anno1404warenrechner.event.GameNameChangedEvent;
import rikka.materialpreference.EditTextPreference;
import rikka.materialpreference.Preference;
import rikka.materialpreference.PreferenceFragment;
import rikka.materialpreference.SimpleMenuPreference;

public class GameSettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.prefs)
    FrameLayout prefs;

    @Inject
    Game game;

    @Inject
    DataManager dataManager;

    @Inject
    EventBus bus;

    private EditTextPreference namePref;
    private SimpleMenuPreference beggarPrincePref;
    private SimpleMenuPreference envoysFavourPref;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        getGameActivity().component().inject(this);

        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_game_settings, container, false);
        ButterKnife.bind(this, view);
        prefs.addView(
                super.onCreateView(inflater, container, savedInstanceState)
        );

        getGameActivity().setSupportActionBar(toolbar);
        final ActionBar actionBar = getGameActivity().getSupportActionBar();


        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.fragment_settings_title);
            actionBar.setSubtitle(game.getName());
        }
        return view;
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.prefs, null);

        namePref = (EditTextPreference) findPreference(getString(R.string.prefkey_title));
        namePref.setSummary(game.getName());

        Preference removePref = findPreference(getString(R.string.prefkey_delete));
        removePref.setOnPreferenceClickListener(preference -> {
            requestConfirmation(
                getString(R.string.deleting_game),
                getString(R.string.deleting_game_desc, game.getName()),
                () -> {
                    dataManager.removeGame(game.getId());
                    getGameActivity().finish();
                });
            return true;
            }
        );

        beggarPrincePref = (SimpleMenuPreference) findPreference(getString(R.string.prefkey_beggar_prince));
        beggarPrincePref.setEntries(
                R.array.attainment_rank
        );
        beggarPrincePref.setEntryValues(new CharSequence[]{"0", "1", "2", "3"});
        beggarPrincePref.setValueIndex(
                game.getBeggarPrince()
        );
        beggarPrincePref.setSummary(getRankDesc(game.getBeggarPrince()));

        envoysFavourPref = (SimpleMenuPreference) findPreference(getString(R.string.prefkey_envoys_favour));
        envoysFavourPref.setEntries(
                R.array.attainment_rank
        );
        envoysFavourPref.setEntryValues(new CharSequence[]{"0", "1", "2", "3"});
        envoysFavourPref.setValueIndex(
                game.getEnvoysFavour()
        );
        envoysFavourPref.setSummary(
                getRankDesc(game.getEnvoysFavour())
        );
    }

    @Nullable
    @Override
    public DividerDecoration onCreateItemDecoration() {
        return new CategoryDivideDividerDecoration();
    }

    private GameActivity getGameActivity() {
        if (!(getActivity() instanceof GameActivity)) {
            throw new IllegalStateException("PopulationFragment is initiated by the wrong activity.");
        }

        return (GameActivity) getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();

        bus.register(this);
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        bus.unregister(this);
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.prefkey_title))) {
            dataManager.setGameTitle(game, sharedPreferences.getString(key, game.getName()));
        } else if (key.equals(getString(R.string.prefkey_beggar_prince))) {
            int res = Integer.valueOf(sharedPreferences.getString(key, String.valueOf(game.getBeggarPrince())));

            dataManager.setBeggarPrince(game, res);
            beggarPrincePref.setValueIndex(res);
            beggarPrincePref.setSummary(getRankDesc(res));
        } else if (key.equals(getString(R.string.prefkey_envoys_favour))) {
            int res = Integer.valueOf(
                    sharedPreferences.getString(key, String.valueOf(game.getEnvoysFavour()))
            );
            dataManager.setEnvoysFavour(game, res);
            envoysFavourPref.setValueIndex(res);
            envoysFavourPref.setSummary(getRankDesc(res));
        } else {
            Log.e("ERROR", "Invalid preference key " + key);
        }

        sharedPreferences.edit().remove(key).apply();
    }

    private String getRankDesc(int rank) {
        return getContext().getResources().getStringArray(R.array.attainment_rank)[rank];
    }

    private void requestConfirmation(String title, String desc, Runnable runnable) {
        new AlertDialog.Builder(getGameActivity())
                .setTitle(title)
                .setMessage(desc)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> runnable.run())
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @SuppressWarnings("unused")
    public void onMessageEvent(GameNameChangedEvent event) {
        if (getGameActivity().getSupportActionBar() != null) {
            getGameActivity().getSupportActionBar().setSubtitle(event.getGame().getName());
        }

        namePref.setSummary(event.getGame().getName());
    }
}
