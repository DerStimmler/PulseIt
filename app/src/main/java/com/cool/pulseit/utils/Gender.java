package com.cool.pulseit.utils;

public enum Gender {
    MALE,
    FEMALE;

    public static Gender toEnum(String genderString) {
        switch (genderString){
            case "männlich":
                return Gender.MALE;
            case "weiblich":
                return Gender.FEMALE;
            default:
                throw new IllegalArgumentException(String.format("Can't convert %s to GenderEnum",genderString));
        }
    }

    public String toSqlValue(){
        return String.format("\"%s\"",this);
    }
}
