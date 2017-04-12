package de.ktran.anno1404warenrechner.views;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.HashSet;
import java.util.Set;

import butterknife.ButterKnife;
import de.ktran.anno1404warenrechner.helpers.JavaCompat;

public abstract class BaseActivity extends AppCompatActivity {

    private final Set<HasLifecycle> objectsWithLifecycle = new HashSet<>();

    public abstract int getLayoutId();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        ButterKnife.bind(this);
    }

    public void registerLifecycle(HasLifecycle hasLifecycle) {
        objectsWithLifecycle.add(hasLifecycle);
    }

    @Override
    protected void onStart() {
        super.onStart();

        JavaCompat.forEach(objectsWithLifecycle, HasLifecycle::onStart);
    }

    @Override
    protected void onStop() {
        super.onStop();

        JavaCompat.forEach(objectsWithLifecycle, HasLifecycle::onStop);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
