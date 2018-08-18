package com.para11el.scheduler.ui;

import com.para11el.scheduler.algorithm.Task;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.graphstream.graph.Graph;
import org.graphstream.ui.fx_viewer.FxDefaultView;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.layout.Eades84Layout;
import org.graphstream.ui.layout.HierarchicalLayout;
import org.graphstream.ui.layout.springbox.implementations.LinLog;
import org.graphstream.ui.layout.springbox.implementations.SpringBox;
import org.graphstream.ui.view.camera.Camera;

import java.awt.Color;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


public class ViewerPaneController {
    private static FxViewer _viewer;
    private static List<Task> _schedule;
    private Camera _camera;
    private FxDefaultView viewPanel;

    private static AnimationTimer _timer;
    private static boolean _timeout = false;

    private static String _inputFile;
    private static String _outputFile;
    private static String _processors;
    private static String _cores;
    private static long _startTime;
    private static int _criticalLength;


	private static int _cellWidth;
	private static int _cellHeight = 20;

    private static ViewerPaneController _instance = null;

    private static AtomicBoolean _hasLoaded = new AtomicBoolean(false);
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

	@FXML
	private TilePane colLabelTile;

	@FXML
	private TilePane tile;

	private static TilePane _tile;
    private static TilePane _colLabelTile;
    private static Label _timerLabel;
    
    
	@FXML
	private ScrollPane scrollPane;

    public ViewerPaneController() {}
    /**
     * Initialize some of the GUI's components to initial states
     */
    @FXML
    public void initialize() {
        _instance = getInstance();
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

		// This is a little hacky but allows static reference to the tile pane
        _tile = tile;
        _colLabelTile = colLabelTile;
        _timerLabel = timerLabel;

		setCellSize(Integer.parseInt(_processors));

        initialisePane(_criticalLength);
        initialiseLabel(_criticalLength);


        this.updateSchedule(_schedule);

		// set cell: processor, start time, length of time, colour (string)
/*		setCell("a", 2, 3, 2, generateColours());
		setCell("b", 4, 0, 7, generateColours());

		setCell("b2", 4, 7, 7, generateColours());

		setCell("c", 3, 18, 12, generateColours());

		setCell("b", 1, 0, 1, generateColours());

		setCell("b", 1, 1, 1, generateColours());

		setCell("b", 1, 7, 4, generateColours());*/

		//setCell("e", 6, 7, 4, generateColours());


        // Embed GraphStream graph into the GUI
        _viewer.addDefaultView(false, _viewer.newDefaultGraphRenderer());
        _viewer.enableAutoLayout();
        viewPanel = (FxDefaultView) _viewer.getDefaultView();
        viewPanel.setFocusTraversable(true); // Allow the keyboard shortcuts
        viewPanel.setMaxHeight(357); // So it fits
        viewPanel.setMaxWidth(598);
        viewPanel.requireFocus();
        _camera = _viewer.getDefaultView().getCamera();
        graphContainer.getChildren().add(viewPanel); // Add it to its container

        inputFileText.setText(_inputFile);
        coresText.setText(_cores);
        processorsText.setText(_processors);
        outputFileText.setText(_outputFile);


        // Set the timer for elapsing the program run time
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

        this.toggleTimer(true); // Start the timer
        _hasLoaded.set(true);
    }
    

    public void setViewer(FxViewer viewer) {
        _viewer = viewer;
    }

    /**
     * Zoom out on the graph view
     * @param event
     * @author Sean Oldfield
     */
    @FXML
    private void zoomInAction(ActionEvent event) {
        Platform.runLater(() -> {
            _camera.setViewPercent(Math.max(0.0001f,
                    _camera.getViewPercent() * 0.7f));
        });
    }

    /**
     * Zoom out on the graph view
     * @param event
     * @author Sean Oldfield
     */
    @FXML
    private void zoomOutAction(ActionEvent event) {
        Platform.runLater(() -> {
            _camera.setViewPercent(_camera.getViewPercent() * 1.3f);
        });
    }

    /**
     * Recenter the camera to default on the graph view
     * @param event
     * @author Sean Oldfield
     */
    @FXML
    private void resetViewAction(ActionEvent event) {
        Platform.runLater(() -> {
              _camera.resetView();
        });
    }

	/**
	 * Pan the camera on the graph view up
	 * @param event
	 *
	 * @author Sean Oldfield
	 */
	@FXML
	private void panUpAction(ActionEvent event) {
		Platform.runLater(() -> {
			double delta = calculateDelta();

			Point3 p = _camera.getViewCenter();
			_camera.setViewCenter(p.x, p.y + delta, 0);
		});
	}

