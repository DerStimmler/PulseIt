package com.cool.pulseit.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.cool.pulseit.R;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    private View _view;
    private ViewPager _viewPager;
    private TabLayout _tabLayout;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.fragment_settings, container, false);

        _tabLayout = _view.findViewById(R.id.settings_tablayout);
        _viewPager = _view.findViewById(R.id.settings_viewpager);

        SettingsTabAdapter tabAdapter = new SettingsTabAdapter(getFragmentManager());
        SettingsResultFragment settingsResultFragment = SettingsResultFragment.newInstance();
        SettingsInputFragment settingsInputFragment = SettingsInputFragment.newInstance(settingsResultFragment);
        tabAdapter.addFragment(settingsInputFragment, getString(R.string.settings_tab_input));
        tabAdapter.addFragment(settingsResultFragment, getString(R.string.settings_tab_result));

        _viewPager.setAdapter(tabAdapter);
        _tabLayout.setupWithViewPager(_viewPager);

        return _view;
    }
}
