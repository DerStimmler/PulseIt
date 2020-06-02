package com.cool.pulseit.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

public class InputFilterMinMax implements InputFilter {

    private int _min;
    private int _max;

    public InputFilterMinMax(int min, int max) {
        this._min = min;
        this._max = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            int input = Integer.parseInt(dest.toString() + source.toString());
            if (isInRange(_min, _max, input))
                return null;
        } catch (NumberFormatException nfe) {
            Log.e("InputFilterMinMax", "Could not parse input to Integer");
        }
        return "";
    }

    private boolean isInRange(int min, int max, int input) {
        return max > min ? input >= min && input <= max : input >= max && input <= min;
    }
}