	/**
	 * Pan the camera on the graph view down
	 * @param event
	 *
	 * @author Sean Oldfield
	 */
	@FXML
	private void panDownAction(ActionEvent event) {
		Platform.runLater(() -> {
			double delta = calculateDelta();

			Point3 p = _camera.getViewCenter();
			_camera.setViewCenter(p.x, p.y - delta, 0);
		});
	}

	/**
	 * Pan the camera on the graph view right
	 * @param event
	 *
	 * @author Sean Oldfield
	 */
	@FXML
	private void panRightAction(ActionEvent event) {
		Platform.runLater(() -> {
			double delta = calculateDelta();

			Point3 p = _camera.getViewCenter();
			_camera.setViewCenter(p.x + delta, p.y, 0);
		});
	}

	/**
	 * Pan the camera on the graph view left
	 * @param event
	 *
	 * @author Sean Oldfield
	 */
	@FXML
	private void panLeftAction(ActionEvent event) {
		Platform.runLater(() -> {
			double delta = calculateDelta();

			Point3 p = _camera.getViewCenter();
			_camera.setViewCenter(p.x - delta, p.y, 0);
		});
	}

	/**
	 * Set the graph to be have a hierarchical layout
	 * @param event
	 *
	 * @author Sean Oldfield
	 */
	@FXML
	void setHierarchicalLayout(ActionEvent event) {
		_viewer.disableAutoLayout();
		_viewer.enableAutoLayout(new HierarchicalLayout());
	}

	/**
	 * Set the graph to be have a linear logarithm layout
	 * @param event
	 *
	 * @author Sean Oldfield
	 */
	@FXML
	void setLinLogLayout(ActionEvent event) {
		_viewer.disableAutoLayout();
		_viewer.enableAutoLayout(new LinLog());
	}

	/**
	 * Set the graph to be have a spring box layout
	 * @param event
	 *
	 * @author Sean Oldfield
	 */
	@FXML
	void setSpringBoxLayout(ActionEvent event) {
		_viewer.disableAutoLayout();
		_viewer.enableAutoLayout(new SpringBox());
	}

	/**
	 * Give graph view focus when clicked on i.e. allow it to be accessed by keyboard shortcuts
	 * @param event
	 *
	 * @author Sean Oldfield
	 */
	@FXML
	void giveGraphFocus(MouseEvent event) {
		viewPanel.requestFocus();
	}

    /**
     * Set useful parameters for viewing and manipulating in the GUI
     * @param parameters String list of parameters
     * @author Sean Oldfield
     */
    public void setParameters(List<String> parameters) {
        _inputFile = parameters.get(0);
        _processors = parameters.get(1);
        if(parameters.get(2).equals("0")) {
            _cores = "Not Set";
        } else {
            _cores = parameters.get(2);
        }
        _outputFile = parameters.get(3);
        _startTime = Long.parseLong(parameters.get(4));
        _criticalLength = Integer.parseInt(parameters.get(5));


    }

	/**
	 * Calculate delta offset for zoom functions in the GUI
	 * @return The delta offset
	 *
	 * @author Sean Oldfield
	 */
	private double calculateDelta() {
		return  _camera.getViewPercent() * _camera.getGraphDimension() * 0.1f;
	}

    /**
     * Start or stop the program timer
     * @param enable True if the timer is to be started, false to stop it
     * @author Sean Oldfield
     */
    public static void toggleTimer(boolean enable) {
        if(enable) {
            _timer.start();
        } else {
            _timer.stop();
        }
    }

	/**
	 * Sets the cell width in the Schedule view
	 * @param processors The number of processors
	 *
	 * @author Tina Chen
	 */
	private void setCellSize(int processors) {

		int gapSize = (processors - 1)*2;
		_cellWidth = (int)(Math.floor((300 - gapSize)/processors));
	}

	/**
	 * Initialise the tile pane with the number of processors
	 * and the critical path length of a sequential schedule
	 * @param num The time taken for a sequential schedule
	 *
	 * @author Tina Chen, Sean Oldfield
	 */
	private static void initialisePane(int num) {

		Text processorLabel;
		int processorNum = Integer.parseInt(_processors);

		// label the processor columns
		for (int i = 0; i < processorNum; i++) {

			// only label with processor number if over 11 processors due to lack of space
			if (processorNum < 11) {
				processorLabel = new Text("P" + Integer.toString(i+1));
			} else {
				processorLabel = new Text(Integer.toString(i+1));
			}

			_tile.getChildren().add(processorLabel);
		}

		// initialise the tile panes with initial grey colour
		for (int i = 0; i < (num)*processorNum; i++) {
			Pane p = new Pane();
			p.setPrefSize(_cellWidth, _cellHeight);
			p.setStyle("-fx-background-color: #D3D3D3");
			_tile.getChildren().add(p);
		}
	}

