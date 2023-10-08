package com.rutvik.locofit.models;

public class Walking extends Exercise{
    public int stepCount;

    public Walking() {
    }

    public Walking(double distance, int stepCount, String duration, String dateTime, int weight) {
        super(distance, duration, dateTime, weight);
        this.stepCount = stepCount;
        this.MET = calcMET(distance, duration);
        this.speed = calcSpeed(distance, duration);
        this.caloriesBurned = calcCaloriesBurned(weight, distance, this.MET);
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }
}
