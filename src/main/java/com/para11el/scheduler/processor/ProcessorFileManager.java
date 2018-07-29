package com.para11el.scheduler.processor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Manager class that parses the .dot files for the scheduler's processor
 *
 * @author Jessica Alcantara, Holly Hagenson
 */
public class ProcessorFileManager {
	
	private Map<String,Integer> _nodes = new HashMap<String,Integer>();
	private Map<Edge,Integer> _edges = new HashMap<Edge,Integer>();
	
    /**
     * Reads in .dot file and extracts nodes and edges (currently only works 
     * for specific .dot formats).
     * @param filename Name of the .dot file that stores the graph
     * @throws IOException Thrown if the graph file cannot be read
     * @author Jessica Alcantara, Holly Hagenson
     */
	public void readFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        
        // Skip first line of .dot file
        reader.readLine();
        
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            
            // Add nodes to map
            String[] graphElements = line.trim().split("\\s+");
            if (graphElements.length == 2) {
            	int weight = Integer.parseInt(graphElements[1].replaceAll("[^0-9]", "")); 
            	_nodes.put(graphElements[0], weight);
            }
            
            // Add edges to map
            if (graphElements.length == 4) {
            	int cost = Integer.parseInt(graphElements[3].replaceAll("[^0-9]", ""));
            	Edge edge = new Edge(graphElements[0], graphElements[2], cost);
            	_edges.put(edge,cost);
            }
 
        }
        reader.close();
	}

}
