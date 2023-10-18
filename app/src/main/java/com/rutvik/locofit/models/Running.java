package com.rutvik.locofit.models;

import java.io.Serializable;

public class Running extends Exercise implements Serializable {
    public Running(double distance, String duration, String onDate, int weight, String location, String onTime) {
        super(distance, duration, onDate, weight, location, onTime);
        this.speed = calcSpeed(distance, duration);
        this.MET = calcMET(distance, duration);
        this.caloriesBurned = calcCaloriesBurned(weight, distance, this.MET);
        this.exerciseType = "running";
    }

    public Running() {
        this.exerciseType = "running";
    }

}
