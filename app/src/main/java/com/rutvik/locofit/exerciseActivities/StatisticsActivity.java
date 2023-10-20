package com.rutvik.locofit.exerciseActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rutvik.locofit.R;
import com.rutvik.locofit.models.Biking;
import com.rutvik.locofit.models.Hiking;
import com.rutvik.locofit.models.Running;
import com.rutvik.locofit.models.Sprinting;
import com.rutvik.locofit.models.Swimming;
import com.rutvik.locofit.models.User;
import com.rutvik.locofit.models.Walking;
import com.rutvik.locofit.util.DBHandler;

import java.text.ParseException;
import java.util.Date;

public class StatisticsActivity extends Activity {
    private TextView statisticsExerciseLabel, nameView, dateView;
    private TextView attributeLabel1, attributeLabel2, attributeLabel3,attributeLabel4,attributeLabel5, attributeLabel6, attributeLabel7;
    private TextView attribute1, attribute2, attribute3,attribute4,attribute5, attribute6, attribute7;

    private ImageView statisticsProfilepic, exerciseImg;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    User user;

    DBHandler dbHandler = new DBHandler(StatisticsActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        statisticsExerciseLabel = findViewById(R.id.statisticsExerciseLabel);
        nameView = findViewById(R.id.nameView);
        dateView = findViewById(R.id.dateView);
        statisticsProfilepic = findViewById(R.id.statisticsProfilepic);
        exerciseImg = findViewById(R.id.exerciseImg);
        attributeLabel1 = findViewById(R.id.attributeLabel1);
        attributeLabel2 = findViewById(R.id.attributeLabel2);
        attributeLabel3 = findViewById(R.id.attributeLabel3);
        attributeLabel4 = findViewById(R.id.attributeLabel4);
        attributeLabel5 = findViewById(R.id.attributeLabel5);
        attributeLabel6 = findViewById(R.id.attributeLabel6);
        attributeLabel7 = findViewById(R.id.attributeLabel7);
        attribute1 = findViewById(R.id.attribute1);
        attribute2 = findViewById(R.id.attribute2);
        attribute3 = findViewById(R.id.attribute3);
        attribute4 = findViewById(R.id.attribute4);
        attribute5 = findViewById(R.id.attribute5);
        attribute6 = findViewById(R.id.attribute6);
        attribute7 = findViewById(R.id.attribute7);
        attributeLabel5.setVisibility(View.GONE);
        attributeLabel6.setVisibility(View.GONE);
        attributeLabel7.setVisibility(View.GONE);
        attribute5.setVisibility(View.GONE);
        attribute6.setVisibility(View.GONE);
        attribute7.setVisibility(View.GONE);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");
        String type = intent.getStringExtra("type");
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");
        sharedPreferences = getSharedPreferences("com.rutvik.locofit.SHAREDPREFERENCES", Context.MODE_PRIVATE);
        user = dbHandler.getUser(username, password);

        String imagePath = sharedPreferences.getString("profileSrc", null);
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            statisticsProfilepic.setImageBitmap(bitmap);
        } else {
            statisticsProfilepic.setImageResource(R.drawable.logo_landscape_round_corner);
        }
//        Log.d("General" , walking.getOnDate() + " " + walking.getDistance());
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        SimpleDateFormat inputTimeFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat outputTimeFormat = new SimpleDateFormat("hh:mm a");

