package com.cool.pulseit.utils;

import android.app.Activity;

import com.cool.pulseit.R;
import com.google.android.material.snackbar.Snackbar;

public class StatusSnackbar {
    public static void show(Activity mainActivity, String message) {
        Snackbar s = Snackbar.make(mainActivity.findViewById(R.id.bottom_navigation), message, Snackbar.LENGTH_SHORT);
        s.setAnchorView(mainActivity.findViewById(R.id.bottom_navigation));
        s.show();
    }
}
