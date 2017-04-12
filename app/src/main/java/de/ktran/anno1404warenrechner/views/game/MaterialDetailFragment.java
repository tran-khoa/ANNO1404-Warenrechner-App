package de.ktran.anno1404warenrechner.views.game;

import android.animation.Animator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import de.ktran.anno1404warenrechner.R;
import de.ktran.anno1404warenrechner.data.Game;
import de.ktran.anno1404warenrechner.data.ProductionChain;
import de.ktran.anno1404warenrechner.views.AnimationFinishListener;

public class MaterialDetailFragment extends GameFragment {

    @BindView(R.id.chainsList)
    RecyclerView chainsRV;

    @Inject
    Gson gson;

    @Inject
    GameActivity gameActivity;

    @Inject
    Game game;

    @Inject
    MaterialDetailAdapter adapter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.chainsAnimationPlaceholder)
    ViewGroup animPlaceholder;

    ProductionChain chain;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_other_goods_detail;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Transition transition = TransitionInflater.from(getContext()).inflateTransition(
                    android.R.transition.move
            );

            transition.addListener(new Transition.TransitionListener() {
                @Override public void onTransitionStart(Transition transition) {}
                @Override public void onTransitionEnd(Transition transition) {
                    showList();
                }
                @Override public void onTransitionCancel(Transition transition) {}
                @Override public void onTransitionPause(Transition transition) {}
                @Override public void onTransitionResume(Transition transition) {}
            });
            setSharedElementEnterTransition(transition);


            setEnterSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onSharedElementsArrived(List<String> sharedElementNames, List<View> sharedElements, OnSharedElementsReadyListener listener) {
                    super.onSharedElementsArrived(sharedElementNames, sharedElements, listener);

                    showList();
                }
            });
        }
    }

    @Override
    protected void onViewCreated(View parent) {
        getGameActivity().component().inject(this);

        gameActivity.setSupportActionBar(toolbar);
        final ActionBar actionBar = gameActivity.getSupportActionBar();

        setHasOptionsMenu(true);

        chain = gson.fromJson(getArguments().getString(GameActivity.BUNDLE_CHAIN_KEY), ProductionChain.class);

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(game.getName());
            actionBar.setSubtitle(gameActivity.getString(R.string.production_chain_title,
                    chain.getBuilding().getProduces().getName(getContext())));
        }
        registerLifecycle(adapter);
        chainsRV.setLayoutManager(new LinearLayoutManager(gameActivity));
        chainsRV.setItemAnimator(new DefaultItemAnimator());
        chainsRV.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        adapter.init(chain);
        chainsRV.setAdapter(adapter);

        final ImageView imageView = (ImageView) animPlaceholder.findViewById(R.id.itemChainsImage);
        imageView.setImageDrawable(chain.getBuilding().getProduces().getDrawable(getContext()));

        final TextView chainsText = (TextView) animPlaceholder.findViewById(R.id.itemChainsText);
        chainsText.setText(chain.getBuilding().getProduces().getName(getContext()));

        final NumberPicker numberPicker = (NumberPicker) animPlaceholder.findViewById(R.id.itemChainsNumberPicker);
        numberPicker.setValue(chain.getChains());

        final TextView tpmText = (TextView) animPlaceholder.findViewById(R.id.tpm);
        tpmText.setText(
                getContext().getString(R.string.tpm, chain.getBuilding().getTonsPerMin() * chain.getChains())
        );

        ViewCompat.setTransitionName(animPlaceholder, chain.getBuilding().name());

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            animPlaceholder.setVisibility(View.GONE);
            chainsRV.setAlpha(1f);
        }
    }

    public void showList() {
        if (chainsRV != null) {
            chainsRV.animate()
                    .alpha(1f)
                    .setDuration(333)
                    .setListener(new AnimationFinishListener() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (animPlaceholder != null) {
                                animPlaceholder.removeAllViews();
                                animPlaceholder.setVisibility(View.GONE);
                            }
                        }
                    });
        } else {
            Log.d("showList()", "Could not set visibility.");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.clear();
    }
}