	/**
	 * Initialise the row labels to represent the time a task is
	 * scheduled based on the critical path length of a sequential schedule
	 *
	 * @param num The time taken for a sequential schedule
	 *
	 * @author Tina Chen
	 */
	private static void initialiseLabel(int num) {

        Label rowLabel;

        for (int i = 0; i < num + 1; i++) {

            // set blank row label for first row
            if (i == 0) {
                rowLabel = new Label("");
            } else {
                rowLabel = new Label(Integer.toString(i - 1));
            }

            rowLabel.setPrefSize(30, _cellHeight);
            rowLabel.setAlignment(Pos.CENTER_RIGHT);

            _colLabelTile.getChildren().add(rowLabel);
        }
    }

    /**
     * Update the schedule shown in the viewer
     * @param schedule A list of tasks representing a schedule
     *
     * @author Sean Oldfield
     */
    public static void updateSchedule(List<Task> schedule) {
        if(_schedule != null) {
            List<Node> children = _tile.getChildren();

            // Reset the pane to defaults
            for (Node n : children) {
                n.setStyle("-fx-background-color: #D3D3D3");
                if (n instanceof Label) {
                    ((Label) n).setText("");
                }
            }

            // Add the tasks to the view
            for (Task task : schedule) {
                setCell(task.getNode().getId(),
                        task.getProcessor(),
                        task.getStartTime(),
                        task.getWeight(),
                        generateColours());
            }
        }


    }


	/**
	 * Sets a cell with a colour and label to represent a
	 * scheduled task
	 * @param label Task label
	 * @param processor The processor the task is scheduled on
	 * @param startTime Start time of the task
	 * @param length The length of the task
	 * @param colour The colour to render the cell
	 *
	 * @author Tina Chen, Sean Oldfield
	 */
	private static void setCell(String label, int processor, int startTime, int length, String colour) {

		int processorNum = Integer.parseInt(_processors);
		int cell = (processor + processorNum * startTime) - 1 + processorNum;
		for (int i = 0; i < length; i++) {
		   // Node node =
            if (i == 0) {  // if first cell for task, set the task label for it

                // If this cell has been labelled in the past then there is no point relabelling it
                if(_tile.getChildren().get(cell) instanceof Label) {
                    ((Label) _tile.getChildren().get(cell)).setText(label);
                } else {
                    Label nodeLabel = new Label(label);
                    nodeLabel.setPrefSize(_cellWidth, _cellHeight);
                    nodeLabel.setAlignment(Pos.CENTER);
                    _tile.getChildren().set(cell, nodeLabel);
                }

				_tile.getChildren().get(cell).setStyle(colour);
			}

			_tile.getChildren().get(cell).setStyle("-fx-background-color: " + colour);
			cell = cell + processorNum;
		}
	}

	/**
	 * Random colour generator for Schedule view
	 * @return String value representing random hex value
	 *
	 * @author Tina Chen
	 */
	private static String generateColours() {

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

	public static ViewerPaneController getInstance() {
        if(_instance == null) {
            _instance = new ViewerPaneController();
        }
        return _instance;
    }

    public void setSchedule(List<Task> schedule) {
	    _schedule = schedule;
    }

    public static void update() {
	    if(_hasLoaded.get() ) {
            Platform.runLater(() -> {
                updateSchedule(_schedule);
            });
        }
    }
    
    /**
     * Sets the timeout if timeout occurs
     * @param timeout True if timeout
     * 
     * @author Tina Chen
     */
    public static void setTimeout(boolean timeout) {
    	_timeout = timeout;
    	
    	// Change colour of timer label
    	if (_timeout) {
    		_timerLabel.setTextFill(Paint.valueOf("#e50000"));
    	}
    }
    
    /**
     * Returns true if a timeout has occurred
     * @return _timeout True if timeout 
     * 
     * @author Tina Chen
     */
    public static boolean getTimeout() {
    	return _timeout;
    }
    
    
    // is running method, may conflict with Sean's
	public static boolean isRunning() {
		
		return _hasLoaded.get();
	}
}

