package gui;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SignupGUI extends Application {
    @Override
    public void start(Stage signupStage){
        //nodes
            Label lb1 = new Label();
            lb1.setText("Name: ");
            TextField nameField = new TextField();
            Label lb5 = new Label();
            lb5.setText("Student ID: ");
            TextField idField = new TextField();
            Label lb2 = new Label();
            lb2.setText("Email");
            TextField emailField = new TextField();
            Label lb3 = new Label();
            lb3.setText("Password:");
            PasswordField passwordField = new PasswordField();
            Button signupBtn = new Button();
            Label lb4 = new Label();
            lb4.setText("Choose a role");
            ComboBox roleField = new ComboBox();
            roleField.getItems().addAll("tutee", "tutor", "admin");
            signupBtn.setText("Sign up");
            signupBtn.setOnAction(e -> {
                Stage dashboardStage = new Stage();
                MainDashboardGUI dashboard = new MainDashboardGUI();
                dashboard.start(dashboardStage);
                signupStage.close();
            });
        
        //scope
            //Layout manager
            VBox root = new VBox();
            root.getChildren().addAll(lb1, nameField, lb5, idField, lb2, emailField,  lb3, passwordField, lb4, roleField,  signupBtn);
            
            //Scene
            Scene scene = new Scene(root, 500, 400);
            
        //Stage Management 
            signupStage.setTitle("Sign up");
            signupStage.setScene(scene);
            signupStage.show();
            
         //css
         scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
    }
    public static void main(String[] args){
        launch(args);
    }
}