        try {
            Date inputDate = inputDateFormat.parse(date);
            String outputDateString = outputDateFormat.format(inputDate);
            Date inputTime = inputTimeFormat.parse(time);
            String outputTime = outputTimeFormat.format(inputTime);
            dateView.setText(outputDateString + ", " + outputTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        nameView.setText(user.getFirstName() + " " + user.getLastName());

        switch (type) {
            case "walking":
                Walking walking = dbHandler.getWalking(user, date, time);
                statisticsExerciseLabel.setText(walking.getExerciseType().toUpperCase() + " Statistics");
                exerciseImg.setImageResource(walking.getImgResource());
                attributeLabel1.setText("Distance");
                attribute1.setText(String.format("%.2f km", (walking.getDistance() * 0.001)));
                attributeLabel2.setText("Duration");
                attribute2.setText(walking.getDuration());
                attributeLabel3.setText("Calories Burned");
                attribute3.setText(String.format("%.2f kcal", (walking.getCaloriesBurned() * 0.001)));
                attributeLabel4.setText("Speed");
                attribute4.setText(String.format("%.2f km/hr", (walking.getSpeed() * 3.6)));
                attributeLabel5.setText("Steps Count");
                attribute5.setText(String.valueOf(walking.getStepCount()));
                attributeLabel5.setVisibility(View.VISIBLE);
                attribute5.setVisibility(View.VISIBLE);
                break;
            case "hiking":
                Hiking hiking = dbHandler.getHiking(user, date, time);
                statisticsExerciseLabel.setText(hiking.getExerciseType().toUpperCase() + " Statistics");
                exerciseImg.setImageResource(hiking.getImgResource());
                attributeLabel1.setText("Distance");
                attribute1.setText(String.format("%.2f km", (hiking.getDistance() * 0.001)));
                attributeLabel2.setText("Duration");
                attribute2.setText(hiking.getDuration());
                attributeLabel3.setText("Calories Burned");
                attribute3.setText(String.format("%.2f kcal", (hiking.getCaloriesBurned() * 0.001)));
                attributeLabel4.setText("Speed");
                attribute4.setText(String.format("%.2f km/hr", (hiking.getSpeed() * 3.6)));
                attributeLabel6.setText("Elevation Gain");
                attribute6.setText(String.format("%.2f m", hiking.getElevationGain()));
                attributeLabel7.setText("Terrain");
                attribute7.setText(hiking.getTerrainDifficultyRating());
                attributeLabel6.setVisibility(View.VISIBLE);
                attribute6.setVisibility(View.VISIBLE);
                attributeLabel7.setVisibility(View.VISIBLE);
                attribute7.setVisibility(View.VISIBLE);
                break;
            case "biking":
                Biking biking = dbHandler.getBiking(user, date, time);
                statisticsExerciseLabel.setText(biking.getExerciseType().toUpperCase() + " Statistics");
                exerciseImg.setImageResource(biking.getImgResource());
                attributeLabel1.setText("Distance");
                attribute1.setText(String.format("%.2f km", (biking.getDistance() * 0.001)));
                attributeLabel2.setText("Duration");
                attribute2.setText(biking.getDuration());
                attributeLabel3.setText("Calories Burned");
                attribute3.setText(String.format("%.2f kcal", (biking.getCaloriesBurned() * 0.001)));
                attributeLabel4.setText("Speed");
                attribute4.setText(String.format("%.2f km/hr", (biking.getSpeed() * 3.6)));
                attributeLabel6.setText("Elevation Gain");
                attribute6.setText(String.format("%.2f m", biking.getElevationGain()));
                attributeLabel7.setText("Biking Type");
                attribute7.setText(biking.getType());
                attributeLabel6.setVisibility(View.VISIBLE);
                attribute6.setVisibility(View.VISIBLE);
                attributeLabel7.setVisibility(View.VISIBLE);
                attribute7.setVisibility(View.VISIBLE);
                break;
            case "running":
                Running running = dbHandler.getRunning(user, date, time);
                statisticsExerciseLabel.setText(running.getExerciseType().toUpperCase() + " Statistics");
                exerciseImg.setImageResource(running.getImgResource());
                attributeLabel1.setText("Distance");
                attribute1.setText(String.format("%.2f km", (running.getDistance() * 0.001)));
                attributeLabel2.setText("Duration");
                attribute2.setText(running.getDuration());
                attributeLabel3.setText("Calories Burned");
                attribute3.setText(String.format("%.2f kcal", (running.getCaloriesBurned() * 0.001)));
                attributeLabel4.setText("Speed");
                attribute4.setText(String.format("%.2f km/hr", (running.getSpeed() * 3.6)));
                break;
            case "sprinting":
                Sprinting sprinting = dbHandler.getSprinting(user, date, time);
                statisticsExerciseLabel.setText(sprinting.getExerciseType().toUpperCase() + " Statistics");
                exerciseImg.setImageResource(R.drawable.sprinting);
                attributeLabel1.setText("Distance");
                attribute1.setText(String.format("%.2f km", (sprinting.getDistance() * 0.001)));
                attributeLabel2.setText("Duration");
                attribute2.setText(sprinting.getDuration());
                attributeLabel3.setText("Calories Burned");
                attribute3.setText(String.format("%.2f kcal", (sprinting.getCaloriesBurned() * 0.001)));
                attributeLabel4.setText("Speed");
                attribute4.setText(String.format("%.2f km/hr", (sprinting.getSpeed() * 3.6)));
                attributeLabel5.setText("Acceleration");
                attribute5.setText(String.format("%.2f m/s", sprinting.getAcceleration()));
                attributeLabel5.setVisibility(View.VISIBLE);
                attribute5.setVisibility(View.VISIBLE);
                break;
            case "swimming":
                Swimming swimming = dbHandler.getSwimming(user, date, time);
                statisticsExerciseLabel.setText(swimming.getExerciseType().toUpperCase() + " Statistics");
                exerciseImg.setImageResource(R.drawable.swimming);
                attributeLabel1.setText("Distance");
                attribute1.setText(String.format("%.2f km", (swimming.getDistance() * 0.001)));
                attributeLabel2.setText("Duration");
                attribute2.setText(swimming.getDuration());
                attributeLabel3.setText("Calories Burned");
                attribute3.setText(String.format("%.2f kcal", (swimming.getCaloriesBurned() * 0.001)));
                attributeLabel4.setText("Swimming Style");
                attribute4.setText(swimming.getStyle());
            default:
                break;
        }
//        int year = Integer.parseInt(walking.getOnDate().substring(0, 4));
//        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
//        int month = Integer.parseInt(walking.getOnDate().substring(6,8));
//        int day = Integer.parseInt(walking.getOnDate().substring(9));
//        int hour = Integer.parseInt(walking.getOnTime().substring(0,2));
//        int minute = Integer.parseInt(walking.getOnTime().substring(3,5));
//        dateView.setText(day + " " + months[month-1] + " " + year + "   " + (hour>12 ? (hour-12 + ":" + minute) : (hour + ":" + minute)));
//        switch (exercise.getExerciseType()) {
//            case "walking":
//                statisticsExerciseLabel.setText(exercise.getExerciseType().toUpperCase() + " Statistics");
//                exerciseImg.setImageResource(R.drawable.walking);
//                break;
//            case "running":
//                statisticsExerciseLabel.setText(exercise.getExerciseType().toUpperCase() + " Statistics");
//                exerciseImg.setImageResource(R.drawable.running);
//                break;
//            case "sprinting":
//                statisticsExerciseLabel.setText(exercise.getExerciseType().toUpperCase() + " Statistics");
//                exerciseImg.setImageResource(R.drawable.sprinting);
//                break;
//            case "swimming":
//                statisticsExerciseLabel.setText(exercise.getExerciseType().toUpperCase() + " Statistics");
//                exerciseImg.setImageResource(R.drawable.swimming);
//                break;
//            case "biking":
//                statisticsExerciseLabel.setText(exercise.getExerciseType().toUpperCase() + " Statistics");
//                exerciseImg.setImageResource(R.drawable.biking);
//                break;
//            case "hiking":
//                statisticsExerciseLabel.setText(exercise.getExerciseType().toUpperCase() + " Statistics");
//                exerciseImg.setImageResource(R.drawable.hiking);
//                break;
//            default:
//                break;
//        }
    }
}