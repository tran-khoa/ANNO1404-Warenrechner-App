package de.ktran.anno1404warenrechner.views.game;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import javax.inject.Inject;

import butterknife.BindView;
import de.ktran.anno1404warenrechner.R;

public class MaterialOverviewFragment extends GameFragment {

    @Inject
    MaterialOverviewAdapter adapter;

    @BindView(R.id.fragment_other_goods_overview_list)
    RecyclerView listRV;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_other_goods_overview;
    }

    @Override
    protected void onViewCreated(View parent) {
        getGameActivity().component().inject(this);

        registerLifecycle(adapter);
        listRV.setLayoutManager(new LinearLayoutManager(getContext()));
        listRV.setItemAnimator(new DefaultItemAnimator());
        listRV.setAdapter(adapter);

        listRV.addItemDecoration(new DividerItemDecoration(
                getContext(), DividerItemDecoration.VERTICAL
        ));
    }
}
