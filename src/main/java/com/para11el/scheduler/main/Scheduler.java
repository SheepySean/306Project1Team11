package com.para11el.scheduler.main;

import com.para11el.scheduler.graph.GraphConstants;
import com.para11el.scheduler.graph.GraphFileManager;
import com.para11el.scheduler.graph.GraphViewManager;
import org.graphstream.graph.Graph;
import java.io.IOException;

/**
 * Main runner class of the program
 */
public class Scheduler {

    private static Graph _inGraph = null;

    private static String _filename = null;
    private static int _scheduleProcessors = 0;
    private static int _numCores = 0;
    private static String _outputFilename = null;
    private static boolean _visualisation = false;

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

        // For viewing the Graph
        GraphViewManager viewManager = new GraphViewManager(_inGraph);
        viewManager.labelGraph();
        //viewManager.unlabelGraph();

        // Name the file if no specific output name was provided
        if(_outputFilename == null) {
            _outputFilename = _filename.substring(0, _filename.lastIndexOf('.'))
                    + "-output" + GraphConstants.FILE_EXT.getValue();
        }

        // Write the output file
        try {
            fileManager.writeGraphFile(_outputFilename,
                    _inGraph, true);
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
            throws ParameterLengthException, NumberFormatException {
        int requiredParams =  Integer.parseInt(ParameterConstants.REQUIRED_PARAMS.getValue());
        // If the required parameters weren't specified
        if(params.length < requiredParams) {
            throw new ParameterLengthException();
        }

        // Read the required parameters
        _filename = params[0];
        _scheduleProcessors = Integer.parseInt(params[1]);

        // Read the additional parameters if there are any
        for(int i = requiredParams; i < params.length; i++) {
            if(params[i].equals(ParameterConstants.PARALLISATION.getValue())) { // -p
                _numCores = Integer.parseInt(params[i+1]);
            } else if(params[i].equals(ParameterConstants.OUTPUT.getValue())) { // -o
                _outputFilename = params[i+1];
            } else if(params[i].equals(ParameterConstants.VISUALISATION.getValue())) { // -v
                _visualisation = true;
            }
        }
    }

}
