package com.rutvik.locofit.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.rutvik.locofit.models.User;

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
    // Exercise table columns and names
    public static final String EXERCISE_TABLE = "exercises";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_DISTANCE = "distance";
    public static final String COLUMN_SPEED = "speed";
    public static final String COLUMN_CALORIES_BURNED = "calories_burned";
    public static final String COLUMN_MET = "met";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_ON_DATE = "on_date";
    public static final String COLUMN_ELEVATION_GAIN = "elevation_gain";
    public static final String COLUMN_BIKING_TYPE = "biking_type";
    public static final String COLUMN_TERRAIN = "terrain";
    public static final String COLUMN_ACCELERATION = "acceleration";
    public static final String COLUMN_SWIM_STYLE = "swim_style";
    public static final String COLUMN_STEP_COUNT = "step_count";

    public DBHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String queryCreateUserTable = "CREATE TABLE " + USER_TABLE + " (" + COLUMN_USERNAME + " TEXT PRIMARY KEY, " + COLUMN_PASSWORD + " TEXT, " + COLUMN_FIRST_NAME + " TEXT, " + COLUMN_LAST_NAME + " TEXT, " + COLUMN_HEIGHT + " INTEGER, " + COLUMN_WEIGHT + " INTEGER, " + COLUMN_BMI +  " REAL, " + COLUMN_GENDER + " TEXT, " + COLUMN_DOB + " DATE, " + COLUMN_EMAIL + " TEXT);";

        String queryCreateExcerciseTable = "CREATE TABLE " + EXERCISE_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USERNAME + " TEXT, " + COLUMN_PASSWORD + " TEXT, " + COLUMN_TYPE + " TEXT, " + COLUMN_DISTANCE + " REAL, " + COLUMN_SPEED + " REAL, " + COLUMN_CALORIES_BURNED + " REAL, " + COLUMN_MET + " REAL, " + COLUMN_DURATION + " TEXT, " + COLUMN_ON_DATE + " DATE, " + COLUMN_ELEVATION_GAIN + " REAL, " + COLUMN_BIKING_TYPE + " TEXT, " + COLUMN_TERRAIN + " TEXT, " + COLUMN_ACCELERATION + " REAL, " + COLUMN_SWIM_STYLE + " TEXT, " + COLUMN_STEP_COUNT + " INTEGER);";
        database.execSQL(queryCreateUserTable);
        database.execSQL(queryCreateExcerciseTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {
        String queryDropUserTable = "DROP TABLE IF EXISTS " + USER_TABLE + ";";
        database.execSQL(queryDropUserTable);
        onCreate(database);
    }

    public void closeDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
    public boolean addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, user.getUsername());
        cv.put(COLUMN_PASSWORD, user.getPassword());
        cv.put(COLUMN_FIRST_NAME, user.getFirstName());
        cv.put(COLUMN_LAST_NAME, user.getLastName());
        cv.put(COLUMN_HEIGHT, user.getHeight());
        cv.put(COLUMN_WEIGHT, user.getWeight());
        cv.put(COLUMN_BMI, user.getBMI());
        cv.put(COLUMN_GENDER, user.getGender());
        cv.put(COLUMN_DOB, user.getDateOfBirth());
        cv.put(COLUMN_EMAIL, user.getEmail());
        long result = db.insert(USER_TABLE, null, cv);
        return result != -1;
    }

    public boolean deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(USER_TABLE, COLUMN_USERNAME + " = ?", new String[]{user.getUsername()});
        return result > 0;
    }

    public User getUser(String givenUsername, String givenPassword){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_USERNAME, COLUMN_PASSWORD, COLUMN_FIRST_NAME, COLUMN_LAST_NAME, COLUMN_HEIGHT, COLUMN_WEIGHT, COLUMN_BMI, COLUMN_GENDER,COLUMN_DOB,COLUMN_EMAIL};
        String selection = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {givenUsername, givenPassword};
        Cursor cursor = db.query(USER_TABLE, projection, selection, selectionArgs, null, null, null);
        User user = null;
        if(cursor != null && cursor.moveToFirst()){
            String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME));
            int height = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HEIGHT));
            int weight = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT));
            double bmi = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_BMI));
            String gender = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENDER));
            String dob = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DOB));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
            user = new User(username, password, firstName, lastName, dob, gender, email, height, weight);
            cursor.close();
        }
            return user;
    }

    public boolean containsUser(String username, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = { COLUMN_USERNAME, COLUMN_PASSWORD };
        String selection = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = { username, password };
        Cursor cursor = db.query(USER_TABLE, projection, selection, selectionArgs, null, null, null);
        boolean userExists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) {
            cursor.close();
        }
        return userExists;
    }
}
