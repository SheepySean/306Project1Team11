package com.para11el.scheduler.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.*;
import javafx.scene.*;

/**
 * Viewer class for the exit window.
 * 
 */
public class ExitWindow {

	/**
	 * Displays the exit confirmation window.
	 * @param stage
	 * @return boolean
	 */
    public static boolean display(Stage stage) {
        Stage dialog = new Stage(); // New stage
        dialog.setResizable(false);
        dialog.setTitle("Confirm Scheduler Close");
        // Modal window relative to the GUI's Stage
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);

        try {
            Parent root = FXMLLoader.load(ViewerPaneController.getInstance()
            		.getClass().getResource("/fxml/ExitPane.fxml")); // Load in the fxml
            Scene dialogScene = new Scene(root);
            dialogScene.getStylesheets().add("/css/main.css"); // Add the css
            stage.getIcons().add(new Image(ExitWindow.class
            		.getResourceAsStream("/images/logo-icon.png")));
            dialog.setScene(dialogScene);
            dialog.sizeToScene();
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
