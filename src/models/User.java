package models;
// abstract since a user isnt created but it is role based 

import java.io.Serializable;


public abstract class User implements Serializable{
    //var
    private String student_id;
    private String student_name;
    private String student_email;
    private String password;
    private Role role;
    private int year_of_study;
    private String expertise;
    private int max_hours_per_week;

    public User(String student_id, String student_name, String student_email, String password, Role role, int year_of_study, String expertise, int max_hours_per_week) {
        this.student_id = student_id;
        this.student_name = student_name;
        this.student_email = student_email;
        this.password = password;
        this.role = role;
        this.year_of_study = year_of_study;
        this.expertise = expertise;
        this.max_hours_per_week = max_hours_per_week;
    }
    
    ///getter
    public String getStudent_id() {
        return student_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public String getStudent_email() {
        return student_email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public int getYear_of_study() {
        return year_of_study;
    }

    public String getExpertise() {
        return expertise;
    }

    public int getMax_hours_per_week() {
        return max_hours_per_week;
    }
    
    //setters
    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public void setStudent_email(String student_email) {
        this.student_email = student_email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setYear_of_study(int year_of_study) {
        this.year_of_study = year_of_study;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public void setMax_hours_per_week(int max_hours_per_week) {
        this.max_hours_per_week = max_hours_per_week;
    }
    
}
