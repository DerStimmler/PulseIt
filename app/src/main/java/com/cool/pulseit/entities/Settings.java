package com.cool.pulseit.entities;

import com.cool.pulseit.enums.Gender;

import java.util.Date;

public class Settings {

    public Gender gender;
    public int weight;
    public int age;
    public Date date;
    private int id;

    public Settings(Gender gender, int weight, int age, Date date) {
        this.gender = gender;
        this.weight = weight;
        this.age = age;
        this.date = date;
    }

    public Settings(Gender gender, int weight, int age, Date date, int id) {
        this.gender = gender;
        this.weight = weight;
        this.age = age;
        this.date = date;
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
