package dbtest;
import server.database.DBOperationsImpl;
import models.*;
import java.time.LocalDateTime;
import java.util.List;

public class TestDBOperations {
    public static void main(String[] args) {
        DBOperationsImpl db = new DBOperationsImpl();
        
        // ============================================================
        // INSERT TESTS
        // ============================================================
        System.out.println("--- INSERT TESTS ---");
        
        // Insert a tutee
        Tutee tutee = new Tutee("TEST001", "Test Tutee", "testtutee@strathmore.edu", "pass123", Role.TUTEE, 2, null, 0);
        db.insertOperation(tutee);
        
        // Insert a tutor
        Tutor tutor = new Tutor("TEST002", "Test Tutor", "testtutor@strathmore.edu", "pass123", Role.TUTOR, 3, "Calculus", 10);
        db.insertOperation(tutor);
        
        // Insert a session (session_id=0 because DB generates it with SERIAL)
        TutoringSession session = new TutoringSession(0, "Calculus II", "TEST002", LocalDateTime.now().plusDays(1), 3, SessionStatus.AVAILABLE);
        db.insertOperation(session);
        
        // Insert a booking — you need a real session_id from DB, use 1 for now
        Booking booking = new Booking(0, 1, "TEST001", BookingStatus.PENDING, LocalDateTime.now());
        db.insertOperation(booking);
        
        // ============================================================
        // SELECT TESTS
        // ============================================================
        System.out.println("--- SELECT TESTS ---");
        
        Object fetchedUser = db.selectOperation("TEST001", "users");
        System.out.println("Fetched user: " + ((fetchedUser != null) ? ((User) fetchedUser).getStudent_name() : "null"));
        
        Object fetchedSession = db.selectOperation(1, "sessions");
        System.out.println("Fetched session: " + ((fetchedSession != null) ? ((TutoringSession) fetchedSession).getSubject() : "null"));
        
        Object fetchedBooking = db.selectOperation(1, "bookings");
        System.out.println("Fetched booking: " + ((fetchedBooking != null) ? ((Booking) fetchedBooking).getStatus() : "null"));
        
        // ============================================================
        // SELECT ALL TESTS
        // ============================================================
        System.out.println("--- SELECT ALL TESTS ---");
        
        List<Object> allUsers = db.selectAllOperation("users");
        System.out.println("Total users: " + allUsers.size());
        
        List<Object> allSessions = db.selectAllOperation("sessions");
        System.out.println("Total sessions: " + allSessions.size());
        
        List<Object> allBookings = db.selectAllOperation("bookings");
        System.out.println("Total bookings: " + allBookings.size());
        
        // ============================================================
        // UPDATE TESTS
        // ============================================================
        System.out.println("--- UPDATE TESTS ---");
        
        // Update the tutee's name
        Tutee updatedTutee = new Tutee("TEST001", "Updated Tutee Name", "testtutee@strathmore.edu", "pass123", Role.TUTEE, 2, null, 0);
        db.updateOperation(updatedTutee);
        
        // Verify the update
        Object verifyUser = db.selectOperation("TEST001", "users");
        System.out.println("Updated user name: " + ((verifyUser != null) ? ((User) verifyUser).getStudent_name() : "null"));
        
        // ============================================================
        // DELETE TESTS
        // ============================================================
        System.out.println("--- DELETE TESTS ---");
        
        db.deleteOperation(1, "bookings");
        db.deleteOperation(1, "sessions");
        db.deleteOperation("TEST001", "users");
        db.deleteOperation("TEST002", "users");
        
        // Verify deletions
        System.out.println("After delete - booking: " + db.selectOperation(1, "bookings"));
        System.out.println("After delete - session: " + db.selectOperation(1, "sessions"));
        System.out.println("After delete - user TEST001: " + db.selectOperation("TEST001", "users"));
        
        System.out.println("--- ALL TESTS DONE ---");
    }
}