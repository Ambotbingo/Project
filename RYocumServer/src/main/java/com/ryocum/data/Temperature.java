package com.ryocum.data;

import java.sql.Timestamp;
import java.time.zone.ZoneOffsetTransitionRule.TimeDefinition;

public class Temperature implements Thermostat {

    private int id;
    private float temp;
    private Timestamp dateTimeInfo;
    
    public Temperature() {
    }

    public Temperature(float temp) {
        this.temp = temp;        
    }

    public DATETIMEINFO(Timestamp dateTimeInfo) {
      this.dateTimeInfo= dateTimeInfo;        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TimeStamp getDateTime() {
        return dateTimeInfo;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }  

    public void setDateTime(Timestamp dateTimeInfo) {
        this.dateTimeInfo = dateTimeInfo;
    }   
}
