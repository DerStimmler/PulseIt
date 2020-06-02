package com.cool.pulseit;

import com.cool.pulseit.utils.Gender;
import com.cool.pulseit.utils.Zones;


public class ZoneCalculator {
    private int _pulse;
    private int _age;
    private int _weight;
    private Enum<Gender> _gender;

    public ZoneCalculator(int pulse, int age, int weight, Enum<Gender> gender){
        _pulse = pulse;
        _age = age;
        _weight= weight;
        _gender=gender;
    }

    public String calculateZone() {
        MaximumHeartRateCalculator maximumHeartRateCalculator = new MaximumHeartRateCalculator(_age,_weight,_gender);
        int maximumHeartRate = maximumHeartRateCalculator.calculateMaximumHeartRate();
        int pulseZone = (int) ((100.0 / maximumHeartRate) * _pulse);

        if (pulseZone > 90) {
            return Zones.VERYHARD.toString();
        } else if (pulseZone > 80 && pulseZone < 90) {
            return Zones.HARD.toString();
        } else if (pulseZone > 70 && pulseZone < 80) {
            return Zones.MODERATE.toString();
        } else if (pulseZone > 60 && pulseZone < 70) {
            return Zones.LIGHT.toString();
        } else if (pulseZone > 50 && pulseZone < 60) {
            return Zones.VERYLIGHT.toString();
        }
        return Zones.NONE.toString();
    }
}
