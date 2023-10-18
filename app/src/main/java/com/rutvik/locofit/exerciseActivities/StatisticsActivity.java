package com.rutvik.locofit.exerciseActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.rutvik.locofit.BaseActivity;
import com.rutvik.locofit.MainActivity;
import com.rutvik.locofit.R;
import com.rutvik.locofit.models.Biking;
import com.rutvik.locofit.models.Exercise;
import com.rutvik.locofit.models.Hiking;
import com.rutvik.locofit.models.User;
import com.rutvik.locofit.models.Walking;
import com.rutvik.locofit.util.DBHandler;

import org.w3c.dom.Text;

public class StatisticsActivity extends Activity {
    private TextView statisticsExerciseLabel, nameView, dateView, tempTextView;
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
        tempTextView = findViewById(R.id.tempTextView);
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


        nameView.setText(user.getFirstName() + " " + user.getLastName());

        switch (type) {
            case "walking":
                Walking walking = dbHandler.getWalking(user, date, time);
                statisticsExerciseLabel.setText(walking.getExerciseType().toUpperCase() + " Statistics");
                exerciseImg.setImageResource(R.drawable.walking);
                tempTextView.setText(walking.toString());
                dateView.setText(walking.getOnDate() + " " + walking.getOnTime());
                break;
            case "hiking":
                Hiking hiking = dbHandler.getHiking(user, date, time);
                statisticsExerciseLabel.setText(hiking.getExerciseType().toUpperCase() + " Statistics");
                exerciseImg.setImageResource(R.drawable.hiking);
                tempTextView.setText(hiking.toString());
                dateView.setText(hiking.getOnDate() + " " + hiking.getOnTime());
                break;
            case "biking":
                Biking biking = dbHandler.getBiking(user, date, time);
                statisticsExerciseLabel.setText(biking.getExerciseType().toUpperCase() + " Statistics");
                exerciseImg.setImageResource(R.drawable.biking);
                tempTextView.setText(biking.toString());
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