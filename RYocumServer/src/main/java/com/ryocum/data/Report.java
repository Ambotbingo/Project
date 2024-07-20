package com.ryocum.data;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.zone.ZoneOffsetTransitionRule.TimeDefinition;
import java.util.Date;


public class Report implements Thermostat {

    private int id;
    private float temp;
    private Timestamp date;
    private String state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public DateTime getDate() {
        return date;
    }

    public Timestamp setDate(Timestamp date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state){
        this.state = state;
    }


}
