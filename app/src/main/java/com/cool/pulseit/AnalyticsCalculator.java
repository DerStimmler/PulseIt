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

    public Result<Zone> calculateCommonZone(Date from, Date to){
        Result<List<Pulse>> result = filterRelevantPulses(from, to);

        if (!result.isOk()) {
            return new Result<Zone>(false, result.getMessage());
        }

        List<Zone> zones = new ArrayList<>();

        for(Pulse pulse : result.getValue()){
            ZoneCalculator zoneCalculator = new ZoneCalculator(pulse);
            zones.add(zoneCalculator.calculateZone());
        }

        Zone commonZone = ListHelper.mostCommon(zones);

        return new Result<Zone>(true, commonZone);
    }

    public Result<Integer> calculateMaxPulse(Date from, Date to) {
        Result<List<Pulse>> result = filterRelevantPulses(from, to);

        if (!result.isOk()) {
            return new Result<Integer>(false, result.getMessage());
        }

        int maxPulse = Integer.MIN_VALUE;

        for (Pulse pulse : result.getValue()) {
            if (pulse.pulse > maxPulse) {
                maxPulse = pulse.pulse;
            }
        }

        return new Result<Integer>(true, maxPulse);
    }

    public Result<Integer> calculateMinPulse(Date from, Date to) {
        Result<List<Pulse>> result = filterRelevantPulses(from, to);

        if (!result.isOk()) {
            return new Result<Integer>(false, result.getMessage());
        }

        int minPulse = Integer.MAX_VALUE;

        for (Pulse pulse : result.getValue()) {
            if (pulse.pulse < minPulse) {
                minPulse = pulse.pulse;
            }
        }

        return new Result<Integer>(true, minPulse);
    }

    private Result<List<Pulse>> filterRelevantPulses(Date from, Date to) {
        List<Pulse> filteredPulses = new ArrayList<>(_pulses);
        for (Pulse pulse : _pulses) {
            if (pulse.date.before(from) || pulse.date.after(to)) {
                filteredPulses.remove(pulse);
            }
        }

        Result<List<Pulse>> result = new Result<>();

        if (filteredPulses.isEmpty()) {
            result.setOk(false);
            result.setMessage(_context.getString(R.string.analytics_calculator_exception_ui_no_pulse_entries_in_date_range));

            return result;
        }

        result.setOk(true);
        result.setValue(filteredPulses);

        return result;
    }
}
