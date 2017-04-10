package de.ktran.anno1404warenrechner.views.game;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import de.ktran.anno1404warenrechner.R;
import de.ktran.anno1404warenrechner.data.Game;

public class GameSectionsPagerAdapter extends FragmentPagerAdapter {

    private final Context context;

    public GameSectionsPagerAdapter(@NonNull final Context context, @NonNull final FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(final int position) {
        switch (position) {
            case 0:
                return new PopulationFragment();
            case 1:
                return new ChainsFragment();
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(final int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.population).toUpperCase();
            case 1:
                return context.getString(R.string.consumption).toUpperCase();
        }

        throw new UnsupportedOperationException();
    }
}
