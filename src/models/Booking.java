package models;

import java.time.LocalDateTime;

public class Booking {      
    //Var
    private int booking_id;
    private int session_id;
    private String tutee_id;
    private BookingStatus status;
    private LocalDateTime requested_at;

    //Constructors
    public Booking(int booking_id, int session_id, String tutee_id, BookingStatus status, LocalDateTime requested_at) {
        this.booking_id = booking_id;
        this.session_id = session_id;
        this.tutee_id = tutee_id;
        this.status = status;
        this.requested_at = requested_at;
    }
    
    //Getters
    public int getBooking_id() {
        return booking_id;
    }

    public int getSession_id() {
        return session_id;
    }

    public String getTutee_id() {
        return tutee_id;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public LocalDateTime getRequested_at() {
        return requested_at;
    }
    
    //Setters 
    public void setBooking_id(int booking_id) {
        this.booking_id = booking_id;
    }

    public void setSession_id(int session_id) {
        this.session_id = session_id;
    }

    public void setTutee_id(String tutee_id) {
        this.tutee_id = tutee_id;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public void setRequested_at(LocalDateTime requested_at) {
        this.requested_at = requested_at;
    }
    
}
