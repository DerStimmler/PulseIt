package com.cool.pulseit.entities;

import java.util.Date;

public class Pulse {
    public Date date;
    public int pulse;
    public Settings settings;
    public String description;
    private int id;

    public Pulse(int id, Date date, int pulse, String description, Settings settings) {
        this.id = id;
        this.date = date;
        this.pulse = pulse;
        this.settings = settings;
        this.description = description;
    }

    public Pulse(Date date, int pulse, String description, Settings settings) {
        this.date = date;
        this.pulse = pulse;
        this.settings = settings;
        this.description = description;
    }

    public int getId() {
        return id;
    }
}
