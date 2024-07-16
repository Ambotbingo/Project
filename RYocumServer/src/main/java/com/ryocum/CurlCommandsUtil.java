package com.ryocum;

import com.google.gson.Gson;
import com.ryocum.data.Report;
import com.ryocum.data.State;
import com.ryocum.data.Temperature;
import com.ryocum.data.Thermostat;
import fi.iki.elonen.NanoHTTPD;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Calendar;

import static fi.iki.elonen.NanoHTTPD.MIME_PLAINTEXT;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;
import static com.ryocum.JDBCConnection.addState;
import static com.ryocum.JDBCConnection.updateTemp;

public final class CurlCommandsUtil {

    private static final String STATE = "status";
    private static final String TEMP = "temp";
    private static final String REPORT = "report";

    private CurlCommandsUtil() {
    }

    public static NanoHTTPD.Response performGet(NanoHTTPD.IHTTPSession session) {
        String jsonResp = null;
        String route = getRoute(session.getUri());
        String param = cleanValue(session.getUri());
        Gson gson = new Gson();

        if (route != null) {
            if (route.equals(TEMP)) {
                if (param != null && !param.equals("")) {
                    Temperature temp = JDBCConnection.getTemp(param);
                    if (temp == null) {
                        return failedAttempt("Temperature value was null.\n");
                    }
                    jsonResp = gson.toJson(temp);
                } else {
                    List<Temperature> temps = JDBCConnection.getAllTemps();
                    if (temps.isEmpty()) {
                        return failedAttempt("The GET request has no available information.\n");
                    }
                    jsonResp = gson.toJson(temps);
                }
            } else if (route.equals(STATE)) {
                State state = JDBCConnection.getState();
                if (state == null) {
                    jsonResp = Boolean.toString(true);
                }
                jsonResp = Boolean.toString(state.isOn());
            } 
           

            return newFixedLengthResponse("\n" +jsonResp + "\n");
        }
        return failedAttempt("Please provide a valid URL path to display or update the thermostat information. For example of this path is HTTP://18.217.90.61:8080/status \n\nAvailable paths include the following: \n\nFor the state or status of the thermostat : HTTP://18.217.90.61:8080/status\n" +
        "For the temperature of the thermostat : HTTP://18.217.90.61:8080/temp\n" + "For the report of the thermostat : HTTP://18.217.90.61:8080/report\n");
    }

    public static NanoHTTPD.Response performPost(NanoHTTPD.IHTTPSession session) {
        try {
            session.parseBody(new HashMap<>());
            String route = session.getUri().replace("/", "");
            Thermostat thermostat = parseRouteParams(
                    session.getQueryParameterString(),
                    route);
   
            String result = null;
            if (thermostat == null) {
                return newFixedLengthResponse("temp or time values unsupported");
            }
            if(route.equals(TEMP))
            {
             result = JDBCConnection.AddInformation(session.getQueryParameterString());
            }
            else if (route.equals(STATE))
            {
             result = JDBCConnection.AddInformation(session.getQueryParameterString());
            }     
            
            return newFixedLengthResponse(result);
        } catch (IOException | NanoHTTPD.ResponseException e) {
            return failedAttempt("unable to commit post");
        }
    }
    public static NanoHTTPD.Response performDelete(NanoHTTPD.IHTTPSession session) {
        String route = session.getUri().replace("/", "");
        if (route == TEMP) {
            String result = JDBCConnection.deleteTemp(cleanValue(session.getUri()));           
            return newFixedLengthResponse(result);
        } else if (route == REPORT) {
            String result = JDBCConnection.deleteTemp(cleanValue(session.getUri()));
            return newFixedLengthResponse("\n" +result  + "\n");
        }

        return failedAttempt("Unable to delete object, make sure correct route\n");
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

    private static String cleanValue(String param) {
        return param.replaceAll("[^0-9]", "");
    }

    private static String getRoute(String param) {
        if (param.contains(TEMP)) {
            return TEMP;
        } else if (param.contains(STATE)) {
            return STATE;
        } else if (param.contains(REPORT)) {
            return REPORT;
        }
        return null;
    }
}
