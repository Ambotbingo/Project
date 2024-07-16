package com.ryocum;

import com.ryocum.data.Report;
import com.ryocum.data.State;
import com.ryocum.data.Temperature;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;


public final class JDBCConnection {

    private static final String DB_CONNECTION = "jdbc:mysql://127.0.0.1:3306/thermostat";
    private static final String ROOT = "root";
    private static final String PASSWORD = "Benjamin12!";

    private JDBCConnection() {
    }    

    // get request based on ID
    public static final Temperature getTemp(String id) {

        String select = "select * from temp where id = " + id;
        try ( Connection conn = setupConnection()) {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(select);
            Temperature temp = new Temperature();
            while (resultSet.next()) {
                temp.setId(resultSet.getInt("ID"));
                temp.setTemp(resultSet.getFloat("TEMP"));
            }
            return temp;
        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
        }
        return null;
    }

    public static final State getState() {
        String select = "select * from state";
        try ( Connection conn = setupConnection()) {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(select);
            State state = new State();
            while (resultSet.next()) {
                int currentState = resultSet.getInt("STATE");
                if (currentState == 1) {
                    state.setOn(true);
                } else  if (currentState == 0 ){
                    state.setOn(false);
                }
            }
            return state;
        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
        }
        return null;
    }

   
    public static final List<Temperature> getAllTemps() {
        List<Temperature> temps = new ArrayList<>();
        String select = "select * from temp";

        try ( Connection conn = setupConnection()) {

            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(select);
            while (resultSet.next()) {

                Temperature obj = new Temperature();
                obj.setId(resultSet.getInt("ID"));                
                obj.setTemp(resultSet.getFloat("TEMP"));     
                obj.setDateTime(resultSet.getTimestamp("DATETIMEINFO"));          
                temps.add(obj);
            }

        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
        }
        return temps;
    }
 
    public static final String addReport(Report report) {
        String insert = "insert into report (temp, date) values ('"
                + report.getTemp()
                + "', '"
                + report.getDate()
                + "')";

        try ( Connection conn = setupConnection()) {
            Statement statement = (Statement) conn.createStatement();
            statement.execute(insert);
        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
            return "Post state Failed\n";
        }

        return "Post Report successful\n";
    }

    public static final String updateState(boolean value) {
        String update = null;

        if (value) {
            update = "update state set state = ''";
        } else {
            update = "update state set state = NULL";
        }

        try ( Connection conn = setupConnection()) {
            Statement statement = (Statement) conn.createStatement();
            statement.execute(update);
        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
            return "Update state Failed\n";
        }

        return "Post state Failed\n";
    }

    public static final String addState(State state) {
        String insert = null;
        if (state.isOn()) {
            insert = "insert into state (state, date) values ('', '"
                    + state.getDate()
                    + "')";
        } else {
            insert = "insert into state (state, date) values (NULL, '"
                    + state.getDate()
                    + "')";
        }

        try ( Connection conn = setupConnection()) {
            Statement statement = (Statement) conn.createStatement();
            statement.execute(insert);
        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
            return "Post state Failed\n";
        }

        return "Post state Successful\n";

    }

    // Add information to Database
    public static final String AddInformation(String tempString) {
        if (tempString != null && tempString!= "") {
            float temp= Float.parseFloat(tempString);
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



    // add a temp to the database
    // public static final String updateTemp(Temperature temp) {
        
    //     String update = "update temp set temp = " + 
    //             temp.getTemp() +                 
    //             " where id = " + 
    //             temp.getId();

    //     try ( Connection conn = setupConnection()) {
    //         Statement statement = (Statement) conn.createStatement();
    //         statement.execute(update);
    //     } catch (SQLException ex) {
    //         System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
    //         return "Post temp Failed\n";
    //     }
    //     return "Post temperature Successful\n";
    // }

    // delete temp from database
    public static final String deleteTemp(String id) {
        String insert = "delete from temp where id = " + id;
        try ( Connection conn = setupConnection()) {
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
