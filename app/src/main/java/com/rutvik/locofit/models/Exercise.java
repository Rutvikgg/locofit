package com.rutvik.locofit.models;


import com.rutvik.locofit.R;

import java.io.Serializable;

public class Exercise implements Serializable {
    public int imgResource = R.drawable.logo_landscape;
    public int id;
    public String exerciseType;
    public double distance;
    public double speed;
    public double caloriesBurned;
    public double MET;
    public String duration;
    public String onDate;
    public String onTime;
    public String location;

    public Exercise() {
    }

    public Exercise(double distance, String duration, String onDate, int weight, String location, String onTime) {
        this.distance = distance;
        this.duration = duration;
        this.onDate = onDate;
        this.location = location;
        this.onTime = onTime;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(double caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public double getMET() {
        return MET;
    }

    public void setMET(double MET) {
        this.MET = MET;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getOnDate() {
        return onDate;
    }

    public void setOnDate(String onDate) {
        this.onDate = onDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOnTime() {
        return onTime;
    }

    public void setOnTime(String onTime) {
        this.onTime = onTime;
    }

    public int getImgResource() {
        return imgResource;
    }

    public double calcSpeed(double distance, String duration) {
        int durationHours = Integer.parseInt(duration.substring(0, 2));
        int durationMinutes = Integer.parseInt(duration.substring(3, 5));
        int durationSeconds = Integer.parseInt(duration.substring(6));
        return distance / ((durationHours*3600)+(durationMinutes*60)+durationSeconds);
    }

    public double calcCaloriesBurned(int weight, double distance, double MET) {
        return weight * distance * MET;
    }

    public double calcMET(double distance, String duration) {
        int durationHours = Integer.parseInt(duration.substring(0, 2));
        int durationMinutes = Integer.parseInt(duration.substring(3, 5));
        int durationSeconds = Integer.parseInt(duration.substring(6, 8));
        return (distance * 0.001) / ((durationHours + (durationMinutes * 0.0166667) + (durationSeconds*0.000277778)) * 3.5 );
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
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
