package com.para11el.scheduler.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.concurrent.atomic.AtomicBoolean;

public class Viewer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        final Popup popup = new Popup(); popup.setX(300); popup.setY(200);
        popup.getContent().addAll(new Circle(25, 25, 50, Color.AQUAMARINE));
        Stage stage = null;
        try{
            ViewerPaneController.setParameters(getParameters().getRaw());
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
