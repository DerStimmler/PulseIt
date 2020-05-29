package com.cool.pulseit.entities;

import com.cool.pulseit.utils.Gender;

import java.util.Date;

public class Settings {

    public Gender gender;
    public int weight;
    public int age;
    public Date date;

    public Settings(Gender gender, int weight, int age, Date date){
        this.gender = gender;
        this.weight = weight;
        this.age = age;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
