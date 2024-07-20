package com.ryocum.data;

import java.time.Instant;
import java.util.Date;


public class Status implements Thermostat {

    
    private int id;
    private String state;
    private time date;

    public Status() {
    }

    public Status(String state) {
        this.state = state;        
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
   
    public time getDate() {
        return date;
    }

    public void setDate(time date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
