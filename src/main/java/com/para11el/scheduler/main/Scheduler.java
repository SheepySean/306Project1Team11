package com.para11el.scheduler.main;

import com.para11el.scheduler.algorithm.SolutionSpaceManager;
import com.para11el.scheduler.algorithm.Task;
import com.para11el.scheduler.graph.GraphConstants;
import com.para11el.scheduler.graph.GraphFileManager;
import com.para11el.scheduler.graph.GraphViewManager;
import org.graphstream.graph.Graph;
import java.io.IOException;
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

	/**
	 * Entry point for the program
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		// Read the parameters provided on the command line
		try {
			Scheduler.readParameters(args);
		} catch (ParameterLengthException e) {
			System.out.println("At least 2 parameters required");
			return; // Exit
		} catch (NumberFormatException e) {
			System.out.println("Please ensure that processors and cores specified " +
					"are numbers");
			return;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Please ensure that number of cores is specified");
			return;
		}

		// Read the supplied graph file in
		GraphFileManager fileManager = new GraphFileManager();
		try {
			_inGraph = fileManager.readGraphFile(_filename,
					"Example Graph");
		} catch(IOException e) {
			System.out.println("Cannot find the specified input file '" + _filename +
					"'");
			return;
		}
		
		//THIS IS JUST FOR TESTING JUST FOR NOWS - WILL SPLIT INTO SEPARATE THINGS!!
		//Create the SolutionSpace
		SolutionSpaceManager s = new SolutionSpaceManager(_inGraph, 1);
		//ArrayList<Task> optimalSolution = s.getOptimal(); // getOptimal solution
		//Graph newGraph = s.labelGraph(optimalSolution); //create a new graph with optimal
		Graph newGraph = s.getGraph();
		
		// For viewing the Graph
		GraphViewManager viewManager = new GraphViewManager(_inGraph);
		viewManager.labelGraph();

		if(_visualise) {
			_inGraph.display();
		}
		// Name the file if no specific output name was provided
		if(_outputFilename == null) {
			_outputFilename = _filename.substring(0, _filename.lastIndexOf('.'))
					+ "-output" + GraphConstants.FILE_EXT.getValue();
		}
		// Write the output file
		try {
			fileManager.writeGraphFile(_outputFilename,
					newGraph, true);
		} catch(IOException e) {
			System.out.println("Unable to write the graph to the file '" + _filename +
					"'");
		}
	}

	/**
	 * Read supplied command line arguments into the scheduler fields
	 * @param params Command line arguments
	 * @throws ParameterLengthException Thrown if less than the required number of parameters is provided
	 * @throws NumberFormatException Thrown if expected number parameters are not numbers
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
			case ParameterType.PARRALISE:
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
			}
		}
	}
}
