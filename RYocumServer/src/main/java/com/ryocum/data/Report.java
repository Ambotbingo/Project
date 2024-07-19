package com.ryocum.data;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;


public class Report implements Thermostat {

    private int id;
    private int temp;
    private DateTime date;
    private String state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public float getState() {
        return state;
    }

    public void setState(String state){
        this.state = state;
    }


}
