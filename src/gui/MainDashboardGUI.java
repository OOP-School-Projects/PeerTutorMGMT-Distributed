package gui;

import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainDashboardGUI extends javafx.application.Application{
    @Override 
    public void start(Stage dashboardStage){
        //nodes
        Label lb = new Label();
        lb.setText("Hallo!");
        TextField tf = new TextField();
        Button btn = new Button();
        
        
        //scope
            //Layout Manager 
            VBox root = new VBox();
            root.getChildren().addAll(lb,tf,btn);
            
            //Scene
            Scene scene = new Scene(root,800,600);
     
            
        //Stage Management
        dashboardStage.setScene(scene);
        dashboardStage.show();
    }
    public static void main(String[] args){
        launch(args);
    }
}    
