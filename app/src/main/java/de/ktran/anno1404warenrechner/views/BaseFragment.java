package de.ktran.anno1404warenrechner.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {
    private final List<HasLifecycle> objectsWithLifecycle = new ArrayList<>();

    protected abstract int getLayoutId();

    protected void onViewCreated(View parent) {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);

        onViewCreated(view);

        return view;
    }

    public void registerLifecycle(HasLifecycle lifecycle) {
        objectsWithLifecycle.add(lifecycle);
    }

    @Override
    public void onStart() {
        super.onStart();

        objectsWithLifecycle.forEach(HasLifecycle::onStart);
    }

    @Override
    public void onStop() {
        super.onStop();

        objectsWithLifecycle.forEach(HasLifecycle::onStop);

    }
}
