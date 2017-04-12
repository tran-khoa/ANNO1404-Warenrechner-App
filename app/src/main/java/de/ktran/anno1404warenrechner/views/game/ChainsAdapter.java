package de.ktran.anno1404warenrechner.views.game;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.ktran.anno1404warenrechner.R;
import de.ktran.anno1404warenrechner.data.DataManager;
import de.ktran.anno1404warenrechner.data.Game;
import de.ktran.anno1404warenrechner.data.ProductionChain;
import de.ktran.anno1404warenrechner.event.ChainsResultEvent;
import de.ktran.anno1404warenrechner.views.HasLifecycle;

public class ChainsAdapter extends RecyclerView.Adapter<ChainsAdapter.ViewHolder> implements HasLifecycle {

    private final DataManager dataManager;
    private final GameActivity gameActivity;
    private final Game game;
    private final EventBus bus;

    private List<ProductionChain> chainsData = new ArrayList<>();

    public ChainsAdapter(@NonNull final Game game, @NonNull final GameActivity gameActivity, @NonNull final EventBus bus, @NonNull final DataManager dataManager) {
        this.gameActivity = gameActivity;
        this.game = game;
        this.bus = bus;
        this.dataManager = dataManager;
    }

    @Override
    public void onStart() {
        this.bus.register(this);

        dataManager.fetchNeedsChainsResults(game);
    }

    @Override
    public void onStop() {
        this.bus.unregister(this);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemChains) LinearLayout parent;
        @BindView(R.id.itemChainsImage) ImageView view;
        @BindView(R.id.itemChainsProgress) ProgressBar progressBar;
        @BindView(R.id.itemChainsAmount) TextView amountText;
        @BindView(R.id.itemChainsPercentage) TextView percentageText;
        @BindView(R.id.itemChainsBonus) Spinner spinner;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemCount() {
        if (this.chainsData == null) {
            return 0;
        }
        return this.chainsData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chains_item, parent, false)
        );
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ProductionChain chain = chainsData.get(position);

        final int efficiencyPercent = (int) Math.floor(chain.getEfficiency() * 100);

        holder.progressBar.setProgress(efficiencyPercent);
        holder.amountText.setText(String.valueOf(chain.getChains()));

        holder.view.setImageDrawable(
                chain.getBuilding().getProduces().getDrawable(gameActivity)
        );
        holder.percentageText.setText(efficiencyPercent + "%");

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(gameActivity,
                R.array.bonus, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(adapter);
        holder.spinner.setSelection(game.getBonus(chain.getBuilding()));

        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dataManager.setBonus(game, chain.getBuilding(), position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                holder.spinner.setSelection(game.getBonus(chain.getBuilding()));
            }
        });

        if (chain.getChains() > 0) {
            holder.parent.setOnClickListener(v -> gameActivity.toChainsDetail(chain, v));
            ViewCompat.setTransitionName(holder.parent, chain.getBuilding().name());
        } else {

            holder.parent.setOnClickListener(v -> Snackbar.make(v, R.string.hint_chain, BaseTransientBottomBar.LENGTH_SHORT).show());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @SuppressWarnings("unused")
    public void onMessageEvent(ChainsResultEvent event) {
        if (event.isGame(game)) {
            this.chainsData = event.getResult();
            notifyDataSetChanged();
        }
    }
}
