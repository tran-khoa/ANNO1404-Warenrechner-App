package de.ktran.anno1404warenrechner.views.game;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import javax.inject.Inject;

import butterknife.BindView;
import de.ktran.anno1404warenrechner.R;
import de.ktran.anno1404warenrechner.data.DataManager;

public class ChainsFragment extends GameFragment {

    @Inject
    DataManager dataManager;

    @Inject
    ChainsAdapter chainsAdapter;

    @BindView(R.id.chainsList)
    RecyclerView chainsRV;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chains;
    }

    @Override
    protected void onViewCreated(View parent) {
        getGameActivity().component().inject(this);

        chainsRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        chainsRV.setItemAnimator(new DefaultItemAnimator());
        chainsRV.setAdapter(chainsAdapter);
        chainsRV.addItemDecoration(new DividerItemDecoration(
                getContext(), DividerItemDecoration.VERTICAL
        ));
    }

    @Override
    public void onStart() {
        registerLifecycle(chainsAdapter);

        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
