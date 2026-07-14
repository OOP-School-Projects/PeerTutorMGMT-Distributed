package client.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.User;
import server.database.*;
import javafx.geometry.Insets;

public class LoginGUI extends Application {
    @Override
    public void start(Stage loginStage){
        //nodes
            Label lb1 = new Label();
            lb1.setText("Enter your student id");
            TextField idField = new TextField();
            
            Label lb2 = new Label();
            lb2.setText("Enter your password");
            PasswordField passwordField = new PasswordField();
 
            Button loginButton = new Button();
            loginButton.setText("Log in");
            loginButton.setOnAction(e -> {
                //variables to hold text data
                String student_id = idField.getText();
                String password = passwordField.getText();
                //Query the db when the button is pressed 
                //try catch to call my gui to call one site as my template
                try{
                    DBOperationsRemote db = rmi.RMIClient.getStub;
                    User user = (User) db.selectOperation(student_id, "users");
                    if (user != null && user.getPassword().equals(password)){
                      //checks if credentials are correct then loads the dashboard
                      Stage dashboardStage = new Stage();
                      MainDashboardGUI dash = new MainDashboardGUI();
                      dash.setLoggedInUser(user);
                      dash.start(dashboardStage);
                      loginStage.close();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Login Failed");
                        alert.setHeaderText(null);
                        alert.setContentText("Invalid student ID or password. Please try again !");
                        alert.showAndWait();
                    }         
                }catch(exception e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Could not reach server: " + e.getMessage());
                    alert.showAndWait();
                }
                
            });
            Label lb3 = new Label();
            lb3.setText("Don't have an account? Sign up");
            lb3.setOnMouseClicked(e -> {
                Stage signupStage = new Stage();
                SignupGUI sgup = new SignupGUI();
                sgup.start(signupStage);
                loginStage.close();
            });
            lb3.getStyleClass().add("link-label");
        
        //scope
            //Layout manager
            VBox root = new VBox(10);
            root.setPadding(new Insets(30));
            root.getChildren().addAll(lb1, idField, lb2, passwordField, loginButton, lb3);
            
            //Scene
            Scene scene = new Scene(root, 500, 400);
            
        //Stage Management 
            loginStage.setTitle("Login");
            loginStage.setScene(scene);
            loginStage.show();
        //css
        scene.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());
    }
    public static void main(String[] args){
        launch(args);
    }
}
