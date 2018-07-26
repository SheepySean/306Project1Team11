package com.para11el.scheduler.main;

import com.para11el.scheduler.graph.GraphFileManager;
import com.para11el.scheduler.graph.GraphViewManager;
import org.graphstream.graph.Graph;

import java.io.IOException;

public class Scheduler {
    private static Graph _inGraph = null;

    /**
     * Entry point for the program
     * @param args
     */
    public static void main(String[] args) {
        GraphFileManager fileManager = new GraphFileManager();
        try {
            _inGraph = fileManager.readGraphFile("example_graphs/Nodes_11_OutTree.dot", "Example Graph");
        } catch(IOException e) {
            e.printStackTrace();
        }

        GraphViewManager viewManager = new GraphViewManager(_inGraph);
        viewManager.labelGraph();
        //viewManager.unlabelGraph();

        _inGraph.display();
        try {
            fileManager.writeGraphFile("out_example.dot", _inGraph, true);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}
