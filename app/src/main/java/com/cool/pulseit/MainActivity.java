package com.cool.pulseit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.cool.pulseit.fragments.AddPulseFragment;
import com.cool.pulseit.fragments.AnalyticsFragment;
import com.cool.pulseit.fragments.HistoryFragment;
import com.cool.pulseit.fragments.InfoFragment;
import com.cool.pulseit.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigation = findViewById(R.id.bottom_navigation);
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_analytics:
                            openFragment(AnalyticsFragment.newInstance("", ""));
                            return true;
                        case R.id.navigation_history:
                            openFragment(HistoryFragment.newInstance("", ""));
                            return true;
                        case R.id.navigation_addPulse:
                            openFragment(AddPulseFragment.newInstance("", ""));
                            return true;
                        case R.id.navigation_settings:
                            openFragment(SettingsFragment.newInstance("", ""));
                            return true;
                        case R.id.navigation_info:
                            openFragment(InfoFragment.newInstance("", ""));
                            return true;
                    }
                    return false;
                }
            };
}
