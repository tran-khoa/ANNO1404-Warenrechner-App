package de.ktran.anno1404warenrechner.views.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.ktran.anno1404warenrechner.R;
import de.ktran.anno1404warenrechner.data.DataManager;
import de.ktran.anno1404warenrechner.data.Game;
import de.ktran.anno1404warenrechner.data.Population;
import de.ktran.anno1404warenrechner.event.GameListResultEvent;
import de.ktran.anno1404warenrechner.helpers.DisplayHelper;
import de.ktran.anno1404warenrechner.views.HasLifecycle;
import de.ktran.anno1404warenrechner.views.game.GameActivity;


public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.ViewHolder> implements HasLifecycle {
    private final EventBus bus;
    private final MainActivity activity;
    private final DataManager dataManager;

    private List<Game> data = new ArrayList<>();

    public GameListAdapter(@NonNull final MainActivity activity, @NonNull final EventBus bus, @NonNull final DataManager dataManager) {
        this.bus = bus;
        this.activity = activity;
        this.dataManager = dataManager;
    }

    @Override
    public void onStart() {
        bus.register(this);

        if (this.bus.getStickyEvent(GameListResultEvent.class) == null) {
            this.dataManager.fetchGameList();
        }
    }

    @Override
    public void onStop() {
        bus.unregister(this);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.game_item_name)
        TextView mName;

        @BindView(R.id.game_item_date)
        TextView mDate;

        @BindView(R.id.game_item)
        CardView mParent;

        @BindView(R.id.game_item_popcount)
        TextView mPopCount;

        @BindView(R.id.game_item_poptype_list)
        ViewGroup mPopType;

        @BindView(R.id.game_item_click)
        View mClick;

        @BindView(R.id.game_list_item_bg)
        SimpleDraweeView mBackground;

        ViewHolder(View v) {
            super(v);

            ButterKnife.bind(this, v);
        }
    }

    @Override
    public GameListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_list_item, parent, false);
        return new GameListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GameListAdapter.ViewHolder holder, int position) {
        final Game game = data.get(position);

        holder.mClick.setOnClickListener(v -> GameActivity.startActivity(activity, game.getId()));

        holder.mName.setText(game.getName());
        holder.mDate.setText(new SimpleDateFormat("dd. MMMM yyyy - HH:mm", Locale.getDefault()).format(game.getDate()));
        holder.mPopCount.setText(String.valueOf(game.getPopCount()));

        holder.mBackground.setImageResource(getDrawableByCiv(game));

        holder.mPopType.removeAllViews();
        final int dimens = DisplayHelper.dpToPx(activity, 15);
        for (Population p : game.getHighestCivs()) {
            final ImageView imageView = new ImageView(activity);
            imageView.setImageDrawable(p.getIcon(activity));
            imageView.setLayoutParams(new RecyclerView.LayoutParams(dimens, dimens));

            holder.mPopType.addView(imageView);
        }
    }

    private int getDrawableByCiv(Game game) {
        final List<Population> highestCivs = game.getHighestCivs();

        if (highestCivs.contains(Population.NOBLEMEN)) {
            int count = game.getPopulation().get(Population.NOBLEMEN);
            if (count >= 3500) {
                return R.drawable.bg_noblemen_3;
            } else if (count >= 750) {
                return R.drawable.bg_noblemen_2;
            }
            return R.drawable.bg_noblemen_1;
        } else if (highestCivs.contains(Population.PATRICIANS)) {
            return R.drawable.bg_patricians;
        }

        return R.drawable.bg_beginning1;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    @SuppressWarnings("unused")
    public void onMessageEvent(GameListResultEvent event) {
        this.data = event.getResults();

        notifyDataSetChanged();
    }
}
