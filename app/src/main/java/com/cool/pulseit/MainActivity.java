package com.cool.pulseit;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cool.pulseit.fragments.AddPulseFragment;
import com.cool.pulseit.fragments.AnalyticsFragment;
import com.cool.pulseit.fragments.HistoryFragment;
import com.cool.pulseit.fragments.InfoFragment;
import com.cool.pulseit.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_analytics:
                            openFragment(AnalyticsFragment.newInstance());
                            return true;
                        case R.id.navigation_history:
                            openFragment(HistoryFragment.newInstance());
                            return true;
                        case R.id.navigation_addPulse:
                            openFragment(AddPulseFragment.newInstance());
                            return true;
                        case R.id.navigation_settings:
                            openFragment(SettingsFragment.newInstance());
                            return true;
                        case R.id.navigation_info:
                            openFragment(InfoFragment.newInstance());
                            return true;
                    }
                    return false;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        //InitialFragment
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, AnalyticsFragment.newInstance());
            transaction.commit();
        }
    }

    public void openFragment(final Fragment fragment) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        final int count = fragmentManager.getBackStackEntryCount();
        transaction.addToBackStack(null);
        transaction.commit();
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (fragmentManager.getBackStackEntryCount() <= count) {
                    fragmentManager.popBackStack();
                    fragmentManager.removeOnBackStackChangedListener(this);
                    bottomNavigation.getMenu().getItem((0)).setChecked(true);
                    Toast.makeText(MainActivity.this, R.string.main_message_ui_double_back_to_close, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void openDialogFragment(DialogFragment fragment) {
        fragment.show(getSupportFragmentManager(), "dialog");
    }
}
