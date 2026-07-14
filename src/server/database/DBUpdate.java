package database;

import java.sql.*;
import models.*;

public class DBUpdate {
    public void updateOperation(Object obj){
      //if we are accessing user table 
        if (obj instanceof User) {
            User user = (User) obj;
            DBConnection dbc = new DBConnection();
            String query = "UPDATE users SET student_name=?, student_email=?, password=?, role=?, year_of_study=?, expertise=?, max_hours_per_week=? WHERE student_id=?";
            try{
                PreparedStatement pst = dbc.con.prepareStatement(query);
                // gets the values from the user object and turns into sql
                pst.setString(1, user.getStudent_name());
                pst.setString(2, user.getStudent_email());
                pst.setString(3, user.getPassword());
                pst.setString(4, user.getRole().name().toLowerCase());
                pst.setInt(5, user.getYear_of_study());
                pst.setString(6, user.getExpertise());
                pst.setInt(7, user.getMax_hours_per_week());
                pst.setString(8, user.getStudent_id());
                int rows = pst.executeUpdate();
                System.out.println("Sucessfully added " + rows + " users");    
                
            }catch(SQLException sqle){
                System.out.println("Failed to add user" + sqle.getMessage());
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
        else if (obj instanceof TutoringSession){
            TutoringSession tutoringSession = (TutoringSession) obj;
            DBConnection dbc = new DBConnection();
            String query = "UPDATE sessions SET subject=?, tutor_id=?, datetime=?, max_students=?, status=? WHERE session_id=?";
            try{
                PreparedStatement pst = dbc.con.prepareStatement(query);
                // gets the values from the tutoring sesh object and turns into sql
                pst.setString(1, tutoringSession.getSubject());
                pst.setString(2, tutoringSession.getTutor_id());
                pst.setTimestamp(3, Timestamp.valueOf(tutoringSession.getDatetime()));
                pst.setInt(4, tutoringSession.getMax_students());
                pst.setString(5, tutoringSession.getStatus().name().toLowerCase());
                pst.setInt(6, tutoringSession.getSession_id());
                int rows = pst.executeUpdate();
                System.out.println("Sucessfully added " + rows + " users");
            }catch(SQLException sqle){
                System.out.println("Failed to add tutoring session" + sqle.getMessage());
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
        else if (obj instanceof Booking) {
            Booking booking = (Booking) obj;
            DBConnection dbc = new DBConnection();
            String query = "UPDATE bookings SET session_id=?, tutee_id=?, status=?, requested_at=? WHERE booking_id=?";
            try{
                PreparedStatement pst = dbc.con.prepareStatement(query);
                // gets the values from the tutoring sesh object and turns into sql
                pst.setInt(1, booking.getSession_id());
                pst.setString(2, booking.getTutee_id());
                pst.setString(3, booking.getStatus().name().toLowerCase());                
                pst.setTimestamp(4, Timestamp.valueOf(booking.getRequested_at()));
                pst.setInt(5,booking.getBooking_id());
                int rows = pst.executeUpdate();
                System.out.println("Sucessfully added " + rows + " users");
            }catch(SQLException sqle){
                System.out.println("Failed to add tutoring session" + sqle.getMessage());
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
