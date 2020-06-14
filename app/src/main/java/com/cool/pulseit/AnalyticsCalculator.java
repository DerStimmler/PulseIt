package com.cool.pulseit;

import android.content.Context;

import com.cool.pulseit.entities.Pulse;
import com.cool.pulseit.utils.ListHelper;
import com.cool.pulseit.utils.Result;
import com.cool.pulseit.utils.Zone;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AnalyticsCalculator {
    private final List<Pulse> _pulses;
    private final Context _context;

    public AnalyticsCalculator(List<Pulse> pulses, Context context) {
        _pulses = pulses;
        _context = context;
    }

    public Result<Zone> calculateCommonZone() {

        List<Zone> zones = new ArrayList<>();

        for (Pulse pulse : _pulses) {
            ZoneCalculator zoneCalculator = new ZoneCalculator(pulse);
            zones.add(zoneCalculator.calculateZone());
        }

        Zone commonZone = ListHelper.mostCommon(zones);

        return new Result<Zone>(true, commonZone);
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

    public Result<Integer> calculateAveragePulse(){
        int totalPulse = 0;

        for(Pulse pulse : _pulses){
            totalPulse += pulse.pulse;
        }

        int averagePulse = totalPulse / _pulses.size();

        return new Result<Integer>(true, averagePulse);
    }


}
