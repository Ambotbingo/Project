package com.ryocum.data;

/**
 * create database thermostat;
 *
 * create table temps (id int unsigned not null auto_increment, temp int
 * unsigned not null, primary key(id));
 */
public class Temperature implements Thermostat {

    private int id;
    private int temp;
    private int temp2;
    private String setting;

    public Temperature() {
    }

    public Temperature(int temp, int temp2, String id) {
        this.temp = temp;
        this.temp2 = temp2;
        this.id = Integer.parseInt(id);
    }

    public Int getId() {
        return id;
    }

    public void setId(Int id) {
        this.id = id;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getTemp2() {
        return temp2;
    }

    public void setTemp2(int temp2) {
        this.temp2 = temp2;
    }

    public String getSetting() {
        return setting;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }

}
