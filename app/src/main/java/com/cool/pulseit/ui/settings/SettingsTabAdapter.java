package com.cool.pulseit.ui.settings;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SettingsTabAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> _tabFragments = new ArrayList<>();
    private List<String> _tabFragmentsTitles = new ArrayList<>();

    public SettingsTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return _tabFragments.get(position);
    }

    @Override
    public int getCount() {
        return _tabFragments.size();
    }

    public void addFragment(Fragment fragment, String title) {
        _tabFragments.add(fragment);
        _tabFragmentsTitles.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return _tabFragmentsTitles.get(position);
    }
}
