package de.ktran.anno1404warenrechner.views.game;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.travijuu.numberpicker.library.NumberPicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.ktran.anno1404warenrechner.R;
import de.ktran.anno1404warenrechner.data.DataManager;
import de.ktran.anno1404warenrechner.data.Game;
import de.ktran.anno1404warenrechner.data.Goods;
import de.ktran.anno1404warenrechner.data.ProductionBuilding;
import de.ktran.anno1404warenrechner.data.ProductionChain;
import de.ktran.anno1404warenrechner.event.MaterialResultsEvent;
import de.ktran.anno1404warenrechner.views.HasLifecycle;

public class MaterialOverviewAdapter extends RecyclerView.Adapter<MaterialOverviewAdapter.ViewHolder> implements HasLifecycle {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemChains) ViewGroup parent;
        @BindView(R.id.itemChainsImage) ImageView view;
        @BindView(R.id.itemChainsNumberPicker)
        NumberPicker amountText;
        @BindView(R.id.itemChainsText) TextView titleText;
        @BindView(R.id.tpm) TextView tpmText;
        @BindView(R.id.itemChainsBonus) Spinner spinner;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    private final EventBus bus;
    private final GameActivity activity;
    private final DataManager dataManager;
    private Game game;

    public MaterialOverviewAdapter(EventBus bus, Game game, GameActivity activity, DataManager dataManager) {
        this.bus = bus;
        this.game = game;
        this.activity = activity;
        this.dataManager = dataManager;
    }

    @Override
    public int getItemCount() {
        return Goods.getMaterials().size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_goods_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Goods goods = Goods.getMaterials().get(position);
        final ProductionBuilding building = goods.getProductionBuilding();
        final int chainsCount = game.getMaterialProductionCount(building);

        holder.view.setImageDrawable(goods.getDrawable(activity));
        holder.titleText.setText(goods.getName(activity));


        holder.amountText.setValue(chainsCount);
        holder.amountText.setValueChangedListener((value, action) -> dataManager.setMaterialProduction(game, building, value));

        holder.tpmText.setText(
                activity.getString(R.string.tpm, building.getTonsPerMin(chainsCount, game.getBonus(building)))
        );

        holder.parent.setOnClickListener((view) -> activity.toOtherGoodsDetail(new ProductionChain(building, chainsCount, 1.0f), view));

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
                R.array.bonus, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(adapter);
        holder.spinner.setSelection(game.getBonus(building));

        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (dataManager.setBonus(game, building, position)) {
                    dataManager.fetchChainsDetailResults(building.getProduces(), game);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ViewCompat.setTransitionName(holder.parent, building.name());
    }

    @Override
    public void onStart() {
        this.bus.register(this);
    }

    @Override
    public void onStop() {
        this.bus.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @SuppressWarnings("unused")
    public void onMessageEvent(MaterialResultsEvent event) {
        if (event.getGame().getId() == game.getId()) {
            this.game = event.getGame();
            notifyDataSetChanged();
        }
    }
}
