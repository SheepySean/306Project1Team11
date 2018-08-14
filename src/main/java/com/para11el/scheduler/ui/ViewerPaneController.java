package com.para11el.scheduler.ui;


import com.para11el.scheduler.graph.GraphFileManager;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.graphstream.graph.Graph;
import org.graphstream.ui.fx_viewer.FxDefaultView;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.javafx.FxGraphRenderer;
//import org.graphstream.ui.layout.HierarchicalLayout;
import org.graphstream.ui.layout.Layout;
import org.graphstream.ui.layout.springbox.implementations.LinLog;
import org.graphstream.ui.layout.springbox.implementations.SpringBox;
/*import org.graphstream.ui.swingViewer.DefaultView;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Camera;*/
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.camera.Camera;

import javax.swing.*;
import java.awt.*;

public class ViewerPaneController {
    private static FxViewer _viewer;
    @FXML
    private AnchorPane anchor;



    @FXML
    private AnchorPane graphContainer;

    @FXML
    private Text label;

    private JPanel graphPanel;

    @FXML
    private SwingNode inGraph;
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
        graphContainer.getChildren().add((FxViewPanel)_viewer.getDefaultView());
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
            Camera camera = _viewer.getDefaultView().getCamera();
            camera.setViewPercent(Math.max(9.999999747378752E-5D, camera.getViewPercent() * 0.8999999761581421D));
        });
    }

    @FXML
    private void zoomOutAction(ActionEvent event) {
        Platform.runLater(() -> {
            Camera camera = _viewer.getDefaultView().getCamera();
            camera.setViewPercent(camera.getViewPercent() * 1.100000023841858D);
        });
    }

    @FXML
    private void resetViewAction(ActionEvent event) {
/*        SwingUtilities.invokeLater(() -> {
            Camera camera = _viewer.getDefaultView().getCamera();
            camera.resetView();
        });*/
    }

}

