package com.para11el.scheduler.main;


import com.para11el.scheduler.algorithm.AStarAlgorithm;
import com.para11el.scheduler.algorithm.DFSAlgorithm;
import com.para11el.scheduler.algorithm.DFSInitialiser;
import com.para11el.scheduler.algorithm.Task;
import com.para11el.scheduler.graph.GraphConstants;
import com.para11el.scheduler.graph.GraphFileManager;
import com.para11el.scheduler.graph.GraphViewManager;
import com.para11el.scheduler.ui.ViewerPaneController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.graphstream.graph.Graph;
import org.apache.commons.lang3.StringUtils;
import org.graphstream.graph.Node;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.logging.LogManager;
import java.util.ArrayList;
import java.util.ArrayList;


/**
 * Main runner class of the program
 *
 * @author Sean Oldfield
 */
public class Scheduler extends Application {

	private static Graph _inGraph = null;
	private static String _filename = null;
	private static int _scheduleProcessors = 0;
	private static int _numCores = 1;
	private static String _outputFilename = null;
	private static boolean _visualise = false;
	private static boolean _astar = true;
	private static boolean _timeout = false;
	private static int _timeoutSeconds = 0;

	/**
	 * Entry point for the program
	 * @param args Command line arguments
	 *
	 * @author Sean Oldfield
	 */
	public static void main(String[] args) {
		System.setProperty("org.graphstream.ui", "javafx"); // Use JavaFx for GUI
		System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer"); // CSS Styling

		long startTime = System.currentTimeMillis(); // Start time of the program

		LogManager.getLogManager().reset(); // Prevent GraphStream logging to the command line

		// Read the parameters provided on the command line
		try {
			readParameters(args);
		} catch (ParameterLengthException e) {
			System.out.println("At least 2 parameters required");
			return; // Exit
		} catch (NumberFormatException e) {
			System.out.println("Please ensure that processors and cores specified " +
					"are numbers");
			return; //Exit
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Please ensure that number of cores is specified");
			return; //Exit
		}

		// Get just the name of the graph file, removing dir paths and extensions
		String trueFileName = removeFileExt(getFilenameNoDirectory(_filename));
		// Name the graph "outputFilename"
		String graphName = "output" + StringUtils.capitalize(trueFileName);

		// Read the supplied graph file in
		GraphFileManager fileManager = new GraphFileManager();
		try {
			_inGraph = fileManager.readGraphFile(_filename,
					graphName);
			System.out.println("Found and loaded the graph file '" + _filename + "'");
		} catch(IOException e) {
			System.out.println("Cannot find the specified input file '" + _filename + "'");
			return;
		}

		// Name the file if no specific output name was provided
		if(_outputFilename == null) {
			_outputFilename = removeFileExt(_filename)
					+ "-output" + GraphConstants.FILE_EXT.getValue();
		}

		/*
		//Check if any of the optional parameters are invalid
		if (invalidOptional()) {
			//Exit if options are invalid
			return;
		}
		 */

		//Initialise a timeoutCounter for use if there is a timeout specified
		Thread timeoutCounter = null;

		if (_timeout) { //Start a timeout on a new thread
			timeoutCounter = new Thread(() -> {
				new TimeOut(_timeoutSeconds);
			});
			timeoutCounter.start();
		}


		if(_visualise) { // Start the GUI on an another thread
			int critLength = new AStarAlgorithm().calculateTotalWeight(_inGraph.nodes());
			String[] guiArgs = { // Parameters needed by the GUI
					_filename,
					Integer.toString(_scheduleProcessors),
					Integer.toString(_numCores),
					getFilenameNoDirectory(_outputFilename),
					Long.toString(startTime),
					Integer.toString(critLength)
			};

			// Start the GUI on another thread
			FxViewer viewer = new FxViewer(_inGraph, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
			ViewerPaneController.getInstance().setViewer(viewer);

			Thread t = new Thread(() -> {
				launch(guiArgs);
			});
			t.start();
			/*
            List<Task> mockTasks = new ArrayList<Task>();
			for(int i = 0; i < 1000000; i++) {
                _inGraph.nodes().forEach((node) -> {
                    mockTasks.add(new Task(node, (int) Math.floor(Math.random() * 30) + 1, (int) Math.floor(Math.random() * 4)));

                });
                ViewerPaneController.getInstance().setSchedule(mockTasks);

                try{
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch(Exception e) {}
                ViewerPaneController.update();
                mockTasks.clear();
            }
			 */

			// For viewing the Graph
			GraphicGraph viewGraph = viewer.getGraphicGraph();
			// Get css for decorating the graph
			URL url = Scheduler.class.getResource("/css/graph.css");
			// Add the css
			viewGraph.setAttribute("ui.stylesheet", "url('" + url + "')");
			GraphViewManager viewManager = new GraphViewManager(_inGraph);
			viewManager.labelGraph(); // Label nodes and edges
			//_inGraph.getNode("0").setAttribute("ui.class", "some");

		}

		//Initialise the output graph
		Graph outputGraph;

		if(_astar) {
			//Searches with A Star Algorithm (default)
			AStarAlgorithm algorithm = new AStarAlgorithm(_inGraph, _scheduleProcessors);
			ArrayList<Task> solution = algorithm.buildSolution();
			ViewerPaneController.getInstance().setSchedule(solution);
			if(ViewerPaneController.isRunning()) {
				ViewerPaneController.update();
			}
			outputGraph = algorithm.getGraph(solution);

		} else {
			//Searches with DFS Algorithm

			DFSInitialiser dfs = new DFSInitialiser(_inGraph, _scheduleProcessors, _numCores);
			ArrayList<Task> solution = dfs.buildSolution();
			
			// Stop timer when the optimal solution is found
			ViewerPaneController.getInstance();
			if (!ViewerPaneController.getTimeout() && 
					ViewerPaneController.isRunning()) {
				ViewerPaneController.toggleTimer(false);
				ViewerPaneController.setLabelFinish();
			}
			outputGraph = dfs.getGraph(solution);
		}



		// Write the output file
		try {
			fileManager.writeGraphFile(_outputFilename,
					outputGraph, true);
			System.out.println("Graph file successfully written to '" + _outputFilename+ "'");
		} catch(IOException e) {
			System.out.println("Unable to write the graph to the file '" + _outputFilename + "'");
		}

		//Interrupt the timeout thread and stop it
		if (_timeout) {
			timeoutCounter.interrupt();
		}

		return;

	}

	/**
	 * Read supplied command line arguments into the scheduler fields
	 * @param params Command line arguments
	 * @throws ParameterLengthException Thrown if less than the required number of parameters is provided
	 * @throws NumberFormatException Thrown if expected number parameters are not numbers
	 *
	 * @author Sean Oldfield, Tina Chen, Rebekah Berriman
	 */
	private static void readParameters(String[] params)
			throws ParameterLengthException, NumberFormatException, ArrayIndexOutOfBoundsException {
		// If the required parameters weren't specified
		if(params.length < ParameterType.REQUIRED_PARAMS) {
			throw new ParameterLengthException();
		}

		// Read the required parameters
		_filename = params[0];
		_scheduleProcessors = Integer.parseInt(params[1]);

		// Read the additional parameters if there are any
		for(int i = ParameterType.REQUIRED_PARAMS; i < params.length; i++) {
			switch (params[i]) {
			case ParameterType.PARALLELISE:
				_numCores = Integer.parseInt(params[i + 1]);
				break;
			case ParameterType.VISUALISE:
				_visualise = true;
				break;
			case ParameterType.OUTPUT_FILE:
				try {
					_outputFilename = params[i + 1];
				} catch (ArrayIndexOutOfBoundsException e) {}
				break;
			case ParameterType.DFS:
				_astar = false;
				break;
			case ParameterType.TIME_OUT:
				_timeout = true;
				_timeoutSeconds = Integer.parseInt(params[i + 1]);
				break;
			}
		}
	}

	/**
	 * Remove a . extension from a file name
	 * @param filename Filename to remove the .dot from
	 * @return Name of the file without the extension
	 *
	 * @author Sean Oldfield
	 */
	private static String removeFileExt(String filename) {
		try {
			return filename.substring(0, filename.lastIndexOf('.'));
		} catch(StringIndexOutOfBoundsException e) {
			return filename;
		}
	}

	/**
	 * Remove any parent directories from a file path
	 * @param path File path to a file
	 * @return The name of the file without directories
	 * @author Sean Oldfield
	 */
	private static String getFilenameNoDirectory(String path) {
		return Paths.get(path).getFileName().toString();
	}

	/**
	 * Checks whether the additional features specified when the program is run
	 * are valid in conjunction with one another.
	 * @return whether the additional features are valid
	 *
	 * @author Rebekah Berriman
	 */
    private static boolean invalidOptional() {
        if (_timeout && (_timeoutSeconds==0)) {
            System.out.println("An optimal solution cannot be found in 0 seconds.");
            return true;
        }
        return false;
    }




	@Override
	public void start(Stage primaryStage) {

		final Popup popup = new Popup(); popup.setX(300); popup.setY(200);
		popup.getContent().addAll(new Circle(25, 25, 50, Color.AQUAMARINE));
		Stage stage = null;
		try{
			List<String> params = getParameters().getRaw();
			ViewerPaneController.getInstance().setParameters(params);
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/ViewerPane.fxml")); // Load the fxml pane
			Scene scene = new Scene(root);
			scene.getStylesheets().add("/css/main.css"); // Add the css
			stage = primaryStage;
			// Add logo to the GUI
			stage.getIcons().add(new Image(Scheduler.class.getResourceAsStream("/images/logo-icon.png")));
			stage.setScene(scene);
			//stage.setResizable(false);
			stage.setTitle("Para11el | Task Scheduler | " + _filename);

			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop(){
		Platform.exit();
		System.exit(0);
	}

}
