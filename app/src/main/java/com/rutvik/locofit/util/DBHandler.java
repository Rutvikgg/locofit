package com.rutvik.locofit.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.rutvik.locofit.models.Biking;
import com.rutvik.locofit.models.Exercise;
import com.rutvik.locofit.models.Hiking;
import com.rutvik.locofit.models.Running;
import com.rutvik.locofit.models.Sprinting;
import com.rutvik.locofit.models.Swimming;
import com.rutvik.locofit.models.User;
import com.rutvik.locofit.models.Walking;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    // Database details
    private static final String DATABASE_NAME = "locofit.db";
    private static final int DATABASE_VERSION = 5;
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
    public static final String COLUMN_PROFILE_PIC = "profile_pic_src";
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
    public static final String COLUMN_ON_TIME = "on_time";
    public static final String COLUMN_LOCATION = "location";
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
        String queryCreateUserTable = "CREATE TABLE " + USER_TABLE + " (" + COLUMN_USERNAME + " TEXT PRIMARY KEY, " + COLUMN_PASSWORD + " TEXT, " + COLUMN_FIRST_NAME + " TEXT, " + COLUMN_LAST_NAME + " TEXT, " + COLUMN_HEIGHT + " INTEGER, " + COLUMN_WEIGHT + " INTEGER, " + COLUMN_BMI +  " REAL, " + COLUMN_GENDER + " TEXT, " + COLUMN_DOB + " DATE, " + COLUMN_EMAIL + " TEXT, " + COLUMN_PROFILE_PIC +" TEXT);";

        String queryCreateExcerciseTable = "CREATE TABLE " + EXERCISE_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USERNAME + " TEXT, " + COLUMN_PASSWORD + " TEXT, " + COLUMN_TYPE + " TEXT, " + COLUMN_DISTANCE + " REAL, " + COLUMN_SPEED + " REAL, " + COLUMN_CALORIES_BURNED + " REAL, " + COLUMN_MET + " REAL, " + COLUMN_DURATION + " TEXT, " + COLUMN_ON_DATE + " TEXT, " + COLUMN_ELEVATION_GAIN + " REAL, " + COLUMN_BIKING_TYPE + " TEXT, " + COLUMN_TERRAIN + " TEXT, " + COLUMN_ACCELERATION + " REAL, " + COLUMN_SWIM_STYLE + " TEXT, " + COLUMN_STEP_COUNT + " INTEGER, " + COLUMN_LOCATION + " TEXT, " + COLUMN_ON_TIME + " TEXT);";
        database.execSQL(queryCreateUserTable);
        database.execSQL(queryCreateExcerciseTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {
        String queryDropUserTable = "DROP TABLE IF EXISTS " + USER_TABLE + ";";
        String queryDropExerciseTable = "DROP TABLE IF EXISTS " + EXERCISE_TABLE + ";";
        database.execSQL(queryDropUserTable);
        database.execSQL(queryDropExerciseTable);
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
//    public void addOrUpdateSrc(User user) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put(COLUMN_PROFILE_PIC, user.getProfilePicSrc());
//        long rowId = db.insertWithOnConflict(USER_TABLE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
//    }
//
//    public String getUserProfilePic(User user) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        String[] projection = {COLUMN_PROFILE_PIC};
//        String selection = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
//        String[] selectionArgs = {user.getUsername(), user.getPassword()};
//        Cursor cursor = db.query(USER_TABLE, projection, selection, selectionArgs, null, null, null);
//        String profilePicSrc = null;
//        if (cursor != null && cursor.moveToFirst()) {
//            profilePicSrc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROFILE_PIC));
//            cursor.close();
//        }
//        return profilePicSrc;
//    }

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
        cv.put(COLUMN_LOCATION, biking.getLocation());
        cv.put(COLUMN_ON_TIME, biking.getOnTime());
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
        cv.put(COLUMN_LOCATION, hiking.getLocation());
        cv.put(COLUMN_ON_TIME, hiking.getOnTime());

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
        cv.put(COLUMN_LOCATION, running.getLocation());
        cv.put(COLUMN_ON_TIME, running.getOnTime());

        long result = db.insert(EXERCISE_TABLE, null, cv);
        return result != -1;
    }

    public boolean addSprinting(User user, Sprinting sprinting) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, user.getUsername());
        cv.put(COLUMN_PASSWORD, user.getPassword());
        cv.put(COLUMN_TYPE, sprinting.getExerciseType());
        cv.put(COLUMN_DISTANCE, sprinting.getDistance());
        cv.put(COLUMN_DURATION, sprinting.getDuration());
        cv.put(COLUMN_SPEED, sprinting.getSpeed());
        cv.put(COLUMN_CALORIES_BURNED, sprinting.getCaloriesBurned());
        cv.put(COLUMN_MET, sprinting.getMET());
        cv.put(COLUMN_ON_DATE, sprinting.getOnDate());
        cv.put(COLUMN_ACCELERATION, sprinting.getAcceleration());
        cv.put(COLUMN_LOCATION, sprinting.getLocation());
        cv.put(COLUMN_ON_TIME, sprinting.getOnTime());

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
        cv.put(COLUMN_LOCATION, swimming.getLocation());
        cv.put(COLUMN_ON_TIME, swimming.getOnTime());

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
        cv.put(COLUMN_LOCATION, walking.getLocation());
        cv.put(COLUMN_ON_TIME, walking.getOnTime());

        long result = db.insert(EXERCISE_TABLE, null, cv);
        return result != -1;
    }

    public Biking getBiking(User user, String date, String time) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_DISTANCE, COLUMN_DURATION, COLUMN_ON_DATE, COLUMN_ELEVATION_GAIN, COLUMN_BIKING_TYPE, COLUMN_LOCATION, COLUMN_ON_DATE, COLUMN_ON_TIME};
        String selection = COLUMN_ON_DATE + " = ? AND " + COLUMN_ON_TIME + " = ?";
        String[] selectionArgs = {date, time};
        Cursor cursor = db.query(EXERCISE_TABLE, projection, selection, selectionArgs, null, null, null);
        Biking biking = null;
        if (cursor != null && cursor.moveToFirst()) {
            double distance = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE));
            String duration = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION));
            String ondate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE));
            String ontime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_TIME));
            String location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION));
            double elevationGain = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ELEVATION_GAIN));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIKING_TYPE));
            biking = new Biking(distance, duration, ondate, user.getWeight(), location, ontime, elevationGain, type);
            cursor.close();
        }
        return biking;
    }

    public Hiking getHiking(User user, String date, String time) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_DISTANCE, COLUMN_DURATION, COLUMN_CALORIES_BURNED, COLUMN_MET, COLUMN_ON_DATE, COLUMN_ELEVATION_GAIN, COLUMN_TERRAIN, COLUMN_LOCATION, COLUMN_ON_TIME};
        String selection = COLUMN_ON_DATE + " = ? AND " + COLUMN_ON_TIME + " = ?";
        String[] selectionArgs = { date, time};
        Cursor cursor = db.query(EXERCISE_TABLE, projection, selection, selectionArgs, null, null, null);
        Hiking hiking = null;
        if(cursor != null && cursor.moveToFirst()) {
            double distance =  cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE));
            String duration = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION));
            String ondate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE));
            double elevationGain = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ELEVATION_GAIN));
           String terrain = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TERRAIN));
            String location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION));
           String ontime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_TIME));
            hiking = new Hiking(distance, duration, ondate, user.getWeight(), location, ontime, terrain, elevationGain);
            cursor.close();
        }
        return hiking;
    }

    public Running getRunning(User user, String date, String time) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_DISTANCE, COLUMN_DURATION, COLUMN_SPEED,  COLUMN_CALORIES_BURNED, COLUMN_MET, COLUMN_ON_DATE, COLUMN_LOCATION, COLUMN_ON_TIME};
        String selection = COLUMN_ON_DATE + " = ? AND " + COLUMN_ON_TIME + " = ?";
        String[] selectionArgs = {date, time};
        Cursor cursor = db.query(EXERCISE_TABLE, projection, selection, selectionArgs, null, null, null);
        Running running = null;
        if(cursor != null && cursor.moveToFirst()) {
            double distance =  cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE));
            String duration = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION));
            String ondate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE));
            String location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION));
            String ontime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_TIME));
            running = new Running(distance, duration, ondate, user.getWeight(), location, ontime);
            cursor.close();
        }
        return running;
    }

    public Sprinting getSprinting(User user, String date, String time) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_DISTANCE, COLUMN_DURATION, COLUMN_SPEED,  COLUMN_CALORIES_BURNED, COLUMN_MET, COLUMN_ON_DATE, COLUMN_ACCELERATION, COLUMN_LOCATION, COLUMN_ON_TIME};
        String selection = COLUMN_ON_DATE + " = ? AND " + COLUMN_ON_TIME + " = ?";
        String[] selectionArgs = {date, time};
        Cursor cursor = db.query(EXERCISE_TABLE, projection, selection, selectionArgs, null, null, null);
        Sprinting sprinting = null;
        if(cursor != null && cursor.moveToFirst()) {
            double distance =  cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE));
            String duration = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION));
            String ondate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE));
            String location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION));
            String ontime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_TIME));
            sprinting = new Sprinting(distance, duration, ondate, user.getWeight(), location, ontime);
            cursor.close();
        }
        return sprinting;
    }

    public Swimming getSwimming(User user,String date, String time) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_DISTANCE, COLUMN_DURATION, COLUMN_SPEED,  COLUMN_CALORIES_BURNED, COLUMN_MET, COLUMN_ON_DATE, COLUMN_SWIM_STYLE, COLUMN_LOCATION, COLUMN_ON_TIME};
        String selection = COLUMN_ON_DATE + " = ? AND " + COLUMN_ON_TIME + " = ?";
        String[] selectionArgs = {date, time};
        Cursor cursor = db.query(EXERCISE_TABLE, projection, selection, selectionArgs, null, null, null);
        Swimming swimming = null;
        if(cursor != null && cursor.moveToFirst()) {
            double distance =  cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE));
            String duration = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION));
            String ondate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE));
            String location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION));
            String ontime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_TIME));
            String style = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SWIM_STYLE));
            swimming = new Swimming(distance, duration, ondate, user.getWeight(), location, ontime, style);
            cursor.close();
        }
        return swimming;
    }

    public Walking getWalking(User user, String date, String time) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_DISTANCE, COLUMN_DURATION, COLUMN_SPEED,  COLUMN_CALORIES_BURNED, COLUMN_MET, COLUMN_ON_DATE, COLUMN_STEP_COUNT, COLUMN_LOCATION, COLUMN_ON_TIME};
        String selection = COLUMN_ON_DATE + " = ? AND " + COLUMN_ON_TIME + " = ?";
        String[] selectionArgs = { date, time};
        Cursor cursor = db.query(EXERCISE_TABLE, projection, selection, selectionArgs, null, null, null);
        Walking walking = null;
        if(cursor != null && cursor.moveToFirst()) {
            double distance = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE));
            String duration = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION));
            String ondate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE));
            String ontime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_TIME));
            String location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION));
            walking = new Walking(distance, duration, ondate, user.getWeight(), location, ontime);
            cursor.close();
        }
        return walking;
    }

    public ArrayList<Biking> getAllBiking(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_ID, COLUMN_DISTANCE, COLUMN_DURATION, COLUMN_SPEED, COLUMN_CALORIES_BURNED, COLUMN_MET, COLUMN_ON_DATE, COLUMN_ELEVATION_GAIN, COLUMN_BIKING_TYPE, COLUMN_LOCATION, COLUMN_ON_TIME};
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
            biking.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)));
            biking.setOnTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_TIME)));
            bikingArrayList.add(biking);
        }
        cursor.close();
        return bikingArrayList;
    }

    public ArrayList<Hiking> getAllHiking(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_ID, COLUMN_DISTANCE, COLUMN_DURATION, COLUMN_CALORIES_BURNED, COLUMN_MET, COLUMN_ON_DATE, COLUMN_ELEVATION_GAIN, COLUMN_TERRAIN, COLUMN_LOCATION, COLUMN_ON_TIME};
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
            hiking.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)));
            hiking.setOnTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_TIME)));
            hikingArrayList.add(hiking);
        }
        cursor.close();
        return hikingArrayList;
    }

    public ArrayList<Running> getAllRunning(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_ID, COLUMN_DISTANCE, COLUMN_DURATION, COLUMN_SPEED,  COLUMN_CALORIES_BURNED, COLUMN_MET, COLUMN_ON_DATE, COLUMN_LOCATION, COLUMN_ON_TIME};
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
            running.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)));
            running.setOnTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_TIME)));
            runningArrayList.add(running);
        }
        cursor.close();
        return runningArrayList;
    }

    public ArrayList<Sprinting> getAllSprinting(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_ID, COLUMN_DISTANCE, COLUMN_DURATION, COLUMN_SPEED,  COLUMN_CALORIES_BURNED, COLUMN_MET, COLUMN_ON_DATE, COLUMN_ACCELERATION, COLUMN_LOCATION,COLUMN_ON_TIME};
        String selection = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ? AND " + COLUMN_TYPE + " = ?";
        String[] selectionArgs = { user.getUsername(), user.getPassword(), "sprinting"};
        String sortOrder = COLUMN_ON_DATE + " DESC";
        Cursor cursor = db.query(EXERCISE_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        ArrayList<Sprinting> sprintingArrayList = new ArrayList<Sprinting>();
        while (cursor.moveToNext()) {
            Sprinting sprinting = new Sprinting();
            sprinting.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            sprinting.setDistance(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE)));
            sprinting.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION)));
            sprinting.setSpeed(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SPEED)));
            sprinting.setCaloriesBurned(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED)));
            sprinting.setMET(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MET)));
            sprinting.setOnDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE)));
            sprinting.setAcceleration(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ACCELERATION)));
            sprinting.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)));
            sprinting.setOnTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_TIME)));
            sprintingArrayList.add(sprinting);
        }
        cursor.close();
        return sprintingArrayList;
    }

    public ArrayList<Swimming> getAllSwimming(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_ID, COLUMN_DISTANCE, COLUMN_DURATION, COLUMN_SPEED,  COLUMN_CALORIES_BURNED, COLUMN_MET, COLUMN_ON_DATE, COLUMN_SWIM_STYLE, COLUMN_LOCATION,COLUMN_ON_TIME};
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
            swimming.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)));
            swimming.setOnTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_TIME)));
            swimmingArrayList.add(swimming);
        }
        cursor.close();
        return swimmingArrayList;
    }

    public ArrayList<Walking> getAllWalking(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_ID, COLUMN_DISTANCE, COLUMN_DURATION, COLUMN_SPEED,  COLUMN_CALORIES_BURNED, COLUMN_MET, COLUMN_ON_DATE, COLUMN_STEP_COUNT, COLUMN_LOCATION,COLUMN_ON_TIME};
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
            walking.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)));
            walking.setOnTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_TIME)));
            walkingArrayList.add(walking);
        }
        cursor.close();
        return walkingArrayList;
    }

    public ArrayList<Exercise> getAllExercise(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_ID, COLUMN_TYPE, COLUMN_DISTANCE, COLUMN_DURATION, COLUMN_SPEED, COLUMN_CALORIES_BURNED, COLUMN_MET, COLUMN_ON_DATE, COLUMN_ELEVATION_GAIN, COLUMN_STEP_COUNT, COLUMN_SWIM_STYLE, COLUMN_ACCELERATION, COLUMN_TERRAIN, COLUMN_BIKING_TYPE, COLUMN_LOCATION,COLUMN_ON_TIME};
        String selection = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {user.getUsername(), user.getPassword()};
        String sortOrder = COLUMN_ON_DATE + " DESC, " + COLUMN_ON_TIME + " DESC";
        Cursor cursor = db.query(EXERCISE_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        ArrayList<Exercise> exerciseArrayList = new ArrayList<Exercise>();
        while (cursor.moveToNext()) {
            switch (cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE))) {
                case "biking":
                    double bdistance = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE));
                    String bduration = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION));
                    String bondate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE));
                    String bontime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_TIME));
                    String blocation = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION));
                    double belevationGain = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ELEVATION_GAIN));
                    String btype = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIKING_TYPE));
                    Biking biking = new Biking(bdistance, bduration, bondate, user.getWeight(), blocation, bontime, belevationGain, btype);
                    exerciseArrayList.add(biking);
                    break;
                case "hiking":
                    double hdistance =  cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE));
                    String hduration = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION));
                    String hondate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE));
                    double helevationGain = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ELEVATION_GAIN));
                    String hterrain = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TERRAIN));
                    String hlocation = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION));
                    String hontime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_TIME));
                    Hiking hiking = new Hiking(hdistance, hduration, hondate, user.getWeight(), hlocation, hontime, hterrain, helevationGain);
                    exerciseArrayList.add(hiking);
                    break;
                case "running":
                    double rdistance =  cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE));
                    String rduration = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION));
                    String rondate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE));
                    String rlocation = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION));
                    String rontime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_TIME));
                    Running running = new Running(rdistance, rduration, rondate, user.getWeight(), rlocation, rontime);
                    exerciseArrayList.add(running);
                    break;
                case "sprinting":
                    double spdistance =  cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE));
                    String spduration = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION));
                    String spondate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE));
                    String splocation = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION));
                    String spontime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_TIME));
                    Sprinting sprinting = new Sprinting(spdistance, spduration, spondate, user.getWeight(), splocation, spontime);
                    exerciseArrayList.add(sprinting);
                    break;
                case "swimming":
                    double sdistance =  cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE));
                    String sduration = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION));
                    String sondate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE));
                    String slocation = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION));
                    String sontime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_TIME));
                    String sstyle = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SWIM_STYLE));
                    Swimming swimming = new Swimming(sdistance, sduration, sondate, user.getWeight(), slocation, sontime, sstyle);
                    exerciseArrayList.add(swimming);
                    break;
                case "walking":
                    double wdistance = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE));
                    String wduration = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION));
                    String wondate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE));
                    String wontime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_TIME));
                    String wlocation = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION));
                    Walking walking = new Walking(wdistance, wduration, wondate, user.getWeight(), wlocation, wontime);
                    exerciseArrayList.add(walking);
                    break;
            }
        }
        cursor.close();
        return exerciseArrayList;
    }

    public Exercise getExercise(String type, String date, String time) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_ID, COLUMN_TYPE, COLUMN_DISTANCE, COLUMN_DURATION, COLUMN_SPEED, COLUMN_CALORIES_BURNED, COLUMN_MET, COLUMN_ON_DATE, COLUMN_ELEVATION_GAIN, COLUMN_STEP_COUNT, COLUMN_SWIM_STYLE, COLUMN_ACCELERATION, COLUMN_TERRAIN, COLUMN_BIKING_TYPE, COLUMN_LOCATION,COLUMN_ON_TIME};
        String selection = COLUMN_ON_DATE + " = ? AND " + COLUMN_ON_TIME + " = ?";
        String[] selectionArgs = {type, date, time};
        Cursor cursor = db.query(EXERCISE_TABLE, projection, selection, selectionArgs, null, null, null);
        Exercise exercise = null;
        if(cursor != null && cursor.moveToFirst()) {
            switch (cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE))) {
                case "biking":
                    exercise = new Biking();
                    exercise.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                    exercise.setDistance(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE)));
                    exercise.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION)));
                    exercise.setSpeed(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SPEED)));
                    exercise.setCaloriesBurned(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED)));
                    exercise.setMET(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MET)));
                    exercise.setOnDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE)));
                    ((Biking) exercise).setElevationGain(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ELEVATION_GAIN)));
                    ((Biking) exercise).setType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIKING_TYPE)));
                    exercise.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)));
                    exercise.setOnTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_TIME)));
                    break;
                case "hiking":
                    exercise = new Hiking();
                    exercise.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                    exercise.setDistance(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE)));
                    exercise.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION)));
                    exercise.setCaloriesBurned(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED)));
                    exercise.setMET(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MET)));
                    exercise.setOnDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE)));
                    ((Hiking) exercise).setElevationGain(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ELEVATION_GAIN)));
                    ((Hiking) exercise).setTerrainDifficultyRating(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TERRAIN)));
                    exercise.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)));
                    exercise.setOnTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_TIME)));
                    break;
                case "running":
                    exercise = new Running();
                    exercise.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                    exercise.setDistance(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE)));
                    exercise.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION)));
                    exercise.setSpeed(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SPEED)));
                    exercise.setCaloriesBurned(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED)));
                    exercise.setMET(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MET)));
                    exercise.setOnDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE)));
                    exercise.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)));
                    exercise.setOnTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_TIME)));
                    break;
                case "sprinting":
                    exercise = new Sprinting();
                    exercise.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                    exercise.setDistance(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE)));
                    exercise.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION)));
                    exercise.setSpeed(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SPEED)));
                    exercise.setCaloriesBurned(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED)));
                    exercise.setMET(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MET)));
                    exercise.setOnDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE)));
                    ((Sprinting) exercise).setAcceleration(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ACCELERATION)));
                    exercise.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)));
                    exercise.setOnTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_TIME)));
                    break;
                case "swimming":
                    exercise = new Swimming();
                    exercise.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                    exercise.setDistance(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE)));
                    exercise.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION)));
                    exercise.setSpeed(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SPEED)));
                    exercise.setCaloriesBurned(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED)));
                    exercise.setMET(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MET)));
                    exercise.setOnDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE)));
                    ((Swimming) exercise).setStyle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SWIM_STYLE)));
                    exercise.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)));
                    exercise.setOnTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_TIME)));

                    break;
                case "walking":
                    exercise = new Walking();
                    exercise.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                    exercise.setDistance(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISTANCE)));
                    exercise.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION)));
                    exercise.setSpeed(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SPEED)));
                    exercise.setCaloriesBurned(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED)));
                    exercise.setMET(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MET)));
                    exercise.setOnDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_DATE)));
                    ((Walking) exercise).setStepCount(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STEP_COUNT)));
                    exercise.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)));
                    exercise.setOnTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ON_TIME)));
                    break;
            }
        }
        assert cursor != null;
        cursor.close();
        return exercise;
    }

    public void deleteAllExcercise(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] whereArgs = {user.getUsername(), user.getPassword()};
        db.delete(EXERCISE_TABLE, whereClause, whereArgs);
    }
}
