package com.para11el.scheduler.main;

import com.para11el.scheduler.graph.GraphConstants;
import com.para11el.scheduler.graph.GraphFileManager;
import com.para11el.scheduler.graph.GraphViewManager;
import org.graphstream.graph.Graph;

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
        Scheduler.readParameters(args);

        GraphFileManager fileManager = new GraphFileManager();
        try {
            _inGraph = fileManager.readGraphFile(GraphConstants.GRAPH_DIRECTORY.getValue() +
                    "/" + GraphConstants.SAMPLE_INPUT_FILE.getValue() + GraphConstants.FILE_EXT.getValue(),
                    "Example Graph");
        } catch(IOException e) {
            e.printStackTrace();
        }

        GraphViewManager viewManager = new GraphViewManager(_inGraph);
        viewManager.labelGraph();
        //viewManager.unlabelGraph();

        _inGraph.display();
        try {
            fileManager.writeGraphFile(GraphConstants.OUTPUT_PREFIX.getValue() +
                    GraphConstants.SAMPLE_INPUT_FILE.getValue() + GraphConstants.FILE_EXT.getValue(),
                    _inGraph, true);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static void readParameters(String[] params) {
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
