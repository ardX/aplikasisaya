package com.example.aplikasisaya;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class DemoCollectionAdapter extends FragmentStateAdapter {
    int jumlahTab = 0;

    public DemoCollectionAdapter(@NonNull FragmentActivity fragmentActivity, int jumlahTab) {
        super(fragmentActivity);
        this.jumlahTab = jumlahTab;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new GempaFragment();
                break;
            case 1:
                fragment = new ListGempaFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return jumlahTab;
    }
}