package com.para11el.scheduler.graph;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.stream.file.FileSink;
import org.graphstream.stream.file.FileSinkDOT;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDOT;
import java.io.IOException;

/**
 * Manager class that control the file I/O for the scheduler's graphs
 *
 * @Author Sean Oldfield
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
     * @throws IOException
     */
    public Graph readGraphFile(String filename, String graphID) throws IOException {
        Graph g = new SingleGraph(graphID);
        FileSource fs = new FileSourceDOT();

        fs.addSink(g);
        fs.readAll(filename);
        return g;
    }

    /**
     * Write a specified graph to an output .dot file
     * @param filename Name of the output file
     * @param graph The graph to be written to the output file
     * @param isDigraph True if the graph is a digraph, false otherwise
     * @throws IOException
     */
    public void writeGraphFile(String filename, Graph graph, boolean isDigraph) throws IOException {
        FileSink fso = new FileSinkDOT(isDigraph);
        fso.writeAll(graph, filename);
    }
}