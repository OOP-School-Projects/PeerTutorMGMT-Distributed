/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author edith
 */
public class CRUDSessionGUI extends javafx.application.Application{
    @Override 
    public void start(Stage primaryStage){
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
        primaryStage.setScene(scene);
        primaryStage.show();
        //css
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
    }
    public static void main(String[] args){
        launch(args);
    }
}    
