package com.cool.pulseit.ui.analytics;

import com.cool.pulseit.entities.Pulse;
import com.cool.pulseit.utils.Result;

import java.util.List;

public class AnalyticsCalculator {
    private final List<Pulse> _pulses;

    public AnalyticsCalculator(List<Pulse> pulses) {
        _pulses = pulses;
    }

    public Result<Integer> calculateMaxPulse() {

        int maxPulse = Integer.MIN_VALUE;

        for (Pulse pulse : _pulses) {
            if (pulse.pulse > maxPulse) {
                maxPulse = pulse.pulse;
            }
        }

        return new Result<Integer>(true, maxPulse);
    }

    public Result<Integer> calculateMinPulse() {

        int minPulse = Integer.MAX_VALUE;

        for (Pulse pulse : _pulses) {
            if (pulse.pulse < minPulse) {
                minPulse = pulse.pulse;
            }
        }

        return new Result<Integer>(true, minPulse);
    }

    public Result<Integer> calculateAveragePulse() {
        int totalPulse = 0;

        for (Pulse pulse : _pulses) {
            totalPulse += pulse.pulse;
        }

        int averagePulse = totalPulse / _pulses.size();

        return new Result<Integer>(true, averagePulse);
    }


}
