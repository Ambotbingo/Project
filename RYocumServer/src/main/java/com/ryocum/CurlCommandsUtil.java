package com.ryocum;

import com.google.gson.Gson;
import com.ryocum.data.Report;
import com.ryocum.data.Settings;
import com.ryocum.data.Status;
import com.ryocum.data.Temperature;
import com.ryocum.data.Thermostat;
import fi.iki.elonen.NanoHTTPD;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Calendar;
import java.util.Date;
import java.time.Instant;

import static fi.iki.elonen.NanoHTTPD.MIME_PLAINTEXT;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;


public final class CurlCommandsUtil {

    private static final String STATE = "status";
    private static final String TEMP = "temp";
    private static final String SETTINGS = "settings";
    private static final String REPORT = "report";

    private CurlCommandsUtil() {
    }

    public static NanoHTTPD.Response performGet(NanoHTTPD.IHTTPSession session) {
        String jsonResp = null;
        String route = getRoute(session.getUri());
        String param = clean(session.getUri());
        Gson gson = new Gson();
        Status stat = new Status();       

        if (route != null) {
            //display temperature
            if (route.equals(TEMP)) {
                if (param != null && !param.equals("")) {
                    Temperature temp = JDBCConnection.getTemp(param);
                    if (temp == null) {
                        return failedAttempt("Temperature value was null.\n");
                    }
                    jsonResp = gson.toJson(temp);
                    return newFixedLengthResponse(jsonResp);
                } else {
                    List<Temperature> temps = JDBCConnection.getAllTemps();
                    if (temps.isEmpty()) {
                        return failedAttempt("The GET request has no available information.\n");
                    }
                    jsonResp = gson.toJson(temps);
                    return newFixedLengthResponse(jsonResp);

                }
                //display status
            } else if (route.equals(STATE)) {
                stat = JDBCConnection.getState();                
                if (stat == null) {
                    return failedAttempt("The GET request has no available thermostat status information.\n");
                }
                jsonResp = gson.toJson(stat.getState());
                String currentStat = stat.getState();                
                return newFixedLengthResponse(currentStat);
                //Display settings
            } else if (route.equals(SETTINGS)) {
                if (param != null && !param.equals("")) {
                    Settings setting = JDBCConnection.getSetting(param);
                    if (setting== null) {
                        return failedAttempt("Settings value was null.\n");
                    }
                    jsonResp = gson.toJson(setting);
                    return newFixedLengthResponse(jsonResp);
                } else {
                    List<Settings> settings = JDBCConnection.getAllSettings();
                    if (settings.isEmpty()) {
                        return failedAttempt("The GET request for settings has no available information.\n");
                    }
                    jsonResp = gson.toJson(settings);
                    return newFixedLengthResponse(jsonResp);
                }
            }
            //Display Report
            else if (route.equals(REPORT)) {
                Report report = new Report();
                report = JDBCConnection.getReport();                                
                Instant now = Instant.now();
                if (report== null) {
                    return failedAttempt("The GET request has no available thermostat REPORT information.\n");
                }
                jsonResp = gson.toJson(report);
               
                return newFixedLengthResponse(jsonResp + "updated: "+ now);
            }
            return newFixedLengthResponse("Please provide a correct path.");
        }
        return failedAttempt(
                "Please provide a valid URL path to display or update the thermostat information. For example of this path is HTTP://18.217.90.61:8080/status \n\nAvailable paths include the following: \n\nFor the state or status of the thermostat : HTTP://18.217.90.61:8080/status\n"
                        +
                        "For the temperature of the thermostat : HTTP://18.217.90.61:8080/temp\n");
    }

