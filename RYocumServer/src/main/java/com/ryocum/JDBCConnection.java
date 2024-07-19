package com.ryocum;

import com.ryocum.data.Report;
import com.ryocum.data.Status;
import com.ryocum.data.Temperature;
import com.ryocum.data.Settings;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public final class JDBCConnection {

    private static final String DB_CONNECTION = "jdbc:mysql://127.0.0.1:3306/thermostat";
    private static final String ROOT = "root";
    private static final String PASSWORD = "Benjamin12!";

    private JDBCConnection() {
    }

    // get request based on ID
    public static final Temperature getTemp(String id) {

        String select = "select * from temp where id = " + id;
        try (Connection conn = setupConnection()) {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(select);
            Temperature temp = new Temperature();
            while (resultSet.next()) {
                temp.setId(resultSet.getInt("ID"));
                temp.setTemp(resultSet.getFloat("TEMP"));
                temp.setDate(resultSet.getTimestamp("TIMDEDATEINFO"));
            }
            return temp;
        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
        }
        return null;
    }

    //get settings based on ID     
    public static final Settings getSetting(String id) {

        String select = "select * from settings where id = " + id;
        try (Connection conn = setupConnection()) {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(select);
            Settings setting = new Settings();
            while (resultSet.next()) {
                setting.setId(resultSet.getInt("ID"));
                setting.setTemp1(resultSet.getFloat("TEMP1"));
                setting.setTemp2(resultSet.getFloat("TEMP2"));
                setting.setTimeOfDay(resultSet.getString("TIMEOFDAY"));
                //temp.setDate(resultSet.getTimestamp("TIMDEDATEINFO"));
            }
            return setting;
        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
        }
        return null;
    }


    public static final Status getState() {  

        String select = "select * from state";        
        try (Connection conn = setupConnection()) {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(select);
            Status obj = new Status();
            while (resultSet.next()) {               
                obj.setId(resultSet.getInt("ID"));
                obj.setState(resultSet.getString("STATUS"));
                obj.setDate(resultSet.getTimestamp("TIMEDATEINFO"));                
            }
            return obj;

        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
        }
        return null;
    }

    public static final List<Temperature> getAllTemps() {
        List<Temperature> temps = new ArrayList<>();
        int rowNum=0;
        rowNum = CountTempRow();
        if(rowNum > 50)
        {
                DeleteTemp();
        }
        String select = "select * from temp";

        try (Connection conn = setupConnection()) {

            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(select);
            while (resultSet.next()) {

                Temperature obj = new Temperature();
                obj.setId(resultSet.getInt("ID"));
                obj.setTemp(resultSet.getFloat("TEMP"));
                obj.setDate(resultSet.getTimestamp("TIMEDATEINFO"));
                temps.add(obj);
            }

        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
        }
        return temps;
    }   
    
    //get all settings in the table
    public static final List<Settings> getAllSettings() {
        List<Settings> settings = new ArrayList<>();        
        String select = "select * from settings";

        try (Connection conn = setupConnection()) {

            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(select);
            while (resultSet.next()) {

                Settings obj = new Settings();
                obj.setId(resultSet.getInt("ID"));
                obj.setTemp1(resultSet.getFloat("TEMP1"));
                obj.setTemp2(resultSet.getFloat("TEMP2"));
                obj.setTimeOfDay(resultSet.getString("TIMEOFDAY"));
                settings.add(obj);
            }

        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
        }
        return settings;
    }    

    //updating the state table
    public static final String updateState(String stateInString) {
        if (stateInString != null && stateInString != "") {
            // stateInString = stateInString.toUpperCase().trim();
            //String insert = "insert into state (status) values ('" + stateInString + "')";
            String insert = "update state SET status = '"+ stateInString + "'";
            try (Connection conn = setupConnection()) {
                Statement statement = (Statement) conn.createStatement();
                statement.execute(insert);
            } catch (SQLException ex) {
                System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
                return "Post state Failed\n";
            }

            return "Post is successfully added to the table.\n";
        }
        return "Post is invalid when malform request is given.\n";
    }

    // Add information to Database
    public static final String AddTemperature(String tempString) {
        if (tempString != null && tempString != "") {
            float temp = Float.parseFloat(tempString);
            //CheckForSettings(temp);
            String insert = "insert into temp (temp) values ('" + temp + "')";
            try (Connection conn = setupConnection()) {
                Statement statement = (Statement) conn.createStatement();
                statement.execute(insert);
            } catch (SQLException ex) {
                System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
                return "Post Failed\n";
            }
            return "Post is successfully added to the table.\n";
        }
        return "Post is invalid when malform request is given.\n";
    }

   public static Boolean CheckForSettingID (int id)
   {
    List<Settings> allsettings = getAllSettings();
    for ( Settings setting : allsettings)
    {
        if(setting.getId() == id)
        {
            return true;
        }
    }
    return false;
   }




//    private static int getHours() {
//        int hour;

//        Timestamp stamp = new Timestamp(System.currentTimeMillis());
//        Date newDate = new Date(stamp.getTime());
//        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
//        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
//        String formattedDate = sdf.format(date);
//        Calendar cal = Calendar.getInstance();
//        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
//        cal.setTime(date);
//        hour = cal.get(Calendar.HOUR_OF_DAY);
//        int minute = cal.get(Calendar.MINUTE);
//        return hour;
//    }

    private  static void DeleteTemp() {
        String del= "DELETE FROM temp";
        try (Connection conn = setupConnection()) {

            Statement statement = conn.createStatement();
            statement.execute(del);
            
        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
        }        
    }

    private static int CountTempRow() {
        String count = "SELECT COUNT(*) FROM temp";
        try (Connection conn = setupConnection()) {

            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(count);
            while (resultSet.next()) {

                return resultSet.getInt(1);
            }

        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
        }
        return 0;
    }

   //update the settings path
    public static final String updateSetting(Settings setting) {
        String timeOfDay = parseTimeOfDay();
        if(setting != null)
    {
        String update = "update settings set temp1 = " +
                setting.getTemp1() +
                ", temp2 = " +
                setting.getTemp2() +
                " where id = " +
                setting.getId() +
                " where timeOfDay = " +
                setting.getTimeOfDay();

        try (Connection conn = setupConnection()) {
            Statement statement = (Statement) conn.createStatement();
            statement.execute(update);
        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
            return "Post temp Failed\n";
        }
        return "Post settings Successful\n";
    }
    return "Please use a valid post format.\n For example: http://18.217.90.61:8080/settings -d <id>,<temp1>,<temp2>,<timeofday>> \n For morning id = 1 : curl -X POST http://18.217.90.61:8080/settings -d 1,70,71,MORNING\n";
    }
   

    //add settings
    public static final String addSetting(Settings setting) {
        if (setting != null) {
            String insert = "insert into settings (id, temp1, temp2, timeofday) values (" + setting.getId() +
            "," + setting.getTemp1() + "," + setting.getTemp2() + ", '" + setting.getTimeOfDay() + "')";
            try (Connection conn = setupConnection()) {
                Statement statement = (Statement) conn.createStatement();
                statement.execute(insert);
            } catch (SQLException ex) {
                System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
                return "Post Failed\n";
            }
            return "Post is successfully added to the table.\n";
        }
        return "Post is invalid when malform request is given.\n";
    }


    // delete temp from database
    public static final String deleteTemp(String id) {
        String insert = "delete from temp where id = " + id;
        try (Connection conn = setupConnection()) {
            Statement statement = (Statement) conn.createStatement();
            statement.execute(insert);
        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
            return "Delete Failed\n";
        }
        return "Delete Successful\n";
    }

    private static final Connection setupConnection() throws SQLException {
        return DriverManager.getConnection(DB_CONNECTION, ROOT, PASSWORD);
    }

    private static String parseTimeOfDay() {
    Calendar time = Calendar.getInstance();
    int hour = time.get(Calendar.HOUR_OF_DAY);
    if (hour >= 18) {
        return "EVENING";
    } else if (hour >= 12) {
        return "AFTERNOON";
    } else {
        return "MORNING";
    }
}

}
