package com.cool.pulseit.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cool.pulseit.R;
import com.cool.pulseit.ui.addPulse.AddPulseFragment;
import com.cool.pulseit.ui.analytics.AnalyticsFragment;
import com.cool.pulseit.ui.history.HistoryFragment;
import com.cool.pulseit.ui.info.InfoFragment;
import com.cool.pulseit.ui.settings.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static Context _context;

    BottomNavigationView bottomNavigation;
    boolean doubleBackToExitPressedOnce = false;
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

    public static Context getContext() {
        return _context;
    }

    public static String getResourceString(int stringId) {
        return _context.getString(stringId);
    }

    public static int getResourceColor(int colorId) {
        return _context.getColor(colorId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _context = this;
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
        doubleBackToExitPressedOnce = false;
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (fragmentManager.getBackStackEntryCount() <= count) {
                    fragmentManager.popBackStack();
                    fragmentManager.removeOnBackStackChangedListener(this);
                    bottomNavigation.getMenu().getItem((0)).setChecked(true);
                    if (doubleBackToExitPressedOnce == false) {
                        doubleBackToExitPressedOnce = true;
                        Toast.makeText(MainActivity.this, R.string.main_message_ui_double_back_to_close, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}
