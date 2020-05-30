package com.cool.pulseit;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class HeartRateCalculator {
    List<Long> deltas;
    Timestamp previousTime;

    public HeartRateCalculator(){
        deltas = new ArrayList<Long>();
        previousTime = new Timestamp(System.currentTimeMillis());
    }

    public void tap() {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        if((now.getTime() - previousTime.getTime()) > 3000){
            restart();
            return;
        }

        deltas.add(now.getTime() - previousTime.getTime());

        previousTime = now;
    }

    public void restart(){
        deltas.clear();
        previousTime = new Timestamp(System.currentTimeMillis());
    }

    public int calculate(){
        if(deltas.isEmpty()){
            return 0;
        }

        Long sum = 0L;

        for(Long delta : deltas){
            sum += delta;
        }

        Long avg = sum / deltas.size();

        int x = (int) (60 / (avg / 1000.0));
        return x;
    }
}
