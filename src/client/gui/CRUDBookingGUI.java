package client.gui;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import models.*;
import server.database.*;
import java.time.LocalDateTime;

public class CRUDBookingGUI extends Application {
    //set before start() is called
    private User loggedInUser;
    private TutoringSession sessionToBook;
    private Booking bookingToManage;
    private String mode; // "request" (tutee) or "manage" (tutor/admin)
    private Runnable refreshCallback;
    
    public void setLoggedInUser(User user){ this.loggedInUser = user; }
    public void setSessionToBook(TutoringSession session){ this.sessionToBook = session; }
    public void setBookingToManage(Booking booking){ this.bookingToManage = booking; }
    public void setMode(String mode){ this.mode = mode; }
    public void setRefreshCallback(Runnable callback){ this.refreshCallback = callback; }
    
    @Override
    public void start(Stage crudStage){
        VBox root = new VBox();
        root.setSpacing(8);
        root.setPadding(new Insets(15));
        
        Label feedbackLabel = new Label();
        
        if(mode.equals("request")){
            //tutee requesting a session - show session details read-only
                Label titleLabel = new Label("Session Details");
                
                Label subjectLabel = new Label("Subject: " + sessionToBook.getSubject());
                Label tutorLabel = new Label("Tutor ID: " + sessionToBook.getTutor_id());
                Label datetimeLabel = new Label("Date & Time: " + sessionToBook.getDatetime().toString());
                Label maxLabel = new Label("Max Students: " + sessionToBook.getMax_students());
                
                Button confirmBtn = new Button("Confirm Request");
                confirmBtn.setOnAction(e -> {
                    //status auto set to PENDING
                    Booking newBooking = new Booking(0, sessionToBook.getSession_id(), loggedInUser.getStudent_id(), BookingStatus.PENDING, LocalDateTime.now());
                    DBOperations db = new DBOperationsImpl();
                    db.insertOperation(newBooking);
                    if(refreshCallback != null){ refreshCallback.run(); }
                    crudStage.close();
                });
                
                Button cancelBtn = new Button("Cancel");
                cancelBtn.setOnAction(e -> crudStage.close());
                
                root.getChildren().addAll(titleLabel, subjectLabel, tutorLabel, datetimeLabel, maxLabel, feedbackLabel, confirmBtn, cancelBtn);
            
        } else if(mode.equals("manage")){
            //tutor/admin approving or declining - show booking details + status dropdown
                Label titleLabel = new Label("Manage Booking");
                
                Label bookingIdLabel = new Label("Booking ID: " + bookingToManage.getBooking_id());
                Label tuteeLabel = new Label("Tutee ID: " + bookingToManage.getTutee_id());
                Label sessionLabel = new Label("Session ID: " + bookingToManage.getSession_id());
                Label currentStatusLabel = new Label("Current Status: " + bookingToManage.getStatus().name());
                
                Label newStatusLabel = new Label("Update Status:");
                ComboBox<String> statusField = new ComboBox<>();
                statusField.getItems().addAll("approved", "completed", "cancelled");
                statusField.setValue("approved");
                
                Button saveBtn = new Button("Save");
                saveBtn.setOnAction(e -> {
                    BookingStatus newStatus = BookingStatus.valueOf(statusField.getValue().toUpperCase());
                    bookingToManage.setStatus(newStatus);
                    DBOperations db = new DBOperationsImpl();
                    db.updateOperation(bookingToManage);
                    if(refreshCallback != null){ refreshCallback.run(); }
                    crudStage.close();
                });
                
                Button cancelBtn = new Button("Cancel");
                cancelBtn.setOnAction(e -> crudStage.close());
                
                root.getChildren().addAll(titleLabel, bookingIdLabel, tuteeLabel, sessionLabel, currentStatusLabel, newStatusLabel, statusField, feedbackLabel, saveBtn, cancelBtn);
        }
        
        //scope
            Scene scene = new Scene(root, 420, 400);
            
        //stage management
            crudStage.setTitle(mode.equals("request") ? "Request Session" : "Manage Booking");
            crudStage.setScene(scene);
            crudStage.show();
            
        //css
        scene.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());
    }
    
    public static void main(String[] args){
        launch(args);
    }
}