package de.ktran.anno1404warenrechner.views.game;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import de.ktran.anno1404warenrechner.R;
import de.ktran.anno1404warenrechner.data.DataManager;
import de.ktran.anno1404warenrechner.data.Game;
import de.ktran.anno1404warenrechner.event.GameNameChangedEvent;

public class GameOverviewFragment extends GameFragment {

    @Inject
    EventBus bus;

    @Inject
    DataManager dataManager;

    @Inject
    GameActivity gameActivity;

    @Inject
    Game game;

    GameSectionsPagerAdapter sectionAdapter;

    @BindView(R.id.game_overview_tabs)
    TabLayout vTabLayout;

    @BindView(R.id.game_overview_container)
    ViewPager vViewPager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_game_overview;
    }

    @Override
    protected void onViewCreated(View parent) {
        getGameActivity().component().inject(this);

        setHasOptionsMenu(true);

        gameActivity.setSupportActionBar(toolbar);

        final ActionBar actionBar = gameActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(game.getName());
        }

        sectionAdapter = new GameSectionsPagerAdapter(getContext(), getChildFragmentManager());

        vTabLayout.setupWithViewPager(vViewPager);
        vViewPager.setAdapter(sectionAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        bus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        bus.unregister(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_game, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                gameActivity.toSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @SuppressWarnings("unused")
    public void onMessageEvent(GameNameChangedEvent event) {

        if (gameActivity.getSupportActionBar() != null) {
            gameActivity.getSupportActionBar().setTitle(event.getGame().getName());
        }
    }
}
