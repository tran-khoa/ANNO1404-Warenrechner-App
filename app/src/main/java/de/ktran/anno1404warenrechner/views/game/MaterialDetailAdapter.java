package de.ktran.anno1404warenrechner.views.game;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.common.base.Preconditions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import de.ktran.anno1404warenrechner.R;
import de.ktran.anno1404warenrechner.data.BuildingAlternative;
import de.ktran.anno1404warenrechner.data.DataManager;
import de.ktran.anno1404warenrechner.data.Game;
import de.ktran.anno1404warenrechner.data.Goods;
import de.ktran.anno1404warenrechner.data.ProductionBuilding;
import de.ktran.anno1404warenrechner.data.ProductionChain;
import de.ktran.anno1404warenrechner.event.ChainsDetailResultEvent;
import de.ktran.anno1404warenrechner.views.HasLifecycle;

public class MaterialDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements HasLifecycle {

    private DataManager dataManager;
    private final GameActivity activity;
    private final Game game;
    private final EventBus bus;

    private List<BuildingAlternative> chainData = new ArrayList<>();

    private ProductionChain chain;

    public MaterialDetailAdapter(DataManager dataManager, Game game, GameActivity activity, EventBus bus) {
        this.dataManager = dataManager;
        this.activity = activity;
        this.game = game;
        this.bus = bus;
    }

    @Override
    public void onStart() {
        bus.register(this);
        dataManager.registerUpdate(chain.getBuilding());
    }
    @Override
    public void onStop() {
        bus.unregister(this);
        dataManager.unregisterUpdate(chain.getBuilding());
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 2;
        }
        return 1;
    }

    @Override
    public int getItemCount() {
        return chainData.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 2) {
            return new MaterialOverviewAdapter.ViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.other_goods_item, parent, false)
            );
        }

        return new ChainsDetailAdapter.ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.chains_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MaterialOverviewAdapter.ViewHolder) {
            Preconditions.checkArgument(position == 0);
            final MaterialOverviewAdapter.ViewHolder viewHolder = (MaterialOverviewAdapter.ViewHolder) holder;

            final Goods goods = chain.getBuilding().getProduces();

            viewHolder.view.setImageDrawable(goods.getDrawable(activity));
            viewHolder.titleText.setText(goods.getName(activity));


            viewHolder.amountText.setValue(chain.getChains());
            viewHolder.amountText.setValueChangedListener((value, action) -> dataManager.setMaterialProduction(game, chain.getBuilding(), value));

            viewHolder.tpmText.setText(
                    activity.getString(R.string.tpm, chain.getBuilding().getTonsPerMin(chain.getChains(), game.getBonus(chain.getBuilding())))
            );

            final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
                    R.array.bonus, R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            viewHolder.spinner.setAdapter(adapter);
            viewHolder.spinner.setSelection(game.getBonus(chain.getBuilding()));

            viewHolder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (dataManager.setBonus(game, chain.getBuilding(), position)) {
                        dataManager.fetchChainsDetailResults(chain.getBuilding().getProduces(), game);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } else if (holder instanceof ChainsDetailAdapter.ViewHolder) {
            Preconditions.checkArgument(position != 0);
            final ChainsDetailAdapter.ViewHolder viewHolder = (ChainsDetailAdapter.ViewHolder) holder;

            final BuildingAlternative entry = chainData.get(position);

            final ProductionChain firstChain = entry.getChain();

            if (entry.getChain().getBuilding() == ProductionBuilding.CHARCOAL_BURNER) {
                viewHolder.view.setImageDrawable(
                        ContextCompat.getDrawable(activity, R.drawable.coal1)
                );
            } else if (entry.getChain().getBuilding() == ProductionBuilding.COAL_MINE) {
                viewHolder.view.setImageDrawable(
                        ContextCompat.getDrawable(activity, R.drawable.coal2)
                );
            } else {
                viewHolder.view.setImageDrawable(entry.getChain().getBuilding().getProduces().getDrawable(activity));
            }

            int percentage = (int) Math.floor(firstChain.getEfficiency() * 100);
            viewHolder.progressBar.setProgress(percentage);
            viewHolder.amountText.setText(String.valueOf(firstChain.getChains()));
            viewHolder.percentageText.setText(String.valueOf(percentage + "%"));


            final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
                    R.array.bonus, R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            viewHolder.spinner.setAdapter(adapter);
            viewHolder.spinner.setSelection(game.getBonus(firstChain.getBuilding()));

            viewHolder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (dataManager.setBonus(game, firstChain.getBuilding(), position)) {
                        dataManager.fetchChainsDetailResults(firstChain.getBuilding().getProduces(), game);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

    void init(ProductionChain chain) {
        this.chain = chain;
        this.chainData.add(
                BuildingAlternative.of(chain)
        );
        notifyDataSetChanged();

        dataManager.fetchChainsDetailResults(chain.getBuilding().getProduces(), game);
    }

    @SuppressWarnings("unused")
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChainsDetailResultEvent event) {
        if (!event.getGoods().getProductionBuilding().equals(this.chain.getBuilding())) return;

        this.chainData = event.getResult();
        this.chain = this.chainData.get(0).getChain();
        notifyDataSetChanged();
    }
}
