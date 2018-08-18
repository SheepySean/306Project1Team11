package com.para11el.scheduler.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

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

    public static void setStage(Stage stage) {
        _stage = stage;
    }
    public static boolean getConfirmation() {
        return _confirmed;
    }
}
