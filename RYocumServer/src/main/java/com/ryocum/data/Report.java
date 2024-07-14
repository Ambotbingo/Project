package com.ryocum.data;

import java.sql.Timestamp;
import java.time.Instant;


public class Report implements Thermostat {

    private int id;
    private int temp;
    private Timestamp date;

    public static final Report buildReport(int temp) {
        Report report = new Report();
        report.setTemp(temp);
        report.setDate(Timestamp.from(Instant.now()));
        return report;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

}
