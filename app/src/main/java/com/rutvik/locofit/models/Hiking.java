package com.rutvik.locofit.models;

public class Hiking extends Exercise{
    public String terrainDifficultyRating;
    public double elevationGain;

    public Hiking() {
        this.exerciseType = "hiking";
    }

    public Hiking(double distance, String duration, String dateTime, int weight, String terrainDifficultyRating, double elevationGain) {
        super(distance, duration, dateTime, weight);
        this.elevationGain = elevationGain;
        this.terrainDifficultyRating = terrainDifficultyRating;
        this.MET = calcMET(terrainDifficultyRating, duration);
        this.caloriesBurned = calcCaloriesBurned(weight, distance, this.MET);
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
}
