package com.tony.weatherapp.model;

import java.util.Date;

public class Day {

    private String description;
    private double tempMax;

    public double getTempMax() {
        return tempMax;
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }

    public double getTempMin() {
        return tempMin;
    }

    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    private double tempMin;
    private Date date;


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Day(String description, double tempMin, double tempMax, Date date) {
        this.description = description;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }





}
