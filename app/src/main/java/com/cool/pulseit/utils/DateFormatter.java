package com.cool.pulseit.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateFormatter {

    private DateFormatter() {
    }

    public static String toDb(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return format.format(date);
    }

    public static Date fromDb(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            Log.e(DateFormatter.class.getName(), "Couldn't parse String " + dateString + " to Date");
        }

        return date;
    }

    public static String forUiWithTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return format.format(date);
    }

    public static String forUiWithoutTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        return format.format(date);
    }
}
