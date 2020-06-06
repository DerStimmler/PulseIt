package com.cool.pulseit.utils;

public enum Zone {
    NONE("keine Zone"),
    VERYLIGHT("Gesundheitszone"),
    LIGHT("Fettverbrennungszone"),
    MODERATE("aerobe Zone"),
    HARD("anaerobe Zone"),
    VERYHARD("Warnzone");

    private String value;

    Zone(final String value) {
        this.value = value;
    }

    public static Zone toEnum(String zonesString) {
        switch (zonesString) {
            case "keine Zone":
            case "NONE":
                return Zone.NONE;
            case "Gesundheitszone":
            case "VERYLIGHT":
                return Zone.VERYLIGHT;
            case "Fettverbrennungszone":
            case "LIGHT":
                return Zone.LIGHT;
            case "aerobe Zone":
            case "MODERATE":
                return Zone.MODERATE;
            case "anaerobe Zone":
            case "HARD":
                return Zone.HARD;
            case "Warnzone":
            case "VERYHARD":
                return Zone.VERYHARD;
            default:
                throw new IllegalArgumentException(String.format("Can't convert %s to ZonesEnum", zonesString));
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
