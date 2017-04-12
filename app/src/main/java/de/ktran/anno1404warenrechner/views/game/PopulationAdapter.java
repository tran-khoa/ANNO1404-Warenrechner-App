package de.ktran.anno1404warenrechner.views.game;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.ktran.anno1404warenrechner.R;
import de.ktran.anno1404warenrechner.data.Game;
import de.ktran.anno1404warenrechner.data.Population;
import de.ktran.anno1404warenrechner.data.PopulationType;
import de.ktran.anno1404warenrechner.event.PopulationResultEvent;
import de.ktran.anno1404warenrechner.views.HasLifecycle;

public class PopulationAdapter extends RecyclerView.Adapter<PopulationAdapter.ViewHolder> implements HasLifecycle {

    private final EventBus bus;
    private final GameActivity activity;

    private Population populationData;
    private Game game = null;

    public PopulationAdapter(@NonNull final Game game, @NonNull final GameActivity activity, @NonNull final EventBus bus) {
        this.activity = activity;
        this.bus = bus;

        this.game = game;
        this.populationData = game.population();
    }

    @Override
    public void onStart() {
        this.bus.register(this);
    }

    @Override
    public void onStop() {
        this.bus.unregister(this);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.populationItem)
        FrameLayout popParent;

        @BindView(R.id.populationItemClick)
        RelativeLayout popClick;

        @BindView(R.id.populationItemIcon)
        ImageView popIcon;

        @BindView(R.id.populationItemName)
        TextView popName;

        @BindView(R.id.populationItemCount)
        TextView popCount;

        @BindView(R.id.populationItemHouseCount)
        TextView houseCount;

        ViewHolder(View v) {
            super(v);

            ButterKnife.bind(this, v);
        }
    }

    @Override
    public PopulationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.population_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (position == 0) { // occidental
            holder.popParent.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
            holder.popClick.setOnClickListener(v -> activity.showHouseEditDialog(PopulationType.Civilization.OCCIDENTAL));

            holder.popName.setText(R.string.pop_total_occidental);
            holder.popIcon.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_pop_occidental));
            holder.popCount.setText(String.valueOf(game.population().getPopulationCount(PopulationType.Civilization.OCCIDENTAL)));
            holder.houseCount.setText(String.valueOf(game.population().getHouseCount(PopulationType.Civilization.OCCIDENTAL)));
        } else if (position == 1) { // oriental
            holder.popParent.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimary));
            holder.popClick.setOnClickListener(v -> activity.showHouseEditDialog(PopulationType.Civilization.ORIENTAL));

            holder.popName.setText(R.string.pop_total_oriental);
            holder.popIcon.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_pop_orient));
            holder.popCount.setText(String.valueOf(game.population().getPopulationCount(PopulationType.Civilization.ORIENTAL)));
            holder.houseCount.setText(String.valueOf(game.population().getHouseCount(PopulationType.Civilization.ORIENTAL)));
        } else {
            final PopulationType pop = PopulationType.values()[position - 2];
            final int popCount = populationData.getPopulationCount(pop);
            final int houseCount = pop.getHouseCountByPopSize(popCount);

            holder.popParent.setBackgroundColor(pop.getColor(activity));
            holder.popClick.setOnClickListener(v -> activity.showPopEditDialog(pop));

            holder.popName.setText(pop.getString(activity));
            holder.popIcon.setImageDrawable(pop.getIcon(activity));
            holder.popCount.setText(String.valueOf(popCount));
            holder.houseCount.setText(String.valueOf(houseCount));
        }
    }

    @Override
    public int getItemCount() {
        return (game != null) ? PopulationType.values().length + 2 : PopulationType.values().length;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @SuppressWarnings("unused")
    public void onMessageEvent(PopulationResultEvent event) {
        this.populationData = event.getGame().population();
        this.game = event.getGame();
        notifyDataSetChanged();
    }
}
