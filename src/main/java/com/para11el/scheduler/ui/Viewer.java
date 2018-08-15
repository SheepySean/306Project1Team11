package com.para11el.scheduler.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicBoolean;

public class Viewer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Stage stage = null;
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/ViewerPane.fxml")); // Load the fxml pane
            Scene scene = new Scene(root);

            scene.getStylesheets().add("/css/main.css"); // Add the css
            stage = primaryStage;
            // Add logo to the GUI
            stage.getIcons().add(new Image(Viewer.class.getResourceAsStream("/images/logo-icon.png")));
            stage.setScene(scene);
            //stage.setResizable(false);
            stage.setTitle("Para11el - Task Scheduler");
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop(){
        Platform.exit();
        System.exit(0);
    }

}
