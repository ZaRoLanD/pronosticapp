package com.zaro.pronosticapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.zaro.pronosticapp.fragments.HistoryFragment;
import com.zaro.pronosticapp.fragments.MatchListFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MatchListFragment(); // Onglet "Pronostics"
            case 1:
                return new HistoryFragment(); // Onglet "Historique"
            default:
                return new MatchListFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Nombre d'onglets
    }
}