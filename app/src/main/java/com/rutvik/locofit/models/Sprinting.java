package com.rutvik.locofit.models;

import com.rutvik.locofit.R;

public class Sprinting extends Exercise{
    public double acceleration;

    public Sprinting() {
        this.imgResource = R.drawable.sprinting;
        this.exerciseType = "sprinting";
    }

    public Sprinting(double distance, String duration, String onDate, int weight, String location, String onTime) {
        super(distance, duration, onDate, weight, location, onTime);
        this.speed = calcSpeed(distance, duration);
        this.MET = calcMET(duration);
        this.caloriesBurned = calcCaloriesBurned(weight, distance, this.MET);
        this.acceleration = calcAcceleration(this.speed, duration);
        this.exerciseType = "sprinting";
       this.imgResource = R.drawable.sprinting;

    }

    public double calcAcceleration(double speed, String duration) {
        int durationHours = Integer.parseInt(duration.substring(0, 2));
        int durationMinutes = Integer.parseInt(duration.substring(3, 5));
        int durationSeconds = Integer.parseInt(duration.substring(6, 8));
        return speed/((durationHours*3600)+(durationMinutes*60)+durationSeconds);
    }

    public double calcMET(String duration) {
        int durationHours = Integer.parseInt(duration.substring(0, 2));
        int durationMinutes = Integer.parseInt(duration.substring(3, 5));
        int durationSeconds = Integer.parseInt(duration.substring(6, 8));
        return 16 * (durationHours + (durationMinutes * 0.0166667) + (durationSeconds*0.000277778));
    }

    public double getAcceleration() {
        return acceleration;
    }

    public int getImgResource() {
        return imgResource;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    @Override
    public String toString() {
        return "Sprinting{" +
                "acceleration=" + acceleration +
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
