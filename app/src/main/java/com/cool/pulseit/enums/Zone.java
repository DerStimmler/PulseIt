package com.cool.pulseit.enums;

import android.util.Log;

import com.cool.pulseit.MainActivity;
import com.cool.pulseit.R;

public enum Zone {
    NONE,
    VERYLIGHT,
    LIGHT,
    MODERATE,
    HARD,
    VERYHARD;

    public static Zone toEnum(String zonesString) {

        if(zonesString.equals(MainActivity.getResourceString(R.string.zone_none)))
            return Zone.NONE;
        if(zonesString.equals(MainActivity.getResourceString(R.string.zone_verylight)))
            return Zone.VERYLIGHT;
        if(zonesString.equals(MainActivity.getResourceString(R.string.zone_light)))
            return Zone.LIGHT;
        if(zonesString.equals(MainActivity.getResourceString(R.string.zone_moderate)))
            return Zone.MODERATE;
        if(zonesString.equals(MainActivity.getResourceString(R.string.zone_hard)))
            return Zone.HARD;
        if(zonesString.equals(MainActivity.getResourceString(R.string.zone_veryhard)))
            return Zone.VERYHARD;

        Log.e("Zone", String.format("Can't convert %s to ZoneEnum", zonesString));
        throw new IllegalArgumentException(String.format("Can't convert %s to ZoneEnum", zonesString));
    }

    @Override
    public String toString() {
        if(this == Zone.NONE)
            return MainActivity.getResourceString(R.string.zone_none);
        if(this == Zone.VERYLIGHT)
            return MainActivity.getResourceString(R.string.zone_verylight);
        if(this == Zone.LIGHT)
            return MainActivity.getResourceString(R.string.zone_light);
        if(this == Zone.MODERATE)
            return MainActivity.getResourceString(R.string.zone_moderate);
        if(this == Zone.HARD)
            return MainActivity.getResourceString(R.string.zone_hard);

        return MainActivity.getResourceString(R.string.zone_veryhard);
    }
}
