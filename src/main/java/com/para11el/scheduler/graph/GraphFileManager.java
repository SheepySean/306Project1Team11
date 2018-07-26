package com.para11el.scheduler.graph;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.stream.file.FileSink;
import org.graphstream.stream.file.FileSinkDOT;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDOT;


import java.io.IOException;


public class GraphFileManager {

        public GraphFileManager(){
        }

        public Graph readGraphFile(String filename, String graphID) throws IOException {
            Graph g = new SingleGraph(graphID);
            FileSource fs = new FileSourceDOT();

            fs.addSink(g);
            fs.readAll(filename);
            return g;
        }

        public void writeGraphFile(String filename, Graph graph, boolean isDigraph) throws IOException {
            FileSink fso = new FileSinkDOT(isDigraph);
            fso.writeAll(graph, filename);
        }
}
