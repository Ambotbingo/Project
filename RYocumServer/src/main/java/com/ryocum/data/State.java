package com.ryocum.data;

import java.sql.Timestamp;
import java.util.Date;


public class State implements Thermostat {

    // true is on, false is off
    private boolean on = true;
    private boolean off = false;
    private Timestamp date;

    public static State buildState(boolean value) {
        State state = new State();
        if( value == true)
        {
        state.setOn(value);
        }
        else{
            state.setOff(value);
        }
        return state;
    }
    
    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public void setOff(boolean off) {
        this.off = off;
    }
   
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

}
