package com.cool.pulseit.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.cool.pulseit.R;
import com.cool.pulseit.entities.Pulse;
import com.cool.pulseit.entities.Settings;
import com.cool.pulseit.enums.Gender;
import com.cool.pulseit.utils.DateFormatter;
import com.cool.pulseit.utils.Result;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseManager extends SQLiteOpenHelper {
    private final Context _context;

    public DatabaseManager(Context context) {
        super(context, "pulseit.db", null, 1);
        _context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("PRAGMA foreign_keys = ON;");
            db.execSQL("CREATE TABLE settings (id INTEGER PRIMARY KEY AUTOINCREMENT, weight INTEGER NOT NULL, age INTEGER NOT NULL, gender TEXT NOT NULL, date TEXT NOT NULL);");
            db.execSQL("CREATE TABLE pulses (id INTEGER PRIMARY KEY AUTOINCREMENT, pulse INTEGER NOT NULL, description TEXT, date TEXT NOT NULL, settings_id INTEGER NOT NULL, FOREIGN KEY(settings_id) REFERENCES settings(id));");
        } catch (Exception ex) {
            Log.e(this.getClass().getName(), _context.getString(R.string.database_message_log_error_create_tables));
        }
    }

    public Result saveSettings(Settings settings) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "INSERT INTO settings (weight,age,gender,date) VALUES(?,?,?,?)";

        Result result = new Result();

        try {
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindLong(1, settings.weight);
            statement.bindLong(2, settings.age);
            statement.bindString(3, settings.gender.toString());
            statement.bindString(4, DateFormatter.toDb(settings.date));
            statement.executeInsert();

            result.setOk(true);
            result.setMessage(_context.getString(R.string.database_message_ui_saved));
            return result;

        } catch (Exception ex) {
            Log.e(this.getClass().getName(), _context.getString(R.string.database_message_log_error_save_settings));

            result.setOk(false);
            result.setMessage(_context.getString(R.string.database_message_ui_error_save_settings));
            return result;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Result<List<Pulse>> getPulses(Date from, Date to) {
        Result<List<Pulse>> result = new Result<>();

        Result<List<Pulse>> allPulses = getPulses();

        if (!allPulses.isOk()) {
            result.setOk(false);
            result.setMessage(allPulses.getMessage());
            return result;
        }

        List<Pulse> filteredPulses = new ArrayList<>();
        filteredPulses.addAll(allPulses.getValue());

        for (Pulse pulse : allPulses.getValue()) {
            if (pulse.date.before(from) || pulse.date.after(to)) {
                filteredPulses.remove(pulse);
            }
        }

        if (filteredPulses.isEmpty()) {
            result.setOk(false);
            result.setMessage(_context.getString(R.string.analytics_calculator_exception_ui_no_pulse_entries_in_date_range));

            return result;
        }

        result.setOk(true);
        result.setValue(filteredPulses);

        return result;
    }

    public Result<List<Pulse>> getPulses() {
        SQLiteDatabase db = getReadableDatabase();

        Result result = new Result();
        List<Pulse> pulses = new ArrayList<Pulse>();

        try {
            Cursor cursor = db.rawQuery("SELECT pulses.id AS pulses_id, pulses.date AS pulses_date, pulses.pulse AS pulses_pulse, pulses.description AS pulses_description, settings.date AS settings_date, settings.id AS settings_id, settings.age AS settings_age, settings.weight AS settings_weight, settings.gender AS settings_gender FROM pulses INNER JOIN settings ON pulses.settings_id = settings.id;", null);

            int resultRows = cursor.getCount();
            if (resultRows == 0) {
                result.setOk(false);
                result.setMessage(_context.getString(R.string.database_message_ui_error_no_pulses_found));
                return result;
            }

            while (cursor.moveToNext()) {

                int pulseId = cursor.getInt(cursor.getColumnIndex("pulses_id"));
                Date pulseDate = DateFormatter.fromDb(cursor.getString(cursor.getColumnIndex("pulses_date")));
                int pulsePulse = cursor.getInt(cursor.getColumnIndex("pulses_pulse"));
                String pulseDescription = cursor.getString(cursor.getColumnIndex("pulses_description"));
                Date settingsDate = DateFormatter.fromDb(cursor.getString(cursor.getColumnIndex("settings_date")));
                int settingsId = cursor.getInt(cursor.getColumnIndex("settings_id"));
                int settingsAge = cursor.getInt(cursor.getColumnIndex("settings_age"));
                int settingsWeight = cursor.getInt(cursor.getColumnIndex("settings_weight"));
                Gender settingsGender = Gender.toEnum(cursor.getString(cursor.getColumnIndex("settings_gender")));

                Settings settings = new Settings(settingsGender, settingsWeight, settingsAge, settingsDate, settingsId);
                Pulse pulse = new Pulse(pulseId, pulseDate, pulsePulse, pulseDescription, settings);

                pulses.add(pulse);
            }

            result.setOk(true);
            result.setValue(pulses);

            return result;

        } catch (Exception ex) {
            Log.e(this.getClass().getName(), _context.getString(R.string.database_message_log_error_get_pulses));

            result.setOk(false);
            result.setMessage(_context.getString(R.string.database_message_ui_error_get_pulses));
            return result;
        }
    }

    public Result savePulse(Pulse pulse) {
        SQLiteDatabase db = getReadableDatabase();
        Result result = new Result();
        String sql;
        try {
            if (pulse.getId() == -1) {
                sql = "INSERT INTO pulses (pulse, date, description, settings_id) VALUES (?,?,?,?)";

                SQLiteStatement statement = db.compileStatement(sql);
                statement.bindLong(1, pulse.pulse);
                statement.bindString(2, DateFormatter.toDb(pulse.date));
                statement.bindString(3, pulse.description);
                statement.bindLong(4, pulse.settings.getId());
                statement.executeInsert();
            } else {
                sql = "INSERT INTO pulses (id, pulse, date, description, settings_id) VALUES (?,?,?,?,?)";

                SQLiteStatement statement = db.compileStatement(sql);
                statement.bindLong(1, pulse.getId());
                statement.bindLong(2, pulse.pulse);
                statement.bindString(3, DateFormatter.toDb(pulse.date));
                statement.bindString(4, pulse.description);
                statement.bindLong(5, pulse.settings.getId());
                statement.executeInsert();
            }

            result.setOk(true);
            result.setMessage(_context.getString(R.string.database_message_ui_saved));
            return result;

        } catch (Exception ex) {
            Log.e(this.getClass().getName(), _context.getString(R.string.database_message_log_error_save_pulse));

            result.setOk(false);
            result.setMessage(_context.getString(R.string.database_message_ui_error_save_pulse));
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
                result.setMessage(_context.getString(R.string.database_message_ui_error_no_settings_found));
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
            Log.e(this.getClass().getName(), _context.getString(R.string.database_message_log_error_get_settings));

            result.setOk(false);
            result.setMessage(_context.getString(R.string.database_message_ui_error_get_settings));
            return result;
        }
    }

    public Result deletePulse(Pulse pulse) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "DELETE FROM pulses WHERE id=?";

        Result result = new Result();

        try {
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindLong(1, pulse.getId());
            statement.executeUpdateDelete();

            result.setOk(true);
            result.setMessage(_context.getString(R.string.database_message_ui_deleted));
            return result;

        } catch (Exception ex) {
            Log.e(this.getClass().getName(), _context.getString(R.string.database_message_log_error_delete_pulse));

            result.setOk(false);
            result.setMessage(_context.getString(R.string.database_message_ui_error_delete_pulse));
            return result;
        }
    }
}
