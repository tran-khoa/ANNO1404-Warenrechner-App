package de.ktran.anno1404warenrechner.views.game;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.ktran.anno1404warenrechner.views.BaseFragment;

public abstract class GameFragment extends BaseFragment {
    @NonNull
    public GameActivity getGameActivity() {
        if (!(getActivity() instanceof GameActivity)) {
            throw new IllegalStateException("PopulationFragment is initiated by the wrong activity.");
        }

        return (GameActivity) getActivity();
    }
}
