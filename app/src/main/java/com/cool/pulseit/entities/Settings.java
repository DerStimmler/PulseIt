package com.cool.pulseit.entities;

import com.cool.pulseit.utils.Gender;

public class Settings {
    public Gender gender;
    public int weight;
    public int age;

    public Settings(Gender gender, int weight, int age){
        this.gender = gender;
        this.weight = weight;
        this.age = age;
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
