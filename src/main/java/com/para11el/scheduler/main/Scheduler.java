package com.para11el.scheduler.main;


import com.para11el.scheduler.algorithm.AStarAlgorithm;
import com.para11el.scheduler.algorithm.DFSAlgorithm;
import com.para11el.scheduler.algorithm.Task;
import com.para11el.scheduler.graph.GraphConstants;
import com.para11el.scheduler.graph.GraphFileManager;
import com.para11el.scheduler.graph.GraphViewManager;
import com.para11el.scheduler.ui.Viewer;
import com.para11el.scheduler.ui.ViewerPaneController;

import javafx.application.Application;
import org.graphstream.graph.Graph;
import org.apache.commons.lang3.StringUtils;
import org.graphstream.ui.fx_viewer.FxViewer;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;


/**
 * Main runner class of the program
 */
public class Scheduler {

	private static Graph _inGraph = null;
	private static String _filename = null;
	private static int _scheduleProcessors = 0;
	private static int _numCores = 0;
	private static String _outputFilename = null;
	private static boolean _visualise = false;
	private static boolean _astar = true;
	private static boolean _timeout = false;
	private static int _timeoutSeconds = 0;

	/**
	 * Entry point for the program
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
        System.setProperty("org.graphstream.ui", "javafx"); // Use JavaFx for GUI

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
		String trueFileName = removeFileExt(Paths.get(_filename).getFileName().toString());
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

		//Check if any of the optional parameters are invalid
		if (invalidOptional()) {
			//Exit if options are invalid
			return; 
		}
		
		//Initialise a timeoutCounter for use if there is a timeout specified
		Thread timeoutCounter = null;
		
		if (_timeout) { //Start a timeout on a new thread
			timeoutCounter = new Thread(() -> {
	            new TimeOut(_timeoutSeconds);
	         });
			timeoutCounter.start();
		}
		
		if(_visualise) { // Start the GUI on an another thread
            new Thread(() -> {
                ViewerPaneController.setViewer(new FxViewer(_inGraph, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD));
                Application.launch(Viewer.class, args);
            }).start();
        } 
        
		//Initialise the output graph
		Graph outputGraph;
		
        if(_astar) {
        	//Searches with A Star Algorithm (default)
        	AStarAlgorithm algorithm = new AStarAlgorithm(_inGraph, _scheduleProcessors);
    		ArrayList<Task> solution = algorithm.buildSolution(); 
    		outputGraph = algorithm.getGraph(solution);
        	
        } else {
        	//Searches with DFS Algorithm
    		DFSAlgorithm algorithm = new DFSAlgorithm(_inGraph, _scheduleProcessors);
    		ArrayList<Task> solution = algorithm.buildSolution();
    		outputGraph = algorithm.getGraph(solution); 	
        }

<<<<<<< HEAD
		//Graph newGraph = solutionSpaceManager.getGraph();
		Graph newGraph = astar.getGraph(solution);
        //Graph newGraph = dfs.getGraph(solution);
		//Graph newGraph = solutionSpaceManager.getGraph();
		//Graph newGraph = astar.getGraph(solution);*/
=======
>>>>>>> 3272cd5f95f6b1d2a848a7d7ef1a4181613f065b
		
		// For viewing the Graph
		GraphViewManager viewManager = new GraphViewManager(_inGraph);
		/*viewManager.labelGraph();
		viewManager.unlabelGraph();*/
		
		
		// Name the file if no specific output name was provided
		if(_outputFilename == null) {
			_outputFilename = removeFileExt(_filename)
					+ "-output" + GraphConstants.FILE_EXT.getValue();
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
	 * @author Sean Oldfield
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
	 * Checks whether the additional features specified when the program is run
	 * are valid in conjunction with one another.
	 * @return whether the additional features are valid
	 * 
	 * @author Rebekah Berriman
	 */
	private static boolean invalidOptional() {
		if (!_astar && (_visualise || (_numCores !=0))) {
			System.out.println("To run the algorithm using DFS, visualisation (-v) and parallelisation (-p) of the search are disabled.");
			return true;
		} else if (_timeout && (_timeoutSeconds==0)) {
			System.out.println("An optimal solution cannot be found in 0 seconds.");
			return true;
		}
		return false; 
	}
}
