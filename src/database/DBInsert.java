package database;

import java.sql.*;
import models.*;

public class DBInsert {
    public void insertOperation(Object obj){
        //if we are accessing user table 
        if (obj instanceof User) {
            User user = (User) obj;
            DBConnection dbc = new DBConnection();
            String query = "INSERT INTO users VALUES (?,?,?,?,?,?,?,?)";
            try{
                PreparedStatement pst = dbc.con.prepareStatement(query);
                // gets the values from the user object and turns into sql
                pst.setString(1, user.getStudent_id());
                pst.setString(2, user.getStudent_name());
                pst.setString(3, user.getStudent_email());
                pst.setString(4, user.getPassword());
                pst.setString(5, user.getRole().name().toLowerCase());
                pst.setInt(6, user.getYear_of_study());
                pst.setString(7, user.getExpertise());
                pst.setInt(8, user.getMax_hours_per_week());
                int rows = pst.executeUpdate();
                System.out.println("Sucessfully added " + rows + " users");    
                
            }catch(SQLException sqle){
                System.out.println("Failed to add user" + sqle.getMessage());
            }
        }
        else if (obj instanceof TutoringSession){
            TutoringSession tutoringSession = (TutoringSession) obj;
            DBConnection dbc = new DBConnection();
            String query = "INSERT INTO sessions VALUES (?,?,?,?,?,?)";
            try{
                PreparedStatement pst = dbc.con.prepareStatement(query);
                // gets the values from the tutoring sesh object and turns into sql
                pst.setInt(1, tutoringSession.getSession_id());
                pst.setString(2, tutoringSession.getSubject());
                pst.setString(3, tutoringSession.getTutor_id());
                pst.setTimestamp(4, Timestamp.valueOf(tutoringSession.getDatetime()));
                pst.setInt(5, tutoringSession.getMax_students());
                pst.setString(6, tutoringSession.getStatus().name().toLowerCase());
                int rows = pst.executeUpdate();
                System.out.println("Sucessfully added " + rows + " users");
            }catch(SQLException sqle){
                System.out.println("Failed to add tutoring session" + sqle.getMessage());
            }
        }
        else if (obj instanceof Booking) {
            Booking booking = (Booking) obj;
            DBConnection dbc = new DBConnection();
            String query = "INSERT INTO bookings VALUES (?,?,?,?,?)";
            try{
                PreparedStatement pst = dbc.con.prepareStatement(query);
                // gets the values from the tutoring sesh object and turns into sql
                pst.setInt(1, booking.getBooking_id());
                pst.setInt(2, booking.getSession_id());
                pst.setString(3, booking.getTutee_id());
                pst.setString(4, booking.getStatus().name().toLowerCase());                
                pst.setTimestamp(5, Timestamp.valueOf(booking.getRequested_at()));
                int rows = pst.executeUpdate();
                System.out.println("Sucessfully added " + rows + " users");
            }catch(SQLException sqle){
                System.out.println("Failed to add tutoring session" + sqle.getMessage());
            }
        }
    }
}
