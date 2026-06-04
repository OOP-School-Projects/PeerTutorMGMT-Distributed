package database;
import java.sql.*;
import models.*;


public class DBSelect {
    public <T> Object selectOperation(T id,String table){
        if(table.equals("users")){
            DBConnection dbc = new DBConnection();
            try{
                String query = "SELECT * FROM users WHERE student_id = ?";
                PreparedStatement pst = dbc.con.prepareStatement(query);
                pst.setObject(1, id);
                ResultSet rs = pst.executeQuery();
                //if to determine the person accesing the table 
                if(rs.next()){
                    String roleStr = rs.getString("role");
                    // if the user is a tutee print a tutee object, same for tutor and admin
                    if(roleStr.equals("tutee")){
                        Tutee user = new Tutee(rs.getString("student_id"), rs.getString("student_name"), rs.getString("student_email"), rs.getString("password"), Role.valueOf(roleStr.toUpperCase()), rs.getInt("year_of_study"), rs.getString("expertise"), rs.getInt("max_hours_per_week"));
                        return user;
                    } else if(roleStr.equals("tutor")){
                        Tutor user = new Tutor(rs.getString("student_id"), rs.getString("student_name"), rs.getString("student_email"), rs.getString("password"), Role.valueOf(roleStr.toUpperCase()), rs.getInt("year_of_study"), rs.getString("expertise"), rs.getInt("max_hours_per_week"));
                        return user;
                    } else if(roleStr.equals("admin")){
                        Admin user = new Admin(rs.getString("student_id"), rs.getString("student_name"), rs.getString("student_email"), rs.getString("password"), Role.valueOf(roleStr.toUpperCase()), rs.getInt("year_of_study"), rs.getString("expertise"), rs.getInt("max_hours_per_week"));
                        return user;
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
        }else if(table.equals("sessions")){
            DBConnection dbc = new DBConnection();
            try{
               String query = "SELECT * FROM sessions WHERE session_id= ?"; 
               PreparedStatement pst = dbc.con.prepareStatement(query);
               pst.setObject(1, id);
               ResultSet rs = pst.executeQuery();
                if(rs.next()){
                    TutoringSession session = new TutoringSession(rs.getInt("session_id"), rs.getString("subject"), rs.getString("tutor_id"), rs.getTimestamp("datetime").toLocalDateTime(), rs.getInt("max_students"), SessionStatus.valueOf(rs.getString("status").toUpperCase()));
                    return session;
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
        }else if(table.equals("bookings")){
            DBConnection dbc = new DBConnection();
            try{
                String query = "SELECT * FROM bookings WHERE booking_id = ?";
               PreparedStatement pst = dbc.con.prepareStatement(query);
               pst.setObject(1, id);
               ResultSet rs = pst.executeQuery();
               if(rs.next()){
                    Booking booking = new Booking(rs.getInt("booking_id"), rs.getInt("session_id"), rs.getString("tutee_id"), BookingStatus.valueOf(rs.getString("status").toUpperCase()), rs.getTimestamp("requested_at").toLocalDateTime());
                    return booking;
               }           
            }catch(SQLException sqle){
                System.out.println("failed to fetch" + sqle.getMessage());     
            } finally {
                if (dbc.con != null) {
                    try { 
                        dbc.con.close(); 
                    } catch (SQLException sqle) {
                        System.out.println("Failed to close con"+ sqle.getMessage());
                    }
                }
            }            
        }
        return null;
    }
}