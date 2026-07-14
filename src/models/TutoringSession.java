package models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TutoringSession implements Serializable {
       
    //var
    private int session_id;
    private String subject;
    private String tutor_id;
    private LocalDateTime datetime;
    private int max_students;
    private SessionStatus status;

    //constructor
    public TutoringSession(int session_id, String subject, String tutor_id, LocalDateTime datetime, int max_students, SessionStatus status) {
        this.session_id = session_id;
        this.subject = subject;
        this.tutor_id = tutor_id;
        this.datetime = datetime;
        this.max_students = max_students;
        this.status = status;
    }

    //getters
    public int getSession_id() {
        return session_id;
    }

    public String getSubject() {
        return subject;
    }

    public String getTutor_id() {
        return tutor_id;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public int getMax_students() {
        return max_students;
    }

    public SessionStatus getStatus() {
        return status;
    }
    
    //setters
    public void setSession_id(int session_id) {
        this.session_id = session_id;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTutor_id(String tutor_id) {
        this.tutor_id = tutor_id;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public void setMax_students(int max_students) {
        this.max_students = max_students;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }
    
}
