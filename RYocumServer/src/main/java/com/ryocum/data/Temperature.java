package com.ryocum.data;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.zone.ZoneOffsetTransitionRule.TimeDefinition;

public class Temperature implements Thermostat {

    private int id;
    private float temp;
    private Timestamp date;
    private int hours;

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

    //24hrs format
    public int getHours()
    {
        int hour;
        if(date != null)
        {
        Timestamp stamp = date;
        Date newDate = new Date(stamp.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedDate = sdf.format(date);
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.setTime(date);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE); 
        return hour;   
        }
        return 0;        
    }
}
