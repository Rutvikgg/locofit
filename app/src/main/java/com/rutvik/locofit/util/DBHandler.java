package com.rutvik.locofit.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

import com.rutvik.locofit.models.Biking;
import com.rutvik.locofit.models.Exercise;
import com.rutvik.locofit.models.Hiking;
import com.rutvik.locofit.models.Running;
import com.rutvik.locofit.models.Sprint;
import com.rutvik.locofit.models.Swimming;
import com.rutvik.locofit.models.User;
import com.rutvik.locofit.models.Walking;

import java.util.ArrayList;

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

    public boolean addBiking(User user, Biking biking){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, user.getUsername());
        cv.put(COLUMN_PASSWORD, user.getPassword());
        cv.put(COLUMN_TYPE, biking.getExerciseType());
        cv.put(COLUMN_DISTANCE, biking.getDistance());
        cv.put(COLUMN_DURATION, biking.getDuration());
        cv.put(COLUMN_SPEED, biking.getSpeed());
        cv.put(COLUMN_CALORIES_BURNED, biking.getCaloriesBurned());
        cv.put(COLUMN_MET, biking.getMET());
        cv.put(COLUMN_ON_DATE, biking.getOnDate());
        cv.put(COLUMN_ELEVATION_GAIN, biking.getElevationGain());
        cv.put(COLUMN_BIKING_TYPE, biking.getType());
        long result = db.insert(EXERCISE_TABLE, null, cv);
        return result != -1;
    }

    public boolean addHiking(User user, Hiking hiking) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, user.getUsername());
        cv.put(COLUMN_PASSWORD, user.getPassword());
        cv.put(COLUMN_TYPE, hiking.getExerciseType());
        cv.put(COLUMN_DISTANCE, hiking.getDistance());
        cv.put(COLUMN_DURATION, hiking.getDuration());
        cv.put(COLUMN_CALORIES_BURNED, hiking.getCaloriesBurned());
        cv.put(COLUMN_MET, hiking.getMET());
        cv.put(COLUMN_ON_DATE, hiking.getOnDate());
        cv.put(COLUMN_ELEVATION_GAIN, hiking.getElevationGain());
        cv.put(COLUMN_TERRAIN, hiking.getTerrainDifficultyRating());
        long result = db.insert(EXERCISE_TABLE, null, cv);
        return result != -1;
    }

    public boolean addRunning(User user, Running running) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, user.getUsername());
        cv.put(COLUMN_PASSWORD, user.getPassword());
        cv.put(COLUMN_TYPE, running.getExerciseType());
        cv.put(COLUMN_DISTANCE, running.getDistance());
        cv.put(COLUMN_DURATION, running.getDuration());
        cv.put(COLUMN_SPEED, running.getSpeed());
        cv.put(COLUMN_CALORIES_BURNED, running.getCaloriesBurned());
        cv.put(COLUMN_MET, running.getMET());
        cv.put(COLUMN_ON_DATE, running.getOnDate());
        long result = db.insert(EXERCISE_TABLE, null, cv);
        return result != -1;
    }

    public boolean addSprint(User user, Sprint sprint) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, user.getUsername());
        cv.put(COLUMN_PASSWORD, user.getPassword());
        cv.put(COLUMN_TYPE, sprint.getExerciseType());
        cv.put(COLUMN_DISTANCE, sprint.getDistance());
        cv.put(COLUMN_DURATION, sprint.getDuration());
        cv.put(COLUMN_SPEED, sprint.getSpeed());
        cv.put(COLUMN_CALORIES_BURNED, sprint.getCaloriesBurned());
        cv.put(COLUMN_MET, sprint.getMET());
        cv.put(COLUMN_ON_DATE, sprint.getOnDate());
        cv.put(COLUMN_ACCELERATION, sprint.getAcceleration());
        long result = db.insert(EXERCISE_TABLE, null, cv);
        return result != -1;
    }

    public boolean addSwimming(User user, Swimming swimming) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, user.getUsername());
        cv.put(COLUMN_PASSWORD, user.getPassword());
        cv.put(COLUMN_TYPE, swimming.getExerciseType());
        cv.put(COLUMN_DISTANCE, swimming.getDistance());
        cv.put(COLUMN_DURATION, swimming.getDuration());
        cv.put(COLUMN_SPEED, swimming.getSpeed());
        cv.put(COLUMN_CALORIES_BURNED, swimming.getCaloriesBurned());
        cv.put(COLUMN_MET, swimming.getMET());
        cv.put(COLUMN_ON_DATE, swimming.getOnDate());
        cv.put(COLUMN_SWIM_STYLE, swimming.getStyle());
        long result = db.insert(EXERCISE_TABLE, null, cv);
        return result != -1;
    }

    public boolean addWalking(User user, Walking walking) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, user.getUsername());
        cv.put(COLUMN_PASSWORD, user.getPassword());
        cv.put(COLUMN_TYPE, walking.getExerciseType());
        cv.put(COLUMN_DISTANCE, walking.getDistance());
        cv.put(COLUMN_DURATION, walking.getDuration());
        cv.put(COLUMN_SPEED, walking.getSpeed());
        cv.put(COLUMN_CALORIES_BURNED, walking.getCaloriesBurned());
        cv.put(COLUMN_MET, walking.getMET());
        cv.put(COLUMN_ON_DATE, walking.getOnDate());
        cv.put(COLUMN_STEP_COUNT, walking.getStepCount());
        long result = db.insert(EXERCISE_TABLE, null, cv);
        return result != -1;
    }

    public Biking getBiking(User user, int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_DISTANCE, COLUMN_DURATION, COLUMN_SPEED, COLUMN_CALORIES_BURNED, COLUMN_MET, COLUMN_ON_DATE, COLUMN_ELEVATION_GAIN, COLUMN_BIKING_TYPE};
        String selection = COLUMN_ID + " = ? AND " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ? AND " + COLUMN_TYPE + " = ?";
        String[] selectionArgs = {String.valueOf(id), user.getUsername(), user.getPassword(), "biking"};
        Cursor cursor = db.query(EXERCISE_TABLE, projection, selection, selectionArgs, null, null, null);
        Biking biking = null;
        if(cursor != null && cursor.moveToFirst()){
            biking = new Biking();
            biking.setId(id);
            biking.setDistance(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE)));
            biking.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION)));
            biking.setSpeed(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SPEED)));
            biking.setCaloriesBurned(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED)));
            biking.setMET(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MET)));
            biking.setOnDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE)));
            biking.setElevationGain(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ELEVATION_GAIN)));
            biking.setType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIKING_TYPE)));
            cursor.close();
        }
        return biking;
    }

    public Hiking getHiking(User user, int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_DISTANCE, COLUMN_DURATION, COLUMN_CALORIES_BURNED, COLUMN_MET, COLUMN_ON_DATE, COLUMN_ELEVATION_GAIN, COLUMN_TERRAIN};
        String selection = COLUMN_ID + " = ? AND " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ? AND " + COLUMN_TYPE + " = ?";
        String[] selectionArgs = {String.valueOf(id), user.getUsername(), user.getPassword(), "hiking"};
        Cursor cursor = db.query(EXERCISE_TABLE, projection, selection, selectionArgs, null, null, null);
        Hiking hiking = null;
        if(cursor != null && cursor.moveToFirst()) {
            hiking = new Hiking();
            hiking.setId(id);
            hiking.setDistance(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE)));
            hiking.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION)));
            hiking.setCaloriesBurned(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED)));
            hiking.setMET(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MET)));
            hiking.setOnDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE)));
            hiking.setElevationGain(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ELEVATION_GAIN)));
            hiking.setTerrainDifficultyRating(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TERRAIN)));
            cursor.close();
        }
        return hiking;
    }

    public Running getRunning(User user, int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_DISTANCE, COLUMN_DURATION, COLUMN_SPEED,  COLUMN_CALORIES_BURNED, COLUMN_MET, COLUMN_ON_DATE};
        String selection = COLUMN_ID + " = ? AND " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ? AND " + COLUMN_TYPE + " = ?";
        String[] selectionArgs = {String.valueOf(id), user.getUsername(), user.getPassword(), "running"};
        Cursor cursor = db.query(EXERCISE_TABLE, projection, selection, selectionArgs, null, null, null);
        Running running = null;
        if(cursor != null && cursor.moveToFirst()) {
            running = new Running();
            running.setId(id);
            running.setDistance(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE)));
            running.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION)));
            running.setSpeed(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SPEED)));
            running.setCaloriesBurned(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED)));
            running.setMET(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MET)));
            running.setOnDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE)));
            cursor.close();
        }
        return running;
    }

    public Sprint getSprint(User user, int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_DISTANCE, COLUMN_DURATION, COLUMN_SPEED,  COLUMN_CALORIES_BURNED, COLUMN_MET, COLUMN_ON_DATE, COLUMN_ACCELERATION};
        String selection = COLUMN_ID + " = ? AND " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ? AND " + COLUMN_TYPE + " = ?";
        String[] selectionArgs = {String.valueOf(id), user.getUsername(), user.getPassword(), "sprint"};
        Cursor cursor = db.query(EXERCISE_TABLE, projection, selection, selectionArgs, null, null, null);
        Sprint sprint = null;
        if(cursor != null && cursor.moveToFirst()) {
            sprint = new Sprint();
            sprint.setId(id);
            sprint.setDistance(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE)));
            sprint.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION)));
            sprint.setSpeed(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SPEED)));
            sprint.setCaloriesBurned(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED)));
            sprint.setMET(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MET)));
            sprint.setOnDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE)));
            sprint.setAcceleration(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ACCELERATION)));
            cursor.close();
        }
        return sprint;
    }

    public Swimming getSwimming(User user, int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_DISTANCE, COLUMN_DURATION, COLUMN_SPEED,  COLUMN_CALORIES_BURNED, COLUMN_MET, COLUMN_ON_DATE, COLUMN_SWIM_STYLE};
        String selection = COLUMN_ID + " = ? AND " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ? AND " + COLUMN_TYPE + " = ?";
        String[] selectionArgs = {String.valueOf(id), user.getUsername(), user.getPassword(), "swimming"};
        Cursor cursor = db.query(EXERCISE_TABLE, projection, selection, selectionArgs, null, null, null);
        Swimming swimming = null;
        if(cursor != null && cursor.moveToFirst()) {
            swimming = new Swimming();
            swimming.setId(id);
            swimming.setDistance(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE)));
            swimming.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION)));
            swimming.setSpeed(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SPEED)));
            swimming.setCaloriesBurned(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED)));
            swimming.setMET(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MET)));
            swimming.setOnDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE)));
            swimming.setStyle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SWIM_STYLE)));
            cursor.close();
        }
        return swimming;
    }

    public Walking getWalking(User user, int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_DISTANCE, COLUMN_DURATION, COLUMN_SPEED,  COLUMN_CALORIES_BURNED, COLUMN_MET, COLUMN_ON_DATE, COLUMN_STEP_COUNT};
        String selection = COLUMN_ID + " = ? AND " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ? AND " + COLUMN_TYPE + " = ?";
        String[] selectionArgs = {String.valueOf(id), user.getUsername(), user.getPassword(), "walking"};
        Cursor cursor = db.query(EXERCISE_TABLE, projection, selection, selectionArgs, null, null, null);
        Walking walking = null;
        if(cursor != null && cursor.moveToFirst()) {
            walking = new Walking();
            walking.setId(id);
            walking.setDistance(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE)));
            walking.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION)));
            walking.setSpeed(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SPEED)));
            walking.setCaloriesBurned(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED)));
            walking.setMET(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MET)));
            walking.setOnDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE)));
            walking.setStepCount(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STEP_COUNT)));
            cursor.close();
        }
        return walking;
    }

    public ArrayList<Biking> getAllBiking(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_ID, COLUMN_DISTANCE, COLUMN_DURATION, COLUMN_SPEED, COLUMN_CALORIES_BURNED, COLUMN_MET, COLUMN_ON_DATE, COLUMN_ELEVATION_GAIN, COLUMN_BIKING_TYPE};
        String selection = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ? AND " + COLUMN_TYPE + " = ?";
        String[] selectionArgs = {user.getUsername(), user.getPassword(), "biking"};
        String sortOrder = COLUMN_ON_DATE + " DESC";
        Cursor cursor = db.query(EXERCISE_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        ArrayList<Biking> bikingArrayList = new ArrayList<Biking>();
        while (cursor.moveToNext()) {
            Biking biking = new Biking();
            biking.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            biking.setDistance(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE)));
            biking.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION)));
            biking.setSpeed(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SPEED)));
            biking.setCaloriesBurned(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED)));
            biking.setMET(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MET)));
            biking.setOnDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE)));
            biking.setElevationGain(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ELEVATION_GAIN)));
            biking.setType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIKING_TYPE)));
            bikingArrayList.add(biking);
        }
        cursor.close();
        return bikingArrayList;
    }

    public ArrayList<Hiking> getAllHiking(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_ID, COLUMN_DISTANCE, COLUMN_DURATION, COLUMN_CALORIES_BURNED, COLUMN_MET, COLUMN_ON_DATE, COLUMN_ELEVATION_GAIN, COLUMN_TERRAIN};
        String selection = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ? AND " + COLUMN_TYPE + " = ?";
        String[] selectionArgs = {user.getUsername(), user.getPassword(), "hiking"};
        String sortOrder = COLUMN_ON_DATE + " DESC";
        Cursor cursor = db.query(EXERCISE_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        ArrayList<Hiking> hikingArrayList = new ArrayList<Hiking>();
        while (cursor.moveToNext()) {
            Hiking hiking = new Hiking();
            hiking.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            hiking.setDistance(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE)));
            hiking.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION)));
            hiking.setCaloriesBurned(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED)));
            hiking.setMET(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MET)));
            hiking.setOnDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE)));
            hiking.setElevationGain(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ELEVATION_GAIN)));
            hiking.setTerrainDifficultyRating(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TERRAIN)));
            hikingArrayList.add(hiking);
        }
        cursor.close();
        return hikingArrayList;
    }

    public ArrayList<Running> getAllRunning(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_ID, COLUMN_DISTANCE, COLUMN_DURATION, COLUMN_SPEED,  COLUMN_CALORIES_BURNED, COLUMN_MET, COLUMN_ON_DATE};
        String selection = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ? AND " + COLUMN_TYPE + " = ?";
        String[] selectionArgs = { user.getUsername(), user.getPassword(), "running"};
        String sortOrder = COLUMN_ON_DATE + " DESC";
        Cursor cursor = db.query(EXERCISE_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        ArrayList<Running> runningArrayList = new ArrayList<Running>();
        while (cursor.moveToNext()){
            Running running = new Running();
            running.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            running.setDistance(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE)));
            running.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION)));
            running.setSpeed(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SPEED)));
            running.setCaloriesBurned(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED)));
            running.setMET(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MET)));
            running.setOnDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE)));
            runningArrayList.add(running);
        }
        cursor.close();
        return runningArrayList;
    }

    public ArrayList<Sprint> getAllSprint(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_ID, COLUMN_DISTANCE, COLUMN_DURATION, COLUMN_SPEED,  COLUMN_CALORIES_BURNED, COLUMN_MET, COLUMN_ON_DATE, COLUMN_ACCELERATION};
        String selection = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ? AND " + COLUMN_TYPE + " = ?";
        String[] selectionArgs = { user.getUsername(), user.getPassword(), "sprint"};
        String sortOrder = COLUMN_ON_DATE + " DESC";
        Cursor cursor = db.query(EXERCISE_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        ArrayList<Sprint> sprintArrayList = new ArrayList<Sprint>();
        while (cursor.moveToNext()) {
            Sprint sprint = new Sprint();
            sprint.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            sprint.setDistance(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE)));
            sprint.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION)));
            sprint.setSpeed(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SPEED)));
            sprint.setCaloriesBurned(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED)));
            sprint.setMET(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MET)));
            sprint.setOnDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE)));
            sprint.setAcceleration(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ACCELERATION)));
            sprintArrayList.add(sprint);
        }
        cursor.close();
        return sprintArrayList;
    }

    public ArrayList<Swimming> getAllSwimming(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_ID, COLUMN_DISTANCE, COLUMN_DURATION, COLUMN_SPEED,  COLUMN_CALORIES_BURNED, COLUMN_MET, COLUMN_ON_DATE, COLUMN_SWIM_STYLE};
        String selection = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ? AND " + COLUMN_TYPE + " = ?";
        String[] selectionArgs = {user.getUsername(), user.getPassword(), "swimming"};
        String sortOrder = COLUMN_ON_DATE + " DESC";
        Cursor cursor = db.query(EXERCISE_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        ArrayList<Swimming> swimmingArrayList = new ArrayList<Swimming>();
        while (cursor.moveToNext()){
            Swimming swimming = new Swimming();
            swimming.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            swimming.setDistance(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE)));
            swimming.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION)));
            swimming.setSpeed(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SPEED)));
            swimming.setCaloriesBurned(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED)));
            swimming.setMET(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MET)));
            swimming.setOnDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE)));
            swimming.setStyle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SWIM_STYLE)));
            swimmingArrayList.add(swimming);
        }
        cursor.close();
        return swimmingArrayList;
    }

    public ArrayList<Walking> getAllWalking(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_ID, COLUMN_DISTANCE, COLUMN_DURATION, COLUMN_SPEED,  COLUMN_CALORIES_BURNED, COLUMN_MET, COLUMN_ON_DATE, COLUMN_STEP_COUNT};
        String selection = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ? AND " + COLUMN_TYPE + " = ?";
        String[] selectionArgs = {user.getUsername(), user.getPassword(), "walking"};
        String sortOrder = COLUMN_ON_DATE + " DESC";
        Cursor cursor = db.query(EXERCISE_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        ArrayList<Walking> walkingArrayList = new ArrayList<Walking>();
        while (cursor.moveToNext()){
            Walking walking = new Walking();
            walking.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            walking.setDistance(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE)));
            walking.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION)));
            walking.setSpeed(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SPEED)));
            walking.setCaloriesBurned(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED)));
            walking.setMET(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MET)));
            walking.setOnDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE)));
            walking.setStepCount(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STEP_COUNT)));
            walkingArrayList.add(walking);
        }
        cursor.close();
        return walkingArrayList;
    }

    public ArrayList<Exercise> getAllExercise(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_ID, COLUMN_TYPE, COLUMN_DISTANCE, COLUMN_DURATION, COLUMN_SPEED, COLUMN_CALORIES_BURNED, COLUMN_MET, COLUMN_ON_DATE, COLUMN_ELEVATION_GAIN, COLUMN_STEP_COUNT, COLUMN_SWIM_STYLE, COLUMN_ACCELERATION, COLUMN_TERRAIN, COLUMN_BIKING_TYPE};
        String selection = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {user.getUsername(), user.getPassword()};
        String sortOrder = COLUMN_ON_DATE + " DESC";
        Cursor cursor = db.query(EXERCISE_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        ArrayList<Exercise> exerciseArrayList = new ArrayList<Exercise>();
        while (cursor.moveToNext()) {
            switch (cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE))) {
                case "biking":
                    Biking biking = new Biking();
                    biking.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                    biking.setDistance(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE)));
                    biking.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION)));
                    biking.setSpeed(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SPEED)));
                    biking.setCaloriesBurned(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED)));
                    biking.setMET(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MET)));
                    biking.setOnDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE)));
                    biking.setElevationGain(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ELEVATION_GAIN)));
                    biking.setType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIKING_TYPE)));
                    exerciseArrayList.add(biking);
                    break;
                case "hiking":
                    Hiking hiking = new Hiking();
                    hiking.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                    hiking.setDistance(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE)));
                    hiking.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION)));
                    hiking.setCaloriesBurned(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED)));
                    hiking.setMET(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MET)));
                    hiking.setOnDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE)));
                    hiking.setElevationGain(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ELEVATION_GAIN)));
                    hiking.setTerrainDifficultyRating(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TERRAIN)));
                    exerciseArrayList.add(hiking);
                    break;
                case "running":
                    Running running = new Running();
                    running.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                    running.setDistance(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE)));
                    running.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION)));
                    running.setSpeed(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SPEED)));
                    running.setCaloriesBurned(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED)));
                    running.setMET(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MET)));
                    running.setOnDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE)));
                    exerciseArrayList.add(running);
                    break;
                case "sprint":
                    Sprint sprint = new Sprint();
                    sprint.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                    sprint.setDistance(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE)));
                    sprint.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION)));
                    sprint.setSpeed(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SPEED)));
                    sprint.setCaloriesBurned(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED)));
                    sprint.setMET(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MET)));
                    sprint.setOnDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE)));
                    sprint.setAcceleration(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ACCELERATION)));
                    exerciseArrayList.add(sprint);
                    break;
                case "swimming":
                    Swimming swimming = new Swimming();
                    swimming.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                    swimming.setDistance(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE)));
                    swimming.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION)));
                    swimming.setSpeed(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SPEED)));
                    swimming.setCaloriesBurned(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED)));
                    swimming.setMET(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MET)));
                    swimming.setOnDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE)));
                    swimming.setStyle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SWIM_STYLE)));
                    exerciseArrayList.add(swimming);
                    break;
                case "walking":
                    Walking walking = new Walking();
                    walking.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                    walking.setDistance(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE)));
                    walking.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION)));
                    walking.setSpeed(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SPEED)));
                    walking.setCaloriesBurned(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED)));
                    walking.setMET(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MET)));
                    walking.setOnDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE)));
                    walking.setStepCount(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STEP_COUNT)));
                    exerciseArrayList.add(walking);
                    break;
            }
        }
        cursor.close();
        return exerciseArrayList;
    }
}
