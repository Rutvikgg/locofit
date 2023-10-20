package com.rutvik.locofit.models;

import com.rutvik.locofit.R;

import java.io.Serializable;

public class Hiking extends Exercise implements Serializable {
    public String terrainDifficultyRating;
    public double elevationGain;

    public Hiking() {
        this.exerciseType = "hiking";
        this.imgResource = R.drawable.hiking;
    }

    public Hiking(double distance, String duration, String onDate, int weight, String location, String onTime, String terrainDifficultyRating, double elevationGain) {
        super(distance, duration, onDate, weight, location, onTime);
        this.terrainDifficultyRating = terrainDifficultyRating;
        this.elevationGain = elevationGain;
        this.MET = calcMET(terrainDifficultyRating, duration);
        this.caloriesBurned = calcCaloriesBurned(weight, distance, this.MET);
        this.speed = calcSpeed(distance, duration);
        this.imgResource = R.drawable.hiking;
        this.exerciseType = "hiking";
    }

    public double calcMET(String terrainDifficultyRating, String duration) {
        int durationHours = Integer.parseInt(duration.substring(0, 2));
        int durationMinutes = Integer.parseInt(duration.substring(3, 5));
        int durationSeconds = Integer.parseInt(duration.substring(6, 8));
        int MET = 0;
        switch (terrainDifficultyRating) {
            case "Easy":
                MET = 2;
                break;
            case "Moderate":
                MET = 4;
                break;
            case "Moderately Strenuous":
                MET = 6;
                break;
            case "Strenuous":
                MET = 8;
                break;
            case "Very Strenuous":
                MET = 10;
                break;
            default:
                MET = 1;
                break;
        }
        return MET * (durationHours + (durationMinutes * 0.0166667) + (durationSeconds*0.000277778));
    }

    public int getImgResource() {
        return imgResource;
    }

    public String getTerrainDifficultyRating() {
        return terrainDifficultyRating;
    }

    public void setTerrainDifficultyRating(String terrainDifficultyRating) {
        this.terrainDifficultyRating = terrainDifficultyRating;
    }

    public double getElevationGain() {
        return elevationGain;
    }

    public void setElevationGain(double elevationGain) {
        this.elevationGain = elevationGain;
    }

    @Override
    public String toString() {
        return "Hiking{" +
                "terrainDifficultyRating='" + terrainDifficultyRating + '\'' +
                ", elevationGain=" + elevationGain +
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