    //Performing the POST request
    public static NanoHTTPD.Response performPost(NanoHTTPD.IHTTPSession session) {
        try {
            session.parseBody(new HashMap<>());
            String route = session.getUri().replace("/", "");
            String result = null;
            Thermostat thermostat = parseRouteParams(
                    session.getQueryParameterString(),
                    route);

            if (route.equals(TEMP)) {
                result = JDBCConnection.AddTemperature(session.getQueryParameterString());
            } else if (route.equals(STATE)) {
                result = JDBCConnection.updateState(session.getQueryParameterString());
            } else if (route.equals(SETTINGS)) {
                Settings setting = parseSettings(session.getQueryParameterString(),
                        route);
                if (setting != null) {
                    int id = setting.getId();
                    if (JDBCConnection.CheckForSettingID(id)) {
                        result = JDBCConnection.updateSetting(setting);
                    } else {
                        result = JDBCConnection.addSetting(setting);
                    }  
                } else {
                    return newFixedLengthResponse("Setting has some issue.");
                }
            }

            return newFixedLengthResponse(result);
        } catch (IOException | NanoHTTPD.ResponseException e) {
            return failedAttempt("unable to commit post");
        }
    }

    public static NanoHTTPD.Response performDelete(NanoHTTPD.IHTTPSession session) {
        String param = clean(session.getUri());
        String route = session.getUri().replace("/", "");

        if (route.equals(TEMP)) {
        String result = JDBCConnection.deleteTemp(clean(session.getUri()));
        return newFixedLengthResponse(result);
        }
        if (route.equals(SETTINGS)) {
            String result = JDBCConnection.deleteSetting(clean(session.getUri()));
            return newFixedLengthResponse(result);
            }
        String result = JDBCConnection.deleteTemp(clean(session.getUri()));
        return newFixedLengthResponse(result);

        // return failedAttempt("Unable to delete recored temperature. Make sure correct
        // the path is correct.\n");
    }

    public static NanoHTTPD.Response failedAttempt(String message) {
        return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, MIME_PLAINTEXT,
                message);
    }

    // temperature information
    private static Thermostat parseRouteParams(String input, String route) {
        if (route.equals(TEMP)) {
            float temp = Float.parseFloat(input);
            return new Temperature(temp);
        }         
        return null;
    }

    private static Integer tryParse(String text) {
        try {
          return Integer.parseInt(text);
        } catch (NumberFormatException e) {
          return null;
        }
      }

      private static Float tryParseFloat(String text) {
        try {
          return Float.parseFloat(text);
        } catch (NumberFormatException e) {
          return null;
        }
      }
    private static String clean(String param) {
        return param.replaceAll("[^0-9]", "");
    }

    private static String getRoute(String param) {
        if (param.contains(TEMP)) {
            return TEMP;
        } else if (param.contains(STATE)) {
            return STATE;
        }else if (param.contains(SETTINGS)) {
            return SETTINGS;
        }
        else if (param.contains(REPORT)) {
            return REPORT;
        }
        return null;
    }


    private static Settings parseSettings(String input, String route) {
        int id;
        float temp1;
        float temp2;
        String timeofday = null;
        
        if (route.equals(SETTINGS)) {
            String[] values = input.split(",");
            if (tryParse(values[0]) != null){
                id = Integer.parseInt(values[0]);                
            } else {
                return null;
            }
            if (tryParseFloat(values[1])!= null) {
                temp1 = Float.parseFloat(values[1]);                
            } else {
                return null;
            }
            if (tryParseFloat(values[2]) != null) {
                temp2 = Float.parseFloat(values[2]);             
            } else {
                return null;
            }  
            if(id == 1) 
            {
                timeofday = "MORNING";
            }
            else if(id == 2)
            {
                timeofday = "AFTERNOON";
            }
            else if (id ==3)
            {
                timeofday = "EVENING";
            }
            else
            {
                if (values[3] != null)
                {
                String newValue = values[3].trim().toUpperCase();
                timeofday = newValue; 
                }
                else
                {
                    timeofday = "";
                }
            }
                
                
            return new Settings(id, temp1, temp2, timeofday);          
        }
        return null;
    }
}
