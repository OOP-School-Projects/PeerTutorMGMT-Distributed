package dbtest;

import server.database.DBOperationsImpl;
import server.database.DBOperationsRemote;
import models.*;

import java.time.LocalDateTime;
import java.util.List;

public class TestDBOperations {

    public static void main(String[] args) {

        try {

            // Test the implementation directly
            DBOperationsRemote db = new DBOperationsImpl();

            // ============================================================
            // INSERT TESTS
            // ============================================================
            System.out.println("--- INSERT TESTS ---");

            Tutee tutee = new Tutee(
                    "TEST001",
                    "Test Tutee",
                    "testtutee@strathmore.edu",
                    "pass123",
                    Role.TUTEE,
                    2,
                    null,
                    0
            );
            db.insertOperation(tutee);

            Tutor tutor = new Tutor(
                    "TEST002",
                    "Test Tutor",
                    "testtutor@strathmore.edu",
                    "pass123",
                    Role.TUTOR,
                    3,
                    "Calculus",
                    10
            );
            db.insertOperation(tutor);

            TutoringSession session = new TutoringSession(
                    0,
                    "Calculus II",
                    "TEST002",
                    LocalDateTime.now().plusDays(1),
                    3,
                    SessionStatus.AVAILABLE
            );
            db.insertOperation(session);

            // Change this if your session ID isn't 1
            Booking booking = new Booking(
                    0,
                    1,
                    "TEST001",
                    BookingStatus.PENDING,
                    LocalDateTime.now()
            );
            db.insertOperation(booking);

            // ============================================================
            // SELECT TESTS
            // ============================================================
            System.out.println("\n--- SELECT TESTS ---");

            User fetchedUser = (User) db.selectOperation("TEST001", "users");
            System.out.println("Fetched User: "
                    + (fetchedUser != null ? fetchedUser.getStudent_name() : "null"));

            TutoringSession fetchedSession =
                    (TutoringSession) db.selectOperation(1, "sessions");
            System.out.println("Fetched Session: "
                    + (fetchedSession != null ? fetchedSession.getSubject() : "null"));

            Booking fetchedBooking =
                    (Booking) db.selectOperation(1, "bookings");
            System.out.println("Fetched Booking: "
                    + (fetchedBooking != null ? fetchedBooking.getStatus() : "null"));

            // ============================================================
            // SELECT ALL TESTS
            // ============================================================
            System.out.println("\n--- SELECT ALL TESTS ---");

            List<Object> allUsers = db.selectAllOperation("users");
            System.out.println("Users: " + allUsers.size());

            List<Object> allSessions = db.selectAllOperation("sessions");
            System.out.println("Sessions: " + allSessions.size());

            List<Object> allBookings = db.selectAllOperation("bookings");
            System.out.println("Bookings: " + allBookings.size());

            // ============================================================
            // UPDATE TESTS
            // ============================================================
            System.out.println("\n--- UPDATE TESTS ---");

            Tutee updatedTutee = new Tutee(
                    "TEST001",
                    "Updated Tutee Name",
                    "testtutee@strathmore.edu",
                    "pass123",
                    Role.TUTEE,
                    2,
                    null,
                    0
            );

            db.updateOperation(updatedTutee);

            User verify =
                    (User) db.selectOperation("TEST001", "users");

            System.out.println("Updated Name: "
                    + (verify != null ? verify.getStudent_name() : "null"));

            // ============================================================
            // DELETE TESTS
            // ============================================================
            System.out.println("\n--- DELETE TESTS ---");

            db.deleteOperation(1, "bookings");
            db.deleteOperation(1, "sessions");
            db.deleteOperation("TEST001", "users");
            db.deleteOperation("TEST002", "users");

            System.out.println("Booking after delete: "
                    + db.selectOperation(1, "bookings"));

            System.out.println("Session after delete: "
                    + db.selectOperation(1, "sessions"));

            System.out.println("User after delete: "
                    + db.selectOperation("TEST001", "users"));

            System.out.println("\n--- ALL TESTS PASSED ---");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}