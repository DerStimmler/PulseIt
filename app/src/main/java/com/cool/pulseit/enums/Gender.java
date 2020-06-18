package com.cool.pulseit.enums;

import android.util.Log;

import com.cool.pulseit.R;
import com.cool.pulseit.ui.main.MainActivity;

public enum Gender {
    MALE,
    FEMALE;

    public static Gender toEnum(String genderString) {
        if (genderString.equals(MainActivity.getResourceString(R.string.gender_male)))
            return Gender.MALE;
        if (genderString.equals(MainActivity.getResourceString(R.string.gender_female)))
            return Gender.FEMALE;

        Log.e("Gender", String.format("Can't convert %s to GenderEnum", genderString));
        throw new IllegalArgumentException(String.format("Can't convert %s to GenderEnum", genderString));
    }

    @Override
    public String toString() {
        if (this == Gender.MALE)
            return MainActivity.getResourceString(R.string.gender_male);

        return MainActivity.getResourceString(R.string.gender_female);
    }
}
