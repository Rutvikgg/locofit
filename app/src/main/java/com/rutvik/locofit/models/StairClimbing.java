package com.rutvik.locofit.models;

public class StairClimbing extends Exercise{
    public int noOfStairs;
    public double elevationGain;

    public StairClimbing() {
    }

    public StairClimbing(double distance, String duration, String dateTime, int weight, int noOfStairs, double elevationGain) {
        super(distance, duration, dateTime, weight);
        this.noOfStairs = noOfStairs;
        this.elevationGain = elevationGain;
        this.speed = calcSpeed(noOfStairs, duration);
        this.MET = calcMET(duration);


    }
    public double calcSpeed(int noOfStairs, String duration) {
        int durationHours = Integer.parseInt(duration.substring(0, 2));
        int durationMinutes = Integer.parseInt(duration.substring(3, 5));
        int durationSeconds = Integer.parseInt(duration.substring(6, 8));
        return noOfStairs / ((durationHours*3600)+(durationMinutes*60)+durationSeconds);
    }

    public double calcMET(String duration) {
        int durationHours = Integer.parseInt(duration.substring(0, 2));
        int durationMinutes = Integer.parseInt(duration.substring(3, 5));
        int durationSeconds = Integer.parseInt(duration.substring(6, 8));
        return 7 * (durationHours + (durationMinutes * 0.0166667) + (durationSeconds*0.000277778));
    }

    public int getNoOfStairs() {
        return noOfStairs;
    }

    public void setNoOfStairs(int noOfStairs) {
        this.noOfStairs = noOfStairs;
    }

    public double getElevationGain() {
        return elevationGain;
    }

    public void setElevationGain(double elevationGain) {
        this.elevationGain = elevationGain;
    }
}
