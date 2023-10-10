package com.rutvik.locofit.models;

public class Running extends Exercise{
    public Running(double distance, String duration, String dateTime, int weight) {
        super(distance, duration, dateTime, weight);
        this.MET = calcMET(distance, duration);
        this.speed = calcSpeed(distance, duration);
        this.caloriesBurned = calcCaloriesBurned(weight, distance, this.MET);
        this.exerciseType = "running";
    }

    public Running() {
        this.exerciseType = "running";
    }
}
