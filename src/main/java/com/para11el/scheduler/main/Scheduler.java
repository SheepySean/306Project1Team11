package com.para11el.scheduler.main;

import com.para11el.scheduler.graph.GraphConstants;
import com.para11el.scheduler.graph.GraphFileManager;
import com.para11el.scheduler.graph.GraphViewManager;
import org.graphstream.graph.Graph;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.IOException;

public class Scheduler {

    private static Graph _inGraph = null;

    private static String _filename = null;
    private static int _scheduleProcessors = 0;
    private static int _numCores = 0;
    private static String _outputFilename = null;
    private static boolean _visualisation = false;

    /**
     * Entry point for the program
     * @param args
     */
    public static void main(String[] args) {
        try {
            Scheduler.readParameters(args);
        } catch (ParameterLengthException e) {
            System.out.println("At least 2 parameters required");
            return;
        } catch (NumberFormatException e) {
            System.out.println("Please ensure that processors and cores specified " +
                    "are numbers");
            return;
        }

        GraphFileManager fileManager = new GraphFileManager();
        try {
            _inGraph = fileManager.readGraphFile(_filename,
                    "Example Graph");
        } catch(IOException e) {
            System.out.println("Cannot find the specified input file '" + _filename +
            "'");
            return;
        }

        GraphViewManager viewManager = new GraphViewManager(_inGraph);
        viewManager.labelGraph();
        //viewManager.unlabelGraph();

        // Name the file if no specific output name was provided
        if(_outputFilename == null) {
            _outputFilename = _filename.substring(0, _filename.lastIndexOf('.'))
                    + "-output" + GraphConstants.FILE_EXT.getValue();
        }

        try {
            fileManager.writeGraphFile(_outputFilename,
                    _inGraph, true);
        } catch(IOException e) {
            System.out.println("Unable to write the graph to the file '" + _outputFilename +
                    "'");
            return;
        }
    }

    private static void readParameters(String[] params)
            throws ParameterLengthException, NumberFormatException {
        if(params.length < Integer.parseInt(ParameterConstants.REQUIRED_PARAMS.getValue())) {
            throw new ParameterLengthException();
        }
        _filename = params[0];
        _scheduleProcessors = Integer.parseInt(params[1]);

        for(int i=2; i < params.length; i++) {
            if(params[i].equals(ParameterConstants.PARALLISATION.getValue())) {
                _numCores = Integer.parseInt(params[i+1]);
            } else if(params[i].equals(ParameterConstants.OUTPUT.getValue())) {
                _outputFilename = params[i+1];
            } else if(params[i].equals(ParameterConstants.VISUALISATION.getValue())) {
                _visualisation = true;
            }
        }
    }

}
