package com.tony.weatherapp.model;

public class Day {

    private String description;
    private double temp;
    private String whatDay;

    public String getWhatDay() {
        return whatDay;
    }

    public void setWhatDay(String whatDay) {
        this.whatDay = whatDay;
    }

    public Day(String description, double temp, String whatDay) {
        this.description = description;
        this.temp = temp;
        this.whatDay = whatDay;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    @Override
    public String toString() {
        return "Day{" +
                "description='" + description + '\'' +
                ", temp=" + temp +
                '}';
    }
}
