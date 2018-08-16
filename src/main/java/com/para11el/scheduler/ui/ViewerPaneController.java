package com.para11el.scheduler.ui;


import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.graphstream.ui.fx_viewer.FxDefaultView;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.layout.Eades84Layout;
import org.graphstream.ui.layout.HierarchicalLayout;
import org.graphstream.ui.layout.springbox.implementations.LinLog;
import org.graphstream.ui.layout.springbox.implementations.SpringBox;
import org.graphstream.ui.view.camera.Camera;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class ViewerPaneController {
    private static FxViewer _viewer;
    private static Camera _camera;
    private FxDefaultView viewPanel;
    private static AnimationTimer _timer;
    private static String _inputFile;
    private static String _outputFile;
    private static String _processors;
    private static String _cores;
    private static long _startTime;

    @FXML
    private AnchorPane graphContainer;


    @FXML
    private Label timerLabel;

    @FXML
    private Text inputFileText;

    @FXML
    private Text processorsText;

    @FXML
    private Text coresText;

    @FXML
    private Text outputFileText;

    /**
     * Initialize some of the GUI's components to initial states
     */
    @FXML
    public void initialize() {
/*        ViewPanel graphViewPanel = _viewer.addDefaultView(false);
        _viewer.enableAutoLayout();
        View view = _viewer.getDefaultView();
        //view.getCamera().setViewPercent(0.5);
        //view.getCamera().setViewCenter(0, 0, 0);
        graphViewPanel.setPreferredSize(new Dimension(200, 200));
        graphPanel = new JPanel();
        graphPanel.add(graphViewPanel);*/
        //view.display((GraphicGraph)_inGraph, true);
        //createAndSetSwingContent(inGraph, graphPanel);

        _viewer.addDefaultView(false, _viewer.newDefaultGraphRenderer());
        _viewer.enableAutoLayout();

        viewPanel = (FxDefaultView) _viewer.getDefaultView();
        viewPanel.setFocusTraversable(true);
        viewPanel.setMaxHeight(357);
        viewPanel.setMinWidth(800);
        viewPanel.requireFocus();
        _camera = _viewer.getDefaultView().getCamera();
        graphContainer.getChildren().add(viewPanel);

        inputFileText.setText(_inputFile);
        coresText.setText(_cores);
        processorsText.setText(_processors);
        outputFileText.setText(_outputFile);

        //long startTime = System.currentTimeMillis();

        _timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long elapsedMillis = System.currentTimeMillis() - _startTime ;
                String timeLabel = String.format("%02d:%02d:%02d",
                        (elapsedMillis / 60000),
                        ((elapsedMillis % 60000) / 1000),
                        ((elapsedMillis % 1000) / 10));
                timerLabel.setText(timeLabel);
            }
        };

        ViewerPaneController.toggleTimer(true);
/*        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(151),
                        new KeyValue(15, 0)));
        timeline.playFromStart();*/

    }

    public static void setViewer(FxViewer viewer) {
        _viewer = viewer;
    }

/*
    private void createAndSetSwingContent(final SwingNode swingNode, JComponent content) {
            SwingUtilities.invokeLater(() -> {

                ViewPanel graphViewPanel = _viewer.addDefaultView(false);
                Layout layout = new SpringBox();
                layout.setQuality(1);
                _viewer.enableAutoLayout(layout);
                View view = _viewer.getDefaultView();
                System.out.println(view.getCamera().getViewCenter());
                //view.getCamera().setViewPercent(0.9D);

                //view.getCamera().setViewCenter(2, 3, 4);
*//*            view.getCamera().setViewPercent(0.5);
            view.getCamera().setViewCenter(0, 0, 0);*//*
                graphViewPanel.setPreferredSize(new Dimension(1200, 520));
                graphPanel = new JPanel();
                graphPanel.add(graphViewPanel);
                swingNode.setContent(graphPanel);
            });
    }*/

    /**
     * Action tied to the menuButton. On "Menu" Click, go to the IntroPane
     * @param event
     */
    @FXML
    private void zoomInAction(ActionEvent event) {
        Platform.runLater(() -> {
            _camera.setViewPercent(Math.max(0.0001f,
                    _camera.getViewPercent() * 0.7f));
        });
    }

    @FXML
    private void zoomOutAction(ActionEvent event) {
        Platform.runLater(() -> {
            _camera.setViewPercent(_camera.getViewPercent() * 1.3f);
        });
    }


    @FXML
    private void resetViewAction(ActionEvent event) {
        Platform.runLater(() -> {
              _camera.resetView();
        });
    }

    @FXML
    private void panUpAction(ActionEvent event) {
        Platform.runLater(() -> {
            double delta = calculateDelta();

            Point3 p = _camera.getViewCenter();
            _camera.setViewCenter(p.x, p.y + delta, 0);
        });
    }


    @FXML
    private void panDownAction(ActionEvent event) {
        Platform.runLater(() -> {
            double delta = calculateDelta();

            Point3 p = _camera.getViewCenter();
            _camera.setViewCenter(p.x, p.y - delta, 0);
        });
    }

    @FXML
    private void panRightAction(ActionEvent event) {
        Platform.runLater(() -> {
            double delta = calculateDelta();

            Point3 p = _camera.getViewCenter();
            _camera.setViewCenter(p.x + delta, p.y, 0);
        });
    }

    @FXML
    private void panLeftAction(ActionEvent event) {
        Platform.runLater(() -> {
            double delta = calculateDelta();

            Point3 p = _camera.getViewCenter();
            _camera.setViewCenter(p.x - delta, p.y, 0);
        });
    }

    @FXML
    void setHierarchicalLayout(ActionEvent event) {
        _viewer.disableAutoLayout();
        _viewer.enableAutoLayout(new HierarchicalLayout());
    }

    @FXML
    void setLinLogLayout(ActionEvent event) {
        _viewer.disableAutoLayout();
        _viewer.enableAutoLayout(new LinLog());
    }

    @FXML
    void setSpringBoxLayout(ActionEvent event) {
        _viewer.disableAutoLayout();
        _viewer.enableAutoLayout(new SpringBox());
    }

    @FXML
    void giveGraphFocus(MouseEvent event) {
        viewPanel.requestFocus();
    }

    public static void setParameters(List<String> parameters) {
        _inputFile = parameters.get(0);
        _processors = parameters.get(1);
        if(parameters.get(2).equals("0")) {
            _cores = "Not Set";
        } else {
            _cores = parameters.get(2);
        }
        _outputFile = parameters.get(3);
        _startTime = Long.parseLong(parameters.get(4));

    }

    private double calculateDelta() {
        return  _camera.getViewPercent() * _camera.getGraphDimension() * 0.1f;
    }

    /**
     * Start or stop the program timer
     * @param enable True if the timer is to be started, false to stop it
     */
    public static void toggleTimer(boolean enable) {
        if(enable) {
            _timer.start();
        } else {
            _timer.stop();
        }
    }
}

