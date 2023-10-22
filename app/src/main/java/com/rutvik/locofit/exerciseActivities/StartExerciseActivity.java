package com.rutvik.locofit.exerciseActivities;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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
import com.rutvik.locofit.R;
import com.rutvik.locofit.models.Biking;
import com.rutvik.locofit.models.Hiking;
import com.rutvik.locofit.models.Running;
import com.rutvik.locofit.models.Sprinting;
import com.rutvik.locofit.models.Swimming;
import com.rutvik.locofit.models.User;
import com.rutvik.locofit.models.Walking;
import com.rutvik.locofit.util.DBHandler;

import java.util.ArrayList;
import java.util.List;

public class StartExerciseActivity extends Activity implements OnMapReadyCallback{
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    public static final int DEFAULT_UPDATE_INTERVAL = 10;
    public static final int FASTEST_UPDATE_INTERVAL = 2;
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private TextView exerciseLabel, spinnerLabel;
    private Chronometer startExerciseTimerView;
    private boolean isRunning = false;
    private long pauseOffset;
    private Button startExerciseButton;
    private Spinner startExerciseSpinner;
    private MapView map;
    private GoogleMap myMap;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private double totalDistance = 0.0;
    private Location previousLocation;
    private List<LatLng> pathPoints;
    private double totalElevationGain = 0.0;
    private double previousElevation = 0.0;
    private FusedLocationProviderClient fusedLocationProviderClient;
    Calendar calendar = Calendar.getInstance();
    User user;
    DBHandler dbHandler = new DBHandler(StartExerciseActivity.this);
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_exercise);
        // Map Setup
        map = findViewById(R.id.map);
        map.onCreate(savedInstanceState);
        map.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        pathPoints = new ArrayList<>();
        locationRequest = new LocationRequest.Builder(1000 * DEFAULT_UPDATE_INTERVAL).setMinUpdateIntervalMillis(1000 * FASTEST_UPDATE_INTERVAL).build();
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        updateLocationOnMap(location);
                        calculateDistance(location);
                        calculateElevationGain(location);
                    }
                }
            }
        };

        sharedPreferences = getSharedPreferences("com.rutvik.locofit.SHAREDPREFERENCES", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String username = sharedPreferences.getString("username", null);
        String password = sharedPreferences.getString("password", null);
        exerciseLabel = findViewById(R.id.exerciseLabel);
        startExerciseTimerView = findViewById(R.id.startExerciseTimerView);
        startExerciseButton = findViewById(R.id.startExerciseButton);
        startExerciseSpinner = findViewById(R.id.startExerciseSpinner);
        spinnerLabel = findViewById(R.id.spinnerLabel);
        startExerciseTimerView.setVisibility(View.GONE);
        Intent intent = getIntent();
        String exercise = intent.getStringExtra("exercise");
        String[] swimmingStyles = {"Freestyle", "Breaststroke", "Backstroke", "Butterfly"};
        String[] hikingTerrain = {"Easy", "Moderate", "Moderately Strenuous", "Strenuous", "Very Strenuous"};
        String[] bikingTypes = {"General Biking", "Mountain Biking"};
        String[] selection = {"None"};
        exerciseLabel.setText(exercise.toUpperCase());


        startExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startExerciseButton.getText().toString().equals("Start")) {
                    startExerciseSpinner.setVisibility(View.GONE);
                    spinnerLabel.setVisibility(View.GONE);
                    startExerciseTimerView.setVisibility(View.VISIBLE);
                    startExerciseButton.setText("Stop");
                    startLocationUpdates();
                    startChronometer();
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                } else {
                    startExerciseButton.setText("Start");
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                    String duration = stopChronometer();
                    Gson gson = new Gson();
                    String locationListString = gson.toJson(pathPoints);
                    String [] currentDateTime = getDateTime();
                    user = dbHandler.getUser(username, password);
                    Intent statisticsIntent = new Intent(StartExerciseActivity.this, StatisticsActivity.class);
                    switch (exercise) {
                        case "walking":
                            Walking walking = new Walking(totalDistance, duration, currentDateTime[0], user.getWeight(), locationListString,currentDateTime[1]);
                            dbHandler.addWalking(user, walking);
                            statisticsIntent.putExtra("username", username);
                            statisticsIntent.putExtra("password", password);
                            statisticsIntent.putExtra("type", "walking");
                            statisticsIntent.putExtra("date", currentDateTime[0]);
                            statisticsIntent.putExtra("time", currentDateTime[1]);
                            break;
                        case "running":
                            Running running = new Running(totalDistance, duration, currentDateTime[0], user.getWeight(), locationListString, currentDateTime[1]);
                            if (dbHandler.addRunning(user, running))
                                Toast.makeText(StartExerciseActivity.this, "running added", Toast.LENGTH_SHORT).show();;
                            statisticsIntent.putExtra("username", username);
                            statisticsIntent.putExtra("password", password);
                            statisticsIntent.putExtra("type", "running");
                            statisticsIntent.putExtra("date", currentDateTime[0]);
                            statisticsIntent.putExtra("time", currentDateTime[1]);
                            break;
                        case "sprinting":
                            Sprinting sprinting = new Sprinting(totalDistance, duration, currentDateTime[0], user.getWeight(), locationListString, currentDateTime[1]);
                            if (dbHandler.addSprinting(user, sprinting))
                                Toast.makeText(StartExerciseActivity.this, "sprinting added", Toast.LENGTH_SHORT).show();;
                            statisticsIntent.putExtra("username", username);
                            statisticsIntent.putExtra("password", password);
                            statisticsIntent.putExtra("type", "sprinting");
                            statisticsIntent.putExtra("date", currentDateTime[0]);
                            statisticsIntent.putExtra("time", currentDateTime[1]);
                            break;
                        case "swimming":
                            Swimming swimming = new Swimming(totalDistance, duration, currentDateTime[0], user.getWeight(), locationListString, currentDateTime[1], selection[0]);
                            if(dbHandler.addSwimming(user, swimming))
                                Toast.makeText(StartExerciseActivity.this, "swimming added", Toast.LENGTH_SHORT).show();;
                            statisticsIntent.putExtra("username", username);
                            statisticsIntent.putExtra("password", password);
                            statisticsIntent.putExtra("type", "swimming");
                            statisticsIntent.putExtra("date", currentDateTime[0]);
                            statisticsIntent.putExtra("time", currentDateTime[1]);
                            break;
                        case "biking":
                            Biking biking = new Biking(totalDistance, duration, currentDateTime[0], user.getWeight(), locationListString, currentDateTime[1], totalElevationGain, selection[0]);
                            if (dbHandler.addBiking(user, biking))
                                Toast.makeText(StartExerciseActivity.this, "biking added succesfully", Toast.LENGTH_SHORT).show();
                            statisticsIntent.putExtra("username", username);
                            statisticsIntent.putExtra("password", password);
                            statisticsIntent.putExtra("type", "biking");
                            statisticsIntent.putExtra("date", currentDateTime[0]);
                            statisticsIntent.putExtra("time", currentDateTime[1]);

                            break;
                        case "hiking":
                            Hiking hiking = new Hiking(totalDistance, duration, currentDateTime[0], user.getWeight(), locationListString, currentDateTime[1], selection[0], totalElevationGain);
                            if (dbHandler.addHiking(user, hiking))
                                Toast.makeText(StartExerciseActivity.this, "added hiking", Toast.LENGTH_SHORT).show();;
                            statisticsIntent.putExtra("username", username);
                            statisticsIntent.putExtra("password", password);
                            statisticsIntent.putExtra("type", "hiking");
                            statisticsIntent.putExtra("date", currentDateTime[0]);
                            statisticsIntent.putExtra("time", currentDateTime[1]);
                            break;
                        default:
                            break;
                    }
                    totalDistance = 0.0;
                    totalElevationGain = 0.0;
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    startActivity(statisticsIntent);
                }


            }
        });

        switch (exercise) {
            case "swimming":
                spinnerLabel.setText("Choose a swimming style");
                ArrayAdapter<String> swimmingAdapter = new ArrayAdapter<>(StartExerciseActivity.this, android.R.layout.simple_spinner_item, swimmingStyles);
                swimmingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                startExerciseSpinner.setAdapter(swimmingAdapter);
                String[] swimmingSelection = selection;
                startExerciseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        swimmingSelection[0] = swimmingStyles[i];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                break;
            case "hiking":
                spinnerLabel.setText("Choose the terrain for hiking");
                ArrayAdapter<String> hikingAdapter = new ArrayAdapter<>(StartExerciseActivity.this, android.R.layout.simple_spinner_item, hikingTerrain);
                hikingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                startExerciseSpinner.setAdapter(hikingAdapter);
                String[] hikingSelection = selection;
                startExerciseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        hikingSelection[0] = hikingTerrain[i];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                break;
            case "biking":
                spinnerLabel.setText("Choose a biking type");
                ArrayAdapter<String> bikingAdapter = new ArrayAdapter<>(StartExerciseActivity.this, android.R.layout.simple_spinner_item, bikingTypes);
                bikingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                startExerciseSpinner.setAdapter(bikingAdapter);
                String[] bikingSelection = selection;
                startExerciseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        bikingSelection[0] = bikingTypes[i];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                break;
            default:
                spinnerLabel.setVisibility(View.GONE);
                startExerciseSpinner.setVisibility(View.GONE);
                break;
        }
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        myMap.getUiSettings().setScrollGesturesEnabled(true);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            myMap.setMyLocationEnabled(true);
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(StartExerciseActivity.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                myMap.addMarker(new MarkerOptions().position(latLng));
                                myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                            }
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }

    private void updateLocationOnMap(Location location) {
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        myMap.clear();
        myMap.addMarker(new MarkerOptions().position(userLocation).title("User Location"));
        pathPoints.add(userLocation);
        PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(pathPoints)
                .width(17)
                .color(ContextCompat.getColor(this, R.color.colorPolyline));
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
        Polyline polyline = myMap.addPolyline(polylineOptions);
    }

    private void calculateDistance(Location location) {
        if (previousLocation != null) {
            totalDistance += previousLocation.distanceTo(location);
        }
        previousLocation = location;
    }

    private void calculateElevationGain(Location location) {
        double currentElevation = location.getAltitude();
        if (currentElevation > previousElevation) {
            totalElevationGain += (currentElevation - previousElevation);
            // Update UI with the updated elevation gain
        }
        previousElevation = currentElevation;
    }

    private void startChronometer() {
        if (!isRunning) {
            startExerciseTimerView.setBase(SystemClock.elapsedRealtime());
            startExerciseTimerView.start();
            isRunning = true;
        }
    }

    private String stopChronometer() {
        String duration = "";
        if (isRunning) {
            startExerciseTimerView.stop();
            isRunning = false;
            long elapsedMillis = SystemClock.elapsedRealtime() - startExerciseTimerView.getBase();
            long seconds = (elapsedMillis / 1000) % 60;
            long minutes = (elapsedMillis / (1000 * 60)) % 60;
            long hours = (elapsedMillis / (1000 * 60 * 60));
            duration = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        return duration;
    }
    private String[] getDateTime() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Months are zero-based
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        String currentDate = String.format("%04d-%02d-%02d", year, month, day);
        String currentTime = String.format("%02d:%02d:%02d", hour, minute, second);
        return new String[] {currentDate, currentTime};
    }


}

