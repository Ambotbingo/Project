package com.ryocum.data;


public class Temperature implements Thermostat {

    private int id;
    private float temp;
    
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

    public void setTemp(int temp) {
        this.temp = temp;
    }    
}
