package com.rutvik.locofit.models;

import com.rutvik.locofit.R;

import java.io.Serializable;

public class Swimming extends Exercise implements Serializable {
    public String style;

    public Swimming() {
        this.imgResource = R.drawable.swimming;
        this.exerciseType = "swimming";
    }

    public Swimming(double distance, String duration, String onDate, int weight, String location, String onTime, String style) {
        super(distance, duration, onDate, weight, location, onTime);
        this.style = style;
        this.MET = calcMET(style, duration);
        this.caloriesBurned = calcCaloriesBurned(weight, distance, this.MET);
        this.imgResource = R.drawable.swimming;
        this.exerciseType = "swimming";
    }

    public double calcMET(String style, String duration) {
        int durationHours = Integer.parseInt(duration.substring(0, 2));
        int durationMinutes = Integer.parseInt(duration.substring(3, 5));
        int durationSeconds = Integer.parseInt(duration.substring(6, 8));
        int MET = 0;
        switch (style) {
            case "Freestyle":
                MET = 8;
                break;
            case "Breaststroke":
            case "Backstroke":
                MET = 7;
                break;
            case "Butterfly":
                MET = 14;
                break;
            default:
                MET = 3;
                break;
        }
        return MET * (durationHours + (durationMinutes * 0.0166667) + (durationSeconds*0.000277778));
    }

    public int getImgResource() {
        return imgResource;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    @Override
    public String toString() {
        return "Swimming{" +
                "style='" + style + '\'' +
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
