package com.para11el.scheduler.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * Controller class for the exit pane.
 */
public class ExitPaneController {
    private static boolean _confirmed;
    private static Stage _stage;
    
    /**
     * Handles the abort visualisation action
     * @param event Exit visualisation event
     */
    @FXML
    void abortAction(ActionEvent event) {
        _confirmed = false;
        _stage.close();
    }

    /**
     * Handles the confirm abort visualisation action
     * @param event Confirm exit event
     */
    @FXML
    void confirmAction(ActionEvent event) {
        _confirmed = true;
        _stage.close();
    }

    /**
     * Sets the stage for visualisation
     * @param stage Stage for visualisation
     */
    public static void setStage(Stage stage) {
        _stage = stage;
    }
    
    /**
     * Returns whether the user confirms exiting the application
     * @return Boolean confirmation
     */
    public static boolean getConfirmation() {
        return _confirmed;
    }
}
