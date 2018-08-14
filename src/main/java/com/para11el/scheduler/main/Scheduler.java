package com.para11el.scheduler.main;


import com.para11el.scheduler.algorithm.SolutionSpaceManager;

import com.para11el.scheduler.graph.GraphConstants;
import com.para11el.scheduler.graph.GraphFileManager;
import com.para11el.scheduler.graph.GraphViewManager;
import com.para11el.scheduler.ui.Viewer;
import com.para11el.scheduler.ui.ViewerPaneController;
import org.graphstream.graph.Graph;
import org.apache.commons.lang3.StringUtils;
import org.graphstream.stream.ProxyPipe;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.view.View;

import java.io.IOException;
import java.nio.file.Paths;


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
        System.setProperty("org.graphstream.ui", "javafx");
		//System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.javafx.FxGraphRenderer");
		// Read the parameters provided on the command line
		try {
            readParameters(args);
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
		
/*		//Create the SolutionSpace
		SolutionSpaceManager solutionSpaceManager = new SolutionSpaceManager(_inGraph, _scheduleProcessors);
		solutionSpaceManager.initialise();
		
		//Get the graph labeled with the optimal solution
		Graph newGraph = solutionSpaceManager.getGraph();*/
		
		// For viewing the Graph
		GraphViewManager viewManager = new GraphViewManager(_inGraph);
/*		viewManager.labelGraph();
		viewManager.unlabelGraph();*/
		if(_visualise) {
		    ViewerPaneController.setViewer(new FxViewer(_inGraph, org.graphstream.ui.view.Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD));
			Viewer.main(null);
			//view.getCamera().setViewCenter(0, 0,0);

        }
		// Name the file if no specific output name was provided
		if(_outputFilename == null) {
			_outputFilename = removeFileExt(_filename)
					+ "-output" + GraphConstants.FILE_EXT.getValue();
		}
		// Write the output file
		try {
			fileManager.writeGraphFile(_outputFilename,
					_inGraph, true);
            System.out.println("Graph file successfully written to '" + _outputFilename+ "'");
		} catch(IOException e) {
			System.out.println("Unable to write the graph to the file '" + _outputFilename + "'");
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
}
