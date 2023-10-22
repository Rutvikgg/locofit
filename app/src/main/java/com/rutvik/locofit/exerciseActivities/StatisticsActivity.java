package com.rutvik.locofit.exerciseActivities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rutvik.locofit.R;
import com.rutvik.locofit.models.Biking;
import com.rutvik.locofit.models.Hiking;
import com.rutvik.locofit.models.Running;
import com.rutvik.locofit.models.Sprinting;
import com.rutvik.locofit.models.Swimming;
import com.rutvik.locofit.models.User;
import com.rutvik.locofit.models.Walking;
import com.rutvik.locofit.util.DBHandler;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class StatisticsActivity extends Activity implements OnMapReadyCallback {
    private TextView statisticsExerciseLabel, nameView, dateView;
    private TextView attributeLabel1, attributeLabel2, attributeLabel3,attributeLabel4,attributeLabel5, attributeLabel6, attributeLabel7;
    private TextView attribute1, attribute2, attribute3,attribute4,attribute5, attribute6, attribute7;

    private ImageView statisticsProfilepic, exerciseImg;
    private MapView statisticsMapView;
    private GoogleMap myMap;
    private List<LatLng> pathPoints;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    User user;

    DBHandler dbHandler = new DBHandler(StatisticsActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        statisticsMapView = findViewById(R.id.statisticsMapView);
        statisticsMapView.onCreate(savedInstanceState);
        statisticsMapView.getMapAsync(this);
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
            statisticsProfilepic.setImageResource(R.drawable.defaultprofilepic);
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
        Gson gson = new Gson();
        Type listType = new TypeToken<List<LatLng>>() {}.getType();

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
                pathPoints = gson.fromJson(walking.getLocation(), listType);
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
                pathPoints = gson.fromJson(hiking.getLocation(), listType);
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
                pathPoints = gson.fromJson(biking.getLocation(), listType);
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
                pathPoints = gson.fromJson(running.getLocation(), listType);
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
                pathPoints = gson.fromJson(sprinting.getLocation(), listType);
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
                pathPoints = gson.fromJson(swimming.getLocation(), listType);
            default:
                break;
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        myMap.getUiSettings().setScrollGesturesEnabled(true);
        if (!pathPoints.isEmpty()) {
            PolylineOptions polylineOptions = new PolylineOptions()
                    .addAll(pathPoints)
                    .width(17)
                    .color(ContextCompat.getColor(this, R.color.colorPolyline));

            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pathPoints.get(0), 15));
            Polyline polyline = myMap.addPolyline(polylineOptions);
        }else {
            LatLng placehoderLocation = new LatLng(55.702888, 13.194710);
            myMap.addMarker(new MarkerOptions().position(placehoderLocation).title("No location available")).showInfoWindow();
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placehoderLocation, 15));
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        statisticsMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        statisticsMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        statisticsMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        statisticsMapView.onLowMemory();
    }
}