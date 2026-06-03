package models;

public class Tutor extends User {
    // constructor that calls the user fields 
    public Tutor(String student_id, String student_name, String student_email, String password, Role role, int year_of_study, String expertise, int max_hours_per_week) {
        super(student_id, student_name, student_email, password, role, year_of_study, expertise, max_hours_per_week);
    }
    
}
