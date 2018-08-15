package com.para11el.scheduler.ui;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.graphstream.ui.fx_viewer.FxDefaultView;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.layout.HierarchicalLayout;
import org.graphstream.ui.view.camera.Camera;


public class ViewerPaneController {
    private static FxViewer _viewer;
    private static Camera _camera;
    private FxDefaultView viewPanel;

    @FXML
    private AnchorPane graphContainer;

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
        _viewer.enableAutoLayout(new HierarchicalLayout());

        viewPanel = (FxDefaultView) _viewer.getDefaultView();
        viewPanel.setFocusTraversable(true);
        viewPanel.setMaxHeight(357);
        viewPanel.setMinWidth(800);
        viewPanel.requireFocus();
        _camera = _viewer.getDefaultView().getCamera();
        graphContainer.getChildren().add(viewPanel);


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
                    _camera.getViewPercent() * 0.9f));
        });
    }

    @FXML
    private void zoomOutAction(ActionEvent event) {
        Platform.runLater(() -> {
            _camera.setViewPercent(_camera.getViewPercent() * 1.1f);
        });
    }


    @FXML
    private void resetViewAction(ActionEvent event) {
        Platform.runLater(() -> {
              _camera.resetView();
        });
    }

    @FXML
    void giveGraphFocus(MouseEvent event) {
        viewPanel.requestFocus();
    }

}

