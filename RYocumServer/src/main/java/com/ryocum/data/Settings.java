package com.ryocum.data;

import java.sql.Timestamp;
import java.util.Date;



public class Settings implements Thermostat {

    private int id;
    private float temp1;
    private float temp2;
    //private Timestamp date;
    public Settings() {
    }

    public Settings(String state) {
        this.state = state;        
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
}
