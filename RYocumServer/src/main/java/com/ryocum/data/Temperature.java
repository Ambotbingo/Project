package com.ryocum.data;

import java.util.Date;
import java.sql.Timestamp;
import java.time.zone.ZoneOffsetTransitionRule.TimeDefinition;

public class Temperature implements Thermostat {

    private int id;
    private float temp;
    private Timestamp date;
    
    public Temperature() {
    }

    public Temperature(float temp) {
        this.temp = temp;        
    }
   
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

   
    
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
