package com.rutvik.locofit.models;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Exercise {
    public double distance;
    public double speed;
    public double caloriesBurned;
    public double MET;
    public LocalTime duration;
    public LocalDateTime dateTime;

    public Exercise(double distance, double speed, double caloriesBurned, double MET, LocalTime duration, LocalDateTime dateTime) {
        this.distance = distance;
        this.speed = speed;
        this.caloriesBurned = caloriesBurned;
        this.MET = MET;
        this.duration = duration;
        this.dateTime = dateTime;
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

    public LocalTime getDuration() {
        return duration;
    }

    public void setDuration(LocalTime duration) {
        this.duration = duration;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
