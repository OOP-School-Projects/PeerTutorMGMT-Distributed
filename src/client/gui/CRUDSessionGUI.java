package client.gui;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import models.*;
import server.database.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CRUDSessionGUI extends Application {
    //set before start() is called
    private User loggedInUser;
    private TutoringSession sessionToEdit;
    private String mode; // "create" or "edit"
    private Runnable refreshCallback;

    public void setLoggedInUser(User user){ this.loggedInUser = user; }
    public void setSessionToEdit(TutoringSession session){ this.sessionToEdit = session; }
    public void setMode(String mode){ this.mode = mode; }
    public void setRefreshCallback(Runnable callback){ this.refreshCallback = callback; }
    
    @Override
    public void start(Stage crudStage){
        //nodes
            Label subjectLabel = new Label("Subject:");
            TextField subjectField = new TextField();
            
            Label dateLabel = new Label("Date:");
            DatePicker datePicker = new DatePicker();
            
            Label timeLabel = new Label("Time (HH:mm):");
            TextField timeField = new TextField();
            timeField.setPromptText("e.g. 14:30");
            
            Label maxStudentsLabel = new Label("Max Students:");
            TextField maxStudentsField = new TextField();
            
            Label statusLabel = new Label("Status:");
            ComboBox<String> statusField = new ComboBox<>();
            statusField.getItems().addAll("available", "booked", "completed", "cancelled");
            statusField.setValue("available");
            
            Label feedbackLabel = new Label();
            
            //if editing, pre-fill the fields
            if(mode.equals("edit") && sessionToEdit != null){
                subjectField.setText(sessionToEdit.getSubject());
                datePicker.setValue(sessionToEdit.getDatetime().toLocalDate());
                timeField.setText(sessionToEdit.getDatetime().toLocalTime().toString());
                maxStudentsField.setText(String.valueOf(sessionToEdit.getMax_students()));
                statusField.setValue(sessionToEdit.getStatus().name().toLowerCase());
            }
            
            Button saveBtn = new Button("Save");
            saveBtn.setOnAction(e -> {
                String subject = subjectField.getText().trim();
                String timeStr = timeField.getText().trim();
                String maxStr = maxStudentsField.getText().trim();
                LocalDate date = datePicker.getValue();
                
                //basic validation
                if(subject.isEmpty() || timeStr.isEmpty() || maxStr.isEmpty() || date == null){
                    feedbackLabel.setText("All fields are required!");
                    return;
                }
                
                //parse date + time into LocalDateTime
                LocalDateTime datetime;
                try{
                    LocalTime time = LocalTime.parse(timeStr);
                    datetime = LocalDateTime.of(date, time);
                } catch(Exception ex){
                    feedbackLabel.setText("Invalid time format. Use HH:mm");
                    return;
                }
                
                int maxStudents;
                try{
                    maxStudents = Integer.parseInt(maxStr);
                } catch(NumberFormatException ex){
                    feedbackLabel.setText("Max students must be a number.");
                    return;
                }
                
                SessionStatus status = SessionStatus.valueOf(statusField.getValue().toUpperCase());
                DBOperations db = new DBOperationsImpl();
                
                if(mode.equals("create")){
                    //session_id = 0 because DB generates it
                    TutoringSession newSession = new TutoringSession(0, subject, loggedInUser.getStudent_id(), datetime, maxStudents, status);
                    db.insertOperation(newSession);
                    if(refreshCallback != null){ refreshCallback.run(); }
                    crudStage.close();
                } else if(mode.equals("edit")){
                    sessionToEdit.setSubject(subject);
                    sessionToEdit.setDatetime(datetime);
                    sessionToEdit.setMax_students(maxStudents);
                    sessionToEdit.setStatus(status);
                    db.updateOperation(sessionToEdit);
                    if(refreshCallback != null){ refreshCallback.run(); }
                    crudStage.close();
                }
            });
            
            Button cancelBtn = new Button("Cancel");
            cancelBtn.setOnAction(e -> crudStage.close());
        
        //scope
            VBox root = new VBox();
            root.getChildren().addAll(subjectLabel, subjectField, dateLabel, datePicker,
                                      timeLabel, timeField, maxStudentsLabel, maxStudentsField,
                                      statusLabel, statusField, feedbackLabel, saveBtn, cancelBtn);
            root.setSpacing(8);
            root.setPadding(new Insets(15));
            
            Scene scene = new Scene(root, 420, 500);
            
        //stage management
            crudStage.setTitle(mode.equals("create") ? "Create Session" : "Edit Session");
            crudStage.setScene(scene);
            crudStage.show();
            
        //css
        scene.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());
    }
    
    public static void main(String[] args){
        launch(args);
    }
}