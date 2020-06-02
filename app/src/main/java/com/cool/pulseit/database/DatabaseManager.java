package com.cool.pulseit.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cool.pulseit.entities.Pulse;
import com.cool.pulseit.entities.Settings;
import com.cool.pulseit.utils.DateFormatter;
import com.cool.pulseit.utils.Gender;
import com.cool.pulseit.utils.Result;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseManager extends SQLiteOpenHelper {
    public DatabaseManager(Context context) {
        super(context, "pulseit.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("PRAGMA foreign_keys = ON;");
            db.execSQL("CREATE TABLE settings (id INTEGER PRIMARY KEY AUTOINCREMENT, weight INTEGER NOT NULL, age INTEGER NOT NULL, gender TEXT NOT NULL, date TEXT NOT NULL);");
            db.execSQL("CREATE TABLE pulses (id INTEGER PRIMARY KEY AUTOINCREMENT, pulse INTEGER NOT NULL, date TEXT NOT NULL, settings_id INTEGER NOT NULL, FOREIGN KEY(settings_id) REFERENCES settings(id));");
        } catch (Exception ex) {
            Log.e(this.getClass().getName(), "Error while creating database tables");
        }
    }

    public Result saveSettings(Settings settings) {
        SQLiteDatabase db = getReadableDatabase();

        Result result = new Result();

        try {
            db.execSQL(String.format("INSERT INTO settings (weight,age,gender,date) VALUES(%s,%s,\"%s\",\"%s\");", settings.weight, settings.age, settings.gender, DateFormatter.toDb(settings.date)));

            result.setOk(true);
            result.setMessage("Gespeichert");
            return result;

        } catch (Exception ex) {
            Log.e(this.getClass().getName(), "Error while saving settings");

            result.setOk(false);
            result.setMessage("Fehler beim Speichern der Settings");
            return result;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Result<List<Pulse>> getPulses() {
        SQLiteDatabase db = getReadableDatabase();

        Result result = new Result();
        List<Pulse> pulses = new ArrayList<Pulse>();

        try {
            Cursor cursor = db.rawQuery("SELECT pulses.id AS pulses_id, pulses.date AS pulses_date, pulses.pulse AS pulses_pulse, settings.date AS settings_date, settings.id AS settings_id, settings.age AS settings_age, settings.weight AS settings_weight, settings.gender AS settings_gender FROM pulses INNER JOIN settings ON pulses.settings_id = settings.id;", null);

            int resultRows = cursor.getCount();
            if (resultRows == 0) {
                result.setOk(false);
                result.setMessage("Keine Pulses gefunden");
                return result;
            }

            while (cursor.moveToNext()) {

                int pulseId = cursor.getInt(cursor.getColumnIndex("pulses_id"));
                Date pulseDate = DateFormatter.fromDb(cursor.getString(cursor.getColumnIndex("pulses_date")));
                int pulsePulse = cursor.getInt(cursor.getColumnIndex("pulses_pulse"));
                Date settingsDate = DateFormatter.fromDb(cursor.getString(cursor.getColumnIndex("settings_date")));
                int settingsId = cursor.getInt(cursor.getColumnIndex("settings_id"));
                int settingsAge = cursor.getInt(cursor.getColumnIndex("settings_age"));
                int settingsWeight = cursor.getInt(cursor.getColumnIndex("settings_weight"));
                Gender settingsGender = Gender.toEnum(cursor.getString(cursor.getColumnIndex("settings_gender")));

                Settings settings = new Settings(settingsGender, settingsWeight, settingsAge, settingsDate, settingsId);
                Pulse pulse = new Pulse(pulseId, pulseDate, pulsePulse, settings);

                pulses.add(pulse);
            }

            result.setOk(true);
            result.setValue(pulses);

            return result;

        } catch (Exception ex) {
            Log.e(this.getClass().getName(), "Error while getting pulses");

            result.setOk(false);
            result.setMessage("Fehler beim Abrufen der Pulses");
            return result;
        }
    }

    public Result savePulse(Pulse pulse) {
        SQLiteDatabase db = getReadableDatabase();

        Result result = new Result();

        try {
            db.execSQL(String.format("INSERT INTO pulses (pulse, date, settings_id) VALUES (%s,\"%s\",%s);", pulse.pulse, DateFormatter.toDb(pulse.date), pulse.settings.getId()));

            result.setOk(true);
            result.setMessage("Gespeichert");
            return result;

        } catch (Exception ex) {
            Log.e(this.getClass().getName(), "Error while saving pulse");

            result.setOk(false);
            result.setMessage("Fehler beim Speichern des Pulses");
            return result;
        }
    }

    public Result<Settings> getLatestSettings() {
        SQLiteDatabase db = getReadableDatabase();

        Result<Settings> result = new Result<Settings>();

        Settings settings;

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM settings ORDER BY date DESC LIMIT 1;", null);
            int resultRows = cursor.getCount();
            if (resultRows == 0) {
                result.setOk(false);
                result.setMessage("Keine Settings Einträge gefunden");
                return result;
            }

            cursor.moveToFirst();

            int id = cursor.getInt(cursor.getColumnIndex("id"));
            int weight = cursor.getInt(cursor.getColumnIndex("weight"));
            int age = cursor.getInt(cursor.getColumnIndex("age"));
            Gender gender = Gender.toEnum(cursor.getString(cursor.getColumnIndex("gender")));
            Date date = DateFormatter.fromDb(cursor.getString(cursor.getColumnIndex("date")));

            settings = new Settings(gender, weight, age, date, id);

            result.setOk(true);
            result.setValue(settings);
            return result;

        } catch (Exception ex) {
            Log.e(this.getClass().getName(), "Error while retrieving Settings");

            result.setOk(false);
            result.setMessage("Fehler beim Abruf der Settings");
            return result;
        }
    }

    public Result deletePulse(Pulse pulse) {
        SQLiteDatabase db = getReadableDatabase();

        Result result = new Result();

        try {
            db.execSQL(String.format("DELETE FROM pulses WHERE id=\'%s\';", String.valueOf(pulse.getId())));

            result.setOk(true);
            result.setMessage("Gelöscht");
            return result;

        } catch (Exception ex) {
            Log.e(this.getClass().getName(), "Error while deleting pulse");

            result.setOk(false);
            result.setMessage("Fehler beim Löschen des Pulses");
            return result;
        }
    }
}
