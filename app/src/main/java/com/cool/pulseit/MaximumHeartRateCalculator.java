package com.cool.pulseit;

import com.cool.pulseit.utils.Gender;

public class MaximumHeartRateCalculator {

    private int _age;
    private int _weight;
    private Enum<Gender> _gender;

    public MaximumHeartRateCalculator(int age, int weight, Enum<Gender> gender) {
        _age = age;
        _weight = weight;
        _gender = gender;
    }

    public int calculateMaximumHeartRate() {
        int mhr = 0;
        if (_gender == Gender.MALE) {
            mhr = (int) (214 - (0.5 * _age) - (0.11 * _weight));
        } else if (_gender == Gender.FEMALE) {
            mhr = (int) (210 - (0.5 * _age) - (0.11 * _weight));
        }
        return mhr;
    }
}
