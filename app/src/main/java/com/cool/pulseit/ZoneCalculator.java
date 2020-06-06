package com.cool.pulseit;

import com.cool.pulseit.entities.Pulse;
import com.cool.pulseit.utils.Gender;
import com.cool.pulseit.utils.Zone;


public class ZoneCalculator {
    private int _pulse;
    private int _age;
    private int _weight;
    private Gender _gender;

    public ZoneCalculator(int pulse, int age, int weight, Gender gender){
        _pulse = pulse;
        _age = age;
        _weight= weight;
        _gender=gender;
    }

    public ZoneCalculator(Pulse pulse){
        this(pulse.pulse,pulse.settings.age, pulse.settings.weight, pulse.settings.gender);
    }

    public Zone calculateZone() {
        MaximumHeartRateCalculator maximumHeartRateCalculator = new MaximumHeartRateCalculator(_age,_weight,_gender);
        int maximumHeartRate = maximumHeartRateCalculator.calculateMaximumHeartRate();
        int pulseZone = (int) ((100.0 / maximumHeartRate) * _pulse);

        if (pulseZone > 90) {
            return Zone.VERYHARD;
        } else if (pulseZone > 80 && pulseZone < 90) {
            return Zone.HARD;
        } else if (pulseZone > 70 && pulseZone < 80) {
            return Zone.MODERATE;
        } else if (pulseZone > 60 && pulseZone < 70) {
            return Zone.LIGHT;
        } else if (pulseZone > 50 && pulseZone < 60) {
            return Zone.VERYLIGHT;
        }
        return Zone.NONE;
    }
}
