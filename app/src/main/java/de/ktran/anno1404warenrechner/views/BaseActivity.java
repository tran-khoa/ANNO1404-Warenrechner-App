package de.ktran.anno1404warenrechner.views;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.ktran.anno1404warenrechner.R;

public abstract class BaseActivity extends AppCompatActivity {

    private final List<HasLifecycle> objectsWithLifecycle = new ArrayList<>();

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

        objectsWithLifecycle.forEach(HasLifecycle::onStart);
    }

    @Override
    protected void onStop() {
        super.onStop();

        objectsWithLifecycle.forEach(HasLifecycle::onStop);
    }
}
