package com.ryocum;

import com.ryocum.data.Report;
import com.ryocum.data.Status;
import com.ryocum.data.Temperature;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class JDBCConnection {

    private static final String DB_CONNECTION = "jdbc:mysql://127.0.0.1:3306/thermostat/";
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

    private static void SetTimZone() {
        String timezone = "SET time_zone = '-07:00'";
        try (Connection conn = setupConnection()) {

            Statement statement = conn.createStatement();
            statement.execute(timezone);
            
        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
        }        
    }


    public static final String updateTemp(Temperature temp) {

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

}
