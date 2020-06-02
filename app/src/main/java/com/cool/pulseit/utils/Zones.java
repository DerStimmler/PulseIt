package com.cool.pulseit.utils;

public enum Zones {
    NONE ("keine Zone"),
    VERYLIGHT ("Gesundheitszone"),
    LIGHT ("Fettverbrennungszone"),
    MODERATE("aerobe Zone"),
    HARD ("anaerobe Zone"),
    VERYHARD ("rote Zone");

    public static Zones toEnum(String zonesString) {
        switch (zonesString) {
            case "keine Zone":
            case "NONE":
                return Zones.NONE;
            case "Gesundheitszone":
            case "VERYLIGHT":
                return Zones.VERYLIGHT;
            case "Fettverbrennungszone":
            case "LIGHT":
                return Zones.LIGHT;
            case "aerobe Zone":
            case "MODERATE":
                return Zones.MODERATE;
            case "anaerobe Zone":
            case "HARD":
                return Zones.HARD;
            case "rote Zone":
            case "VERYHARD":
                return Zones.VERYHARD;
            default:
                throw new IllegalArgumentException(String.format("Can't convert %s to ZonesEnum", zonesString));
        }
    }

    private String value;

    Zones(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }

}
