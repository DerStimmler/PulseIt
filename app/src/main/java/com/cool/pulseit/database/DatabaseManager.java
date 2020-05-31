package com.cool.pulseit.database;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cool.pulseit.entities.Pulse;
import com.cool.pulseit.entities.Settings;
import com.cool.pulseit.utils.DateFormatter;
import com.cool.pulseit.utils.Gender;

import java.util.Date;

public class DatabaseManager extends SQLiteOpenHelper {
    public DatabaseManager(Context context){
        super(context, "pulseit.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL("CREATE TABLE settings (id INTEGER PRIMARY KEY AUTOINCREMENT, weight INTEGER NOT NULL, age INTEGER NOT NULL, gender TEXT NOT NULL, date TEXT NOT NULL);");
            db.execSQL("CREATE TABLE pulses (id INTEGER PRIMARY KEY AUTOINCREMENT, pulse INTEGER NOT NULL, date TEXT NOT NULL, settings INTEGER, FOREIGN KEY(settings) REFERENCES settings(id));");
        }catch(Exception ex){
            Log.e(this.getClass().getName(), "Error while creating database tables");
        }
    }

    public void saveSettings(Settings settings) {
        SQLiteDatabase db = getReadableDatabase();
        try {
            db.execSQL(String.format("INSERT INTO settings (weight,age,gender,date) VALUES(%s,%s,\"%s\",\"%s\")", settings.weight, settings.age, settings.gender, DateFormatter.toDb(settings.date)));
        }catch (Exception ex){
            Log.e(this.getClass().getName(), "Error while saving settings");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void savePulse(Pulse pulse) {
        SQLiteDatabase db = getReadableDatabase();

        try{
            db.execSQL(String.format("INSERT INTO pulses (pulse, date, settings) VALUES (%s,%s,%s)",pulse.pulse, DateFormatter.toDb(pulse.date),pulse.settings.getId()));
        }catch(Exception ex){
            Log.e(this.getClass().getName(), "Error while saving pulse");
        }
    }

    public Settings getLatestSettings() {
        SQLiteDatabase db = getReadableDatabase();

        Settings settings = null;

        try{
            Cursor cursor = db.rawQuery("SELECT * FROM settings ORDER BY date DESC LIMIT 1", null);
            int resultRows = cursor.getCount();
            if(resultRows == 0){
                throw new Resources.NotFoundException();
            }

            cursor.moveToFirst();

            int id = cursor.getInt(cursor.getColumnIndex("id"));
            int weight = cursor.getInt(cursor.getColumnIndex("weight"));
            int age = cursor.getInt(cursor.getColumnIndex("age"));
            Gender gender = Gender.toEnum(cursor.getString(cursor.getColumnIndex("gender")));
            Date date = DateFormatter.fromDb(cursor.getString(cursor.getColumnIndex("date")));

            settings = new Settings(gender,weight,age,date,id);

        }catch(Exception ex){
            Log.e(this.getClass().getName(), "Error while retrieving Settings");
        }

        return settings;
    }
}
