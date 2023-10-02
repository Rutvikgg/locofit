package com.rutvik.locofit.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHandler extends SQLiteOpenHelper {
    // Database details
    private static final String DATABASE_NAME = "locofit.db";
    private static final int DATABASE_VERSION = 1;
    // User table columns and names
    public static final String USER_TABLE = "users";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_HEIGHT = "height";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_BMI = "body_mass_index";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_DOB = "date_of_birth";
    public static final String COLUMN_EMAIL = "email";

    public DBHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String queryCreateUserTable = "CREATE TABLE " + USER_TABLE + " (" + COLUMN_USERNAME + " TEXT PRIMARY KEY, " + COLUMN_PASSWORD + " TEXT, " + COLUMN_FIRST_NAME + " TEXT, " + COLUMN_LAST_NAME + " TEXT, " + COLUMN_HEIGHT + " INTEGER, " + COLUMN_WEIGHT + " INTEGER, " + COLUMN_BMI +  " REAL, " + COLUMN_GENDER + " TEXT, " + COLUMN_DOB + " DATE, " + COLUMN_EMAIL + " TEXT);";
        database.execSQL(queryCreateUserTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {
        String queryDropUserTable = "DROP TABLE IF EXISTS " + USER_TABLE + ";";
        database.execSQL(queryDropUserTable);
        onCreate(database);
    }
}
