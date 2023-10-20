package com.rutvik.locofit.models;

import com.rutvik.locofit.R;

import java.io.Serializable;

public class Running extends Exercise implements Serializable {
    public Running(double distance, String duration, String onDate, int weight, String location, String onTime) {
        super(distance, duration, onDate, weight, location, onTime);
        this.speed = calcSpeed(distance, duration);
        this.MET = calcMET(distance, duration);
        this.caloriesBurned = calcCaloriesBurned(weight, distance, this.MET);
        this.imgResource = R.drawable.running;
        this.exerciseType = "running";
    }

    public Running() {
        this.imgResource = R.drawable.running;
        this.exerciseType = "running";
    }

    public int getImgResource() {
        return imgResource;
    }
}
