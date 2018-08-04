package com.para11el.scheduler.graph;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.stream.file.*;

import java.io.IOException;

/**
 * Manager class that control the file I/O for the scheduler's graphs
 *
 * @author Sean Oldfield
 */
public class GraphFileManager {

    /**
     * Constructor for GraphFileManager
     */
    public GraphFileManager(){
    }

    /**
     * Read in a specified file in .dot format and return a corresponding graph from it
     * @param filename Name of the .dot file that stores the graph
     * @param graphID Name of the generated graph for titling purposes
     * @return Graph made from the file
     * @throws IOException Thrown if the graph file cannot be read
     * @author Sean Oldfield
     */
    public Graph readGraphFile(String filename, String graphID) throws IOException {
        Graph g = new SingleGraph(graphID);
        FileSource fs = new FileSourceDOT();

        fs.addSink(g);
        fs.readAll(filename); // Read the .dot file
        return g;
    }

    /**
     * Write a specified graph to an output .dot file
     * @param filename Name of the output file
     * @param graph The graph to be written to the output file
     * @param isDigraph True if the graph is a _digraph, false otherwise
     * @throws IOException Thrown if the graph file cannot be written
     * @author Sean Oldfield
     */
    public void writeGraphFile(String filename, Graph graph, boolean isDigraph) throws IOException {
        CustomFileSinkDot fso = new CustomFileSinkDot(true, graph.getId());
        fso.writeAll(graph, filename); // Write to .dot file
    }
}
