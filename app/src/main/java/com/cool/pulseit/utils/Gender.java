package com.cool.pulseit.utils;

public enum Gender {
    MALE("männlich"),
    FEMALE("weiblich");

    private String value;

    Gender(final String value) {
        this.value = value;
    }

    public static Gender toEnum(String genderString) {
        switch (genderString) {
            case "männlich":
            case "MALE":
                return Gender.MALE;
            case "weiblich":
            case "FEMALE":
                return Gender.FEMALE;
            default:
                throw new IllegalArgumentException(String.format("Can't convert %s to GenderEnum", genderString));
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
