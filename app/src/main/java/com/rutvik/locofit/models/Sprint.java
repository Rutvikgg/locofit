package com.rutvik.locofit.models;

public class Sprint extends Exercise{
    public double acceleration;

    public Sprint() {
        this.exerciseType = "sprint";
    }

    public Sprint(double distance, String duration, String dateTime, int weight) {
        super(distance, duration, dateTime, weight);
        this.speed = calcSpeed(distance, duration);
        this.MET = calcMET(duration);
        this.caloriesBurned = calcCaloriesBurned(weight, distance, this.MET);
        this.acceleration = calcAcceleration(this.speed, duration);
        this.exerciseType = "sprint";
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

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }
}
