package com.cool.pulseit.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cool.pulseit.entities.Settings;

public class DatabaseManager extends SQLiteOpenHelper {
    public DatabaseManager(Context context){
        super(context, "pulseit.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL("CREATE TABLE settings (id INTEGER PRIMARY KEY AUTOINCREMENT, weight INTEGER NOT NULL, age INTEGER NOT NULL, gender TEXT NOT NULL);");
        }catch(Exception ex){
            Log.e("DatabaseManager", "Error while creating database tables");
        }
    }

    public void saveSettings(Settings settings) {
        SQLiteDatabase db = getReadableDatabase();

        db.execSQL(String.format("INSERT INTO settings (weight,age,gender) VALUES(%s,%s,%s)",settings.weight, settings.age, settings.gender.toSqlValue()));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
