package com.cool.pulseit.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

public class InputFilterGreaterZero implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            int input = Integer.parseInt(dest.toString() + source.toString());
            if (isGreaterZero(input))
                return null;
        } catch (NumberFormatException nfe) {
            Log.e(this.getClass().getName(), "Could not parse input to Integer");
        }
        return "";
    }

    private boolean isGreaterZero(int input) {
        return input > 0;
    }
}
