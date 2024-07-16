package com.ryocum.data;

import java.sql.Timestamp;
import java.util.Date;


public class Status implements Thermostat {

    // true is on, false is off
    private Boolean state;
    private Timestamp date;

    public Status() {
    }

    public Status(Boolean  state) {
        this.state = state;        
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public Boolean getState() {
        return state;
    }
   
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

}
