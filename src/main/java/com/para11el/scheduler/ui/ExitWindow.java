package com.para11el.scheduler.ui;

import javafx.fxml.FXMLLoader;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.control.*;

import com.sun.prism.paint.Color;

import javafx.geometry.*;




public class ExitWindow {

    private static boolean _answer;

    public static boolean display(Stage stage) {
        Stage dialog = new Stage(); // New stage
        dialog.setResizable(false);
        //dialog.initStyle(StageStyle.UNDECORATED);
        // Modal window relative to the GUI's Stage
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);

        try {
            Parent root = FXMLLoader.load(ViewerPaneController.getInstance().getClass().getResource("/fxml/ExitPane.fxml")); // Load in the fxml
            Scene dialogScene = new Scene(root);
            dialogScene.getStylesheets().add("/css/main.css"); // Add the css
            dialog.setScene(dialogScene);
            ExitPaneController.setStage(dialog);
            dialog.showAndWait(); // show the dialog

            if(ExitPaneController.getConfirmation()) {
                return true;
            } else {
                return false;
            }
        } catch(Exception e) {
            return true;
        }
    }
}
