package com.cool.pulseit.ui.addPulse;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.cool.pulseit.ui.main.MainActivity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class HeartRateCalculator {
    private List<Long> _deltas;
    private Timestamp _previousTime;
    private Context _context = MainActivity.getContext();

    public HeartRateCalculator() {
        _deltas = new ArrayList<>();
        _previousTime = new Timestamp(System.currentTimeMillis());
    }

    public void tap() {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        vibrate();

        if ((now.getTime() - _previousTime.getTime()) > 3000) {
            restart();
            return;
        }

        _deltas.add(now.getTime() - _previousTime.getTime());

        _previousTime = now;
    }

    private void vibrate() {
        Vibrator v = (Vibrator) _context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(80, VibrationEffect.EFFECT_TICK));
        } else {
            //deprecated in API 26
            v.vibrate(80);
        }
    }

    public void restart() {
        _deltas.clear();
        _previousTime = new Timestamp(System.currentTimeMillis());
    }

    public int calculate() {
        if (_deltas.isEmpty()) {
            return 0;
        }

        Long sum = 0L;

        for (Long delta : _deltas) {
            sum += delta;
        }

        Long avg = sum / _deltas.size();

        return (int) (60 / (avg / 1000.0));
    }
}
