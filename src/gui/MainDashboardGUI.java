package gui;

import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainDashboardGUI extends javafx.application.Application{
    @Override 
    public void start(Stage dashboardStage){
        //nodes
        Label lb1 = new Label();
        lb1.setText("Welcome" /* + getrole*/ );
        Label lb2 = new Label();
        lb2.setText("Available sessions");
        Label lb3 = new Label();
        lb3.setText("My requests");
        Button logout = new Button();
        logout.setText("Log out");
        logout.setOnAction(e -> {
            dashboardStage.close();
        });
        
        
        
        //scope
            //Layout Manager 
            VBox root = new VBox();
            root.getChildren().addAll(lb1,lb2,lb3,logout);
            
            //Scene
            Scene scene = new Scene(root,800,600);
     
            
        //Stage Management
        dashboardStage.setTitle("Main Dashboard");
        dashboardStage.setScene(scene);
        dashboardStage.show();
    }
    public static void main(String[] args){
        launch(args);
    }
}    
