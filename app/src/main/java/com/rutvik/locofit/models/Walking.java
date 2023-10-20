package com.rutvik.locofit.models;

import com.rutvik.locofit.R;

import java.io.Serializable;

public class Walking extends Exercise implements Serializable {
    public int stepCount;

    public Walking() {
        this.imgResource = R.drawable.walking;
        this.exerciseType = "walking";
    }

    public Walking(double distance, String duration, String onDate, int weight, String location, String onTime) {
        super(distance, duration, onDate, weight, location, onTime);
        this.stepCount = calcSteps(distance);
        this.speed = calcSpeed(distance, duration);
        this.MET = calcMET(distance, duration);
        this.caloriesBurned = calcCaloriesBurned(weight, distance, this.MET);
        this.imgResource = R.drawable.walking;
        this.exerciseType = "walking";
    }

    private int calcSteps(double distance) {
        return (int) ((distance / 10)/0.7);
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }
    public int getImgResource() {
        return imgResource;
    }

    @Override
    public String toString() {
        return "Walking{" +
                "stepCount=" + stepCount +
                ", id=" + id +
                ", exerciseType='" + exerciseType + '\'' +
                ", distance=" + distance +
                ", speed=" + speed +
                ", caloriesBurned=" + caloriesBurned +
                ", MET=" + MET +
                ", duration='" + duration + '\'' +
                ", onDate='" + onDate + '\'' +
                ", onTime='" + onTime + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
