package de.ktran.anno1404warenrechner.views.game;

import android.support.v4.content.ContextCompat;
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
import de.ktran.anno1404warenrechner.data.BuildingAlternative;
import de.ktran.anno1404warenrechner.data.DataManager;
import de.ktran.anno1404warenrechner.data.Game;
import de.ktran.anno1404warenrechner.data.ProductionBuilding;
import de.ktran.anno1404warenrechner.data.ProductionChain;
import de.ktran.anno1404warenrechner.event.ChainsDetailResultEvent;
import de.ktran.anno1404warenrechner.views.HasLifecycle;

public class ChainsDetailAdapter extends RecyclerView.Adapter<ChainsDetailAdapter.ViewHolder> implements HasLifecycle {

    private DataManager dataManager;
    private final GameActivity activity;
    private final Game game;
    private final EventBus bus;

    private List<BuildingAlternative> chainData = new ArrayList<>();

    private ProductionChain chain;

    public ChainsDetailAdapter(DataManager dataManager, Game game, GameActivity activity, EventBus bus) {
        this.dataManager = dataManager;
        this.activity = activity;
        this.game = game;
        this.bus = bus;
    }

    @Override
    public void onStart() {
        bus.register(this);
    }
    @Override
    public void onStop() {
        bus.unregister(this);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemChains)
        LinearLayout parent;

        @BindView(R.id.itemChainsImage)
        ImageView view;

        @BindView(R.id.itemChainsProgress)
        ProgressBar progressBar;

        @BindView(R.id.itemChainsAmount)
        TextView amountText;

        @BindView(R.id.itemChainsPercentage)
        TextView percentageText;
        @BindView(R.id.itemChainsBonus)
        Spinner spinner;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

    }

    @Override
    public int getItemCount() {
        return chainData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.chains_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final BuildingAlternative entry = chainData.get(position);

        final ProductionChain firstChain = entry.getChain();

        if (entry.getChain().getBuilding() == ProductionBuilding.CHARCOAL_BURNER) {
            holder.view.setImageDrawable(
                    ContextCompat.getDrawable(activity, R.drawable.coal1)
            );
        } else if (entry.getChain().getBuilding() == ProductionBuilding.COAL_MINE) {
            holder.view.setImageDrawable(
                    ContextCompat.getDrawable(activity, R.drawable.coal2)
            );
        } else {
            holder.view.setImageDrawable(entry.getChain().getBuilding().getProduces().getDrawable(activity));
        }

        int percentage = (int) Math.floor(firstChain.getEfficiency() * 100);
        holder.progressBar.setProgress(percentage);
        holder.amountText.setText(String.valueOf(firstChain.getChains()));
        holder.percentageText.setText(String.valueOf(percentage + "%"));


        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
                R.array.bonus, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(adapter);
        holder.spinner.setSelection(game.getBonus().get(firstChain.getBuilding()));

        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (dataManager.setBonus(game, firstChain.getBuilding(), position)) {
                    dataManager.fetchChainsDetailResults(chain, game);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    void init(ProductionChain chain) {
        this.chain = chain;
        this.chainData.add(
                BuildingAlternative.of(chain)
        );
        notifyDataSetChanged();

        dataManager.fetchChainsDetailResults(chain, game);
    }

    @SuppressWarnings("unused")
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChainsDetailResultEvent event) {
        if (!event.getChain().getBuilding().equals(this.chain.getBuilding())) return;

        this.chainData = event.getResult();
        notifyDataSetChanged();
    }
}
