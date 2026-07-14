package database;

import java.sql.*;
import java.util.*;
import models.*;

public class DBSelectAll {
    public List<Object> selectAllOperation(String table){
        if(table.equals("users")){
            DBConnection dbc = new DBConnection();
            List<Object> results = new ArrayList<>();
            try{
                String query = "SELECT * FROM users";
                PreparedStatement pst = dbc.con.prepareStatement(query);
                ResultSet rs = pst.executeQuery();
                while(rs.next()){
                    String roleStr = rs.getString("role");
                    if(roleStr.equals("tutee")){
                        Tutee user = new Tutee(rs.getString("student_id"), rs.getString("student_name"), rs.getString("student_email"), rs.getString("password"), Role.valueOf(roleStr.toUpperCase()), rs.getInt("year_of_study"), rs.getString("expertise"), rs.getInt("max_hours_per_week"));
                        results.add(user);
                    }else if(roleStr.equals("tutor")){
                        Tutor user = new Tutor(rs.getString("student_id"), rs.getString("student_name"), rs.getString("student_email"), rs.getString("password"), Role.valueOf(roleStr.toUpperCase()), rs.getInt("year_of_study"), rs.getString("expertise"), rs.getInt("max_hours_per_week"));
                        results.add(user);
                    }else if(roleStr.equals("admin")){
                        Admin user = new Admin(rs.getString("student_id"), rs.getString("student_name"), rs.getString("student_email"), rs.getString("password"), Role.valueOf(roleStr.toUpperCase()), rs.getInt("year_of_study"), rs.getString("expertise"), rs.getInt("max_hours_per_week"));
                        results.add(user);
                    }
                }
            }catch(SQLException sqle){
                System.out.println("failed to fetch" + sqle.getMessage());
            }finally {
                if (dbc.con != null) {
                    try { 
                        dbc.con.close(); 
                    } catch (SQLException sqle) {
                        System.out.println("Failed to close con"+ sqle.getMessage());
                    }
                }
            }
            return results;
        }else if(table.equals("sessions")){
            DBConnection dbc = new DBConnection();
            List<Object> results = new ArrayList<>();
            try{
                String query = "SELECT * FROM sessions";
                PreparedStatement pst = dbc.con.prepareStatement(query);
                ResultSet rs = pst.executeQuery();  
                while(rs.next()){
                    TutoringSession session = new TutoringSession(rs.getInt("session_id"), rs.getString("subject"), rs.getString("tutor_id"), rs.getTimestamp("datetime").toLocalDateTime(), rs.getInt("max_students"), SessionStatus.valueOf(rs.getString("status").toUpperCase()));
                    results.add(session);
                }
            }catch(SQLException sqle){
                System.out.println("failed to fetch" + sqle.getMessage());
            }finally {
                if (dbc.con != null) {
                    try { 
                        dbc.con.close(); 
                    } catch (SQLException sqle) {
                        System.out.println("Failed to close con"+ sqle.getMessage());
                    }
                }
            }return results;
        }else if(table.equals("bookings")){
            DBConnection dbc = new DBConnection();
            List<Object> results = new ArrayList<>();
            try{
                String query = "SELECT * FROM bookings";
                PreparedStatement pst = dbc.con.prepareStatement(query);
                ResultSet rs = pst.executeQuery();  
                while(rs.next()){
                    Booking booking = new Booking(rs.getInt("booking_id"), rs.getInt("session_id"), rs.getString("tutee_id"), BookingStatus.valueOf(rs.getString("status").toUpperCase()), rs.getTimestamp("requested_at").toLocalDateTime());
                    results.add(booking);
                }
            }catch(SQLException sqle){
                System.out.println("failed to fetch" + sqle.getMessage());
            }finally {
                if (dbc.con != null) {
                    try { 
                        dbc.con.close(); 
                    } catch (SQLException sqle) {
                        System.out.println("Failed to close con"+ sqle.getMessage());
                    }
                }
            }return results;
        }   
        return null;  
    }
}
