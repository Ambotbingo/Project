package com.ryocum.data;

import java.sql.Timestamp;
import java.util.Date;



public class Settings implements Thermostat {

    // true is on, false is off
    private String Time;
    private String state;
    private Timestamp date;
    private int id;

    public Settings() {
    }

    public Settings(String state) {
        this.state = state;        
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
   
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
