package com.para11el.scheduler.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * Controller class for ExitPane.fxml
 *
 * @author Sean Oldfield
 */
public class ExitPaneController {
    private static boolean _confirmed;
    private static Stage _stage;

    @FXML
    void abortAction(ActionEvent event) {
        _confirmed = false;
        _stage.close();


    }

    @FXML
    void confirmAction(ActionEvent event) {
        _confirmed = true;
        _stage.close();

    }

    /**
     * Set stage that the modal dialog is shown on
     * @param stage Stage the dialog is set on
     */
    public static void setStage(Stage stage) {
        _stage = stage;
    }

    /**
     * Get whether or not the GUI should be closed based on user response
     * @return True if the GUI should be close
     */
    public static boolean getConfirmation() {
        return _confirmed;
    }
}
