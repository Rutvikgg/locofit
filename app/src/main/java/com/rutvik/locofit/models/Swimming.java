package com.rutvik.locofit.models;

public class Swimming extends Exercise{
    public String style;

    public Swimming() {
    }

    public Swimming(double distance, String style, String duration, String dateTime, int weight) {
        super(distance, duration, dateTime, weight);
        this.style = style;
        this.MET = calcMET(style, duration);
        this.speed = calcSpeed(distance, duration);
        this.caloriesBurned = calcCaloriesBurned(weight, distance, this.MET);
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

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
