package com.ryocum.data;

import java.sql.Timestamp;
import java.util.Date;



public class Settings implements Thermostat {

    private int id;
    private float temp1;
    private float temp2;
    private String timeOfDay;
    public Settings() {
    }

    public Settings(int id, float temp1, float temp2, String timeOfDay) {
        this.temp1 = temp1;
        this.temp2 = temp2;
        this.id = id;
        this.timeOfDay = timeOfDay;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
   
    public float getTemp2() {
        return temp2;
    }

    public void setTemp2(float temp2) {
        this.temp2 = temp2;
    }  
    public float getTemp1() {
        return temp1;
    }

    public void setTemp1(float temp1) {
        this.temp1 = temp1;
    }  

    public String getTimeOfDay() {
        return timeOfDay;
    }
    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }  
}
