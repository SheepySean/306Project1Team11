package com.para11el.scheduler.ui;


import com.para11el.scheduler.graph.GraphFileManager;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.graphstream.graph.Graph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import java.awt.*;

public class ViewerPaneController {
    private static Viewer _viewer;
    @FXML
    private AnchorPane pane;

    @FXML
    private Text label;



    @FXML
    private SwingNode swing;
    /**
     * Initialize some of the GUI's components to initial states
     */
    @FXML
    public void initialize() {
        ViewPanel view = _viewer.addDefaultView(false);
        _viewer.enableAutoLayout();
        view.setPreferredSize(new Dimension(1200, 720));
        JPanel frame = new JPanel();
        frame.setLayout(new BorderLayout());
        frame.add(view, BorderLayout.SOUTH);
        //view.display((GraphicGraph)_inGraph, true);
        swing.setContent(frame);
    }

    public static void setViewer(Viewer viewer) {
        _viewer = viewer;
    }
}

