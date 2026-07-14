package client.gui;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.*;
import server.database.*;
import javafx.geometry.Insets;

public class SignupGUI extends Application {
    @Override
    public void start(Stage signupStage){
        //nodes
            Label nameLabel = new Label();
            nameLabel.setText("Name: ");
            TextField nameField = new TextField();
            
            Label idLabel = new Label();
            idLabel.setText("Student ID: ");
            TextField idField = new TextField();
            
            Label emailLabel = new Label();
            emailLabel.setText("Email");
            TextField emailField = new TextField();
            
            Label passwordLabel = new Label();
            passwordLabel.setText("Password:");
            PasswordField passwordField = new PasswordField();
            
            Label roleLabel = new Label();
            roleLabel.setText("Choose a role:");
            ComboBox<String> roleField = new ComboBox<>();
            roleField.getItems().addAll("tutee", "tutor", "admin");
            roleField.setValue("Tutee");
            
            Button signupBtn = new Button();
            signupBtn.setText("Sign up");
            
            Label statusLabel = new Label();
            
            signupBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String id = idField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();
            String roleStr = roleField.getValue();
            
            if (name.isEmpty() || id.isEmpty() || email.isEmpty() || password.isEmpty()) {
                statusLabel.setText("All fields required!");
                return;
            }
             Role role = Role.valueOf(roleStr.toUpperCase());
            // Create Tutee (works for all roles)
            User newUser = new Tutee(id, name, email, password, role, 0, "", 0);
            
            DBOperations db = new DBOperationsImpl();
            db.insertOperation(newUser);
            statusLabel.setText("Signup successful! You can now log in.");
            
            // Auto-close after 2 seconds
            new Thread(() -> {
                try { Thread.sleep(2000); } catch (InterruptedException ex) {}
                javafx.application.Platform.runLater(() -> {
                    signupStage.close();
                    new LoginGUI().start(new Stage());
                });
            }).start();       
            });
            //login redirect if you have an acc
            Label loginRedirectLabel = new Label("Already have an account? Log in");
            loginRedirectLabel.setOnMouseClicked(e -> {
                Stage loginStage = new Stage();
                LoginGUI lgin = new LoginGUI();
                lgin.start(loginStage);
                signupStage.close();
            });
            loginRedirectLabel.getStyleClass().add("link-label"); 
        //scope
            //Layout manager
            VBox root = new VBox(10);
            root.setPadding(new Insets(30));
            root.getChildren().addAll(nameLabel, nameField, idLabel, idField,
                                  emailLabel, emailField, passwordLabel, passwordField,
                                  roleLabel, roleField, signupBtn, statusLabel, loginRedirectLabel);
            
            //Scene
            Scene scene = new Scene(root, 500, 550);
            
        //Stage Management 
            signupStage.setTitle("Sign up");
            signupStage.setScene(scene);
            signupStage.show();
            
         //css
         scene.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());
    }
    public static void main(String[] args){
        launch(args);
    }
}