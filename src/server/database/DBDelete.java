package server.database;

import java.sql.*;


public class DBDelete {
    //using generic T for the diff id types
    public <T> void deleteOperation(T id, String table){
        //accessing user table
        //use .equal instead of == for string to check for value
        if (table.equals("users")){
            DBConnection dbc = new DBConnection();
            String query = "DELETE FROM users WHERE student_id = ?";
            try {
                PreparedStatement pst = dbc.con.prepareStatement(query);
                pst.setObject(1, id);
                int rows = pst.executeUpdate();
                System.out.println("Deleted " + rows + " rows");
            } catch (SQLException sqle) {
                System.out.println("Delete failed: " + sqle.getMessage());
            } finally {
                if (dbc.con != null) {
                    try { 
                        dbc.con.close(); 
                    } catch (SQLException sqle) {
                        System.out.println("Failed to close con"+ sqle.getMessage());
                    }
                }
            }
        }else if(table.equals("sessions")){
           DBConnection dbc = new DBConnection();
           String query = "DELETE FROM sessions WHERE session_id= ? ";
            try {
                PreparedStatement pst = dbc.con.prepareStatement(query);
                pst.setObject(1, id);
                int rows = pst.executeUpdate();
                System.out.println("Deleted " + rows + " rows");
            } catch (SQLException sqle) {
                System.out.println("Delete failed: " + sqle.getMessage());
            }finally {
                if (dbc.con != null) {
                    try { 
                        dbc.con.close(); 
                    } catch (SQLException sqle) {
                        System.out.println("Failed to close con"+ sqle.getMessage());
                    }
                }
            }
        }else if(table.equals("bookings")){
            DBConnection dbc = new DBConnection();
            String query = "DELETE FROM bookings WHERE booking_id=?";
             try {
                PreparedStatement pst = dbc.con.prepareStatement(query);
                pst.setObject(1, id);
                int rows = pst.executeUpdate();
                System.out.println("Deleted " + rows + " rows");
            } catch (SQLException sqle) {
                System.out.println("Delete failed: " + sqle.getMessage());
            }finally {
                if (dbc.con != null) {
                    try { 
                        dbc.con.close(); 
                    } catch (SQLException sqle) {
                        System.out.println("Failed to close con"+ sqle.getMessage());
                    }
                }
            }
        }
    }
}
