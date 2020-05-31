package com.cool.pulseit.entities;

import java.util.Date;

public class Pulse {
    private int id;
    public Date date;
    public int pulse;
    public Settings settings;

    public Pulse(int id, Date date, int pulse, Settings settings) {
        this.id = id;
        this.date = date;
        this.pulse = pulse;
        this.settings = settings;
    }

    public Pulse( Date date, int pulse, Settings settings) {
        this.date = date;
        this.pulse = pulse;
        this.settings = settings;
    }

    public int getId() {
        return id;
    }
}
