package com.para11el.scheduler.ui;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import org.graphstream.ui.fx_viewer.FxDefaultView;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.layout.Eades84Layout;
import org.graphstream.ui.layout.HierarchicalLayout;
import org.graphstream.ui.layout.springbox.implementations.LinLog;
import org.graphstream.ui.layout.springbox.implementations.SpringBox;
import org.graphstream.ui.view.camera.Camera;

import java.util.List;
import java.util.Random;


public class ViewerPaneController {
    private static FxViewer _viewer;
    private static Camera _camera;
    private FxDefaultView viewPanel;

    private static String _inputFile;
    private static String _outputFile;
    private static String _processors;
    private static String _cores;
    
    private int _cellWidth;
    private int _cellHeight = 20;

    @FXML
    private AnchorPane graphContainer;

    @FXML
    private Text inputFileText;

    @FXML
    private Text processorsText;

    @FXML
    private Text coresText;

    @FXML
    private Text outputFileText;
    
    @FXML
    private TilePane colLabelTile;
    
    @FXML
    private TilePane tile;

    @FXML
    private ScrollPane scrollPane;


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
    	
    	scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    	

    	
    	setCellSize(Integer.parseInt(_processors));
    	
    	initialisePane(30);
    	initialiseLabel(30);


		// set cell: processor, start time, length of time, colour (string)
		setCell("a", 2, 3, 2, generateColours());
		setCell("b", 4, 0, 7, generateColours());

		setCell("b2", 4, 7, 7, generateColours());

		setCell("c", 3, 1, 12, generateColours());

		setCell("b", 1, 0, 1, generateColours());

		setCell("b", 1, 1, 1, generateColours());

		setCell("b", 1, 7, 4, generateColours());
		
		
		// testing more processors
    	
    	

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

    }

    private double calculateDelta() {
        return  _camera.getViewPercent() * _camera.getGraphDimension() * 0.1f;
    }
    
    // ====================================
    
    private void initialisePane(int num) {

		for (int i = 0; i < num*Integer.parseInt(_processors); i++) {
			Pane p = new Pane();
			p.setPrefSize(_cellWidth, _cellHeight);
			p.setStyle("-fx-background-color: #D3D3D3");
			tile.getChildren().add(p);
		}
	}
    
    private void initialiseLabel(int num) {
    	
    	for (int i = 1; i < num+1; i++) {
    		
    		Label colLabel = new Label(Integer.toString(i));
    		colLabel.setPrefSize(30, _cellHeight);
    		colLabel.setAlignment(Pos.CENTER_RIGHT);
    		
    		colLabelTile.getChildren().add(colLabel);
			
		}
    	
    }
    
    
    private void setCell(String label, int processor, int startTime, int length, String colour) {

		int cell = (processor + Integer.parseInt(_processors) * startTime) -1;

		for (int i = 0; i < length; i++) {

			if (i == 0) {

				Label nodeLabel = new Label(label);
				nodeLabel.setPrefSize(_cellWidth, _cellHeight);
				nodeLabel.setAlignment(Pos.CENTER);
				tile.getChildren().set(cell, nodeLabel);
				tile.getChildren().get(cell).setStyle(colour);
			}
			tile.getChildren().get(cell).setStyle("-fx-background-color: " + colour);
			cell = cell + Integer.parseInt(_processors);
		}
	}

	private String generateColours() {

		Random rand = new Random();

		float r = rand.nextFloat();
		float g = rand.nextFloat() / 2f;
		float b = rand.nextFloat() / 2f;

		java.awt.Color randomColor = new java.awt.Color(r, g, b);
		String hex = Integer.toHexString(randomColor.getRGB() & 0xffffff);

		if (hex.length() < 6) {
			hex = "0" + hex;
		}
		hex = "#" + hex;

		return hex;
	}
	
	private void setCellSize(int processors) {
		
		int gapSize = (processors - 1)*2;
		_cellWidth = (int)(Math.floor((300 - gapSize)/processors));
	}
}

