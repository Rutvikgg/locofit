package com.rutvik.locofit.models;

public class Biking extends Exercise{
    public double elevationGain;
    public String type;

    public Biking() {
        this.exerciseType = "biking";
    }

    public Biking(double distance, String duration, String onDate, int weight, double elevationGain, String type) {
        super(distance, duration, onDate, weight);
        this.elevationGain = elevationGain;
        this.speed = calcSpeed(distance, duration);
        this.MET = calcMET(type, duration);
        this.caloriesBurned = calcCaloriesBurned(weight, distance, this.MET);
        this.exerciseType = "biking";
    }
    public double calcMET(String type, String duration) {
        int durationHours = Integer.parseInt(duration.substring(0, 2));
        int durationMinutes = Integer.parseInt(duration.substring(3, 5));
        int durationSeconds = Integer.parseInt(duration.substring(6, 8));
        int MET = 0;
        switch (type) {
            case "General Biking":
                MET = 7;
                break;
            case "Mountain Biking":
                MET = 12;
                break;
            default:
                MET = 5;
                break;
        }
        return MET * (durationHours + (durationMinutes * 0.0166667) + (durationSeconds*0.000277778));
    }

    public double getElevationGain() {
        return elevationGain;
    }

    public void setElevationGain(double elevationGain) {
        this.elevationGain = elevationGain;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
