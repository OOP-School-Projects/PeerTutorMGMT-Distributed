package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginGUI extends Application {
    @Override
    public void start(Stage loginStage){
        //nodes
            Label lb1 = new Label();
            lb1.setText("Enter your email");
            TextField emailField = new TextField();
            Label lb2 = new Label();
            lb2.setText("Enter your password");
            PasswordField passwordField = new PasswordField();
            Button loginButton = new Button();
            loginButton.setText("Log in");
            loginButton.setOnAction(e -> {
                Stage dashboardStage = new Stage();
                MainDashboardGUI dash = new MainDashboardGUI();
                dash.start(dashboardStage);
                loginStage.close();
            });
            Label lb3 = new Label();
            lb3.setText("Don't have an account? Sign up");
            Button redirectSignup = new Button();
            redirectSignup.setText("Sign up");
            redirectSignup.setOnAction(e -> {
                Stage signupStage = new Stage();
                SignupGUI sgup = new SignupGUI();
                sgup.start(signupStage);
                loginStage.close();
            });
        
        //scope
            //Layout manager
            VBox root = new VBox();
            root.getChildren().addAll(lb1, emailField, lb2, passwordField, loginButton, lb3, redirectSignup);
            
            //Scene
            Scene scene = new Scene(root, 500, 400);
            
        //Stage Management 
            loginStage.setTitle("Login");
            loginStage.setScene(scene);
            loginStage.show();
    }
    public static void main(String[] args){
        launch(args);
    }
}
