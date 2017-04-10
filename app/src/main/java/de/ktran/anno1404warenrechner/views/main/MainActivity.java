package de.ktran.anno1404warenrechner.views.main;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;

import javax.inject.Inject;

import butterknife.BindView;
import de.ktran.anno1404warenrechner.App;
import de.ktran.anno1404warenrechner.R;
import de.ktran.anno1404warenrechner.data.DataManager;
import de.ktran.anno1404warenrechner.views.BaseActivity;
import de.ktran.anno1404warenrechner.views.DaggerMainActivityComponent;
import de.ktran.anno1404warenrechner.views.MainActivityComponent;
import de.ktran.anno1404warenrechner.views.MainActivityModule;

public class MainActivity extends BaseActivity {

    @Inject
    DataManager dataManager;

    @Inject
    GameListAdapter listAdapter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.game_list)
    RecyclerView gamesRV;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main2;
    }

    private MainActivityComponent mComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mComponent = DaggerMainActivityComponent.builder()
                .appComponent(App.component(this))
                .mainActivityModule(new MainActivityModule(this)).build();

        this.mComponent.inject(this);

        setSupportActionBar(toolbar);

        getWindow().setBackgroundDrawable(null);

        fab.setOnClickListener(v -> dataManager.createGame());

        registerLifecycle(listAdapter);

        gamesRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        gamesRV.setItemAnimator(new DefaultItemAnimator());
        gamesRV.setAdapter(listAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                new LibsBuilder()
                        .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                        .withAboutIconShown(true)
                        .withAboutAppName(getString(R.string.app_name))
                        .withAboutVersionShown(true)
                        .withAboutDescription(getString(R.string.app_desc))
                        .withLicenseDialog(true)
                        .withLicenseShown(true)
                        .start(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public MainActivityComponent component() {
        return mComponent;
    }
}
