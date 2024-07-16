package com.ryocum.data;

import java.sql.Timestamp;
import java.util.Date;


public class State implements Thermostat {

    // true is on, false is off
    private boolean state= true;;
    private Timestamp date;

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean getState() {
        return state;
    }
   
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

}
