package com.para11el.scheduler.processor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manager class that parses the .dot files for the scheduler's processor
 *
 * @author Jessica Alcantara, Holly Hagenson
 */
public class ProcessorFileManager {
	
	private Map<String,Integer> _nodes = new HashMap<String,Integer>();
	private Map<String,List<String>> _adjacentNodes = new HashMap<String,List<String>>();
	private Map<Edge,Integer> _edges = new HashMap<Edge,Integer>();
	private List<List<String>> _paths = new ArrayList<List<String>>();
	
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
            	
            	// Add adjacent node to map
            	List<String> adj = _adjacentNodes.containsKey(graphElements[0]) ? 
            			_adjacentNodes.get(graphElements[0]) : new ArrayList<String>();
            	adj.add(graphElements[2]);
            	_adjacentNodes.put(graphElements[0], adj);
            }
 
        }
        reader.close();
	}
	
    /**
     * Returns the list of root nodes of a graph.
     * @return List of root nodes
     * @author Jessica Alcantara, Holly Hagenson
     */
	public List<String> getRoots() {
		List<String> roots = new ArrayList<String>(_nodes.keySet());
		for (String s : _nodes.keySet()) {
			for (Edge e : _edges.keySet()) {
				if (e.getTarget().equals(s)) {
					roots.remove(s);
				}
			}
		}

		return roots;
	}
	
    /**
     * Calculates all possible path of multiple entry and exits of a graph.
     * @return List of paths
     * @author Jessica Alcantara, Holly Hagenson
     */
	public List<List<String>> calculateStateSpace() {
		List<String> roots = getRoots();
		
		// Find paths for each entry of a graph
		for (String entry : roots) {
			List<String> path = new ArrayList<String>();
			path.add(entry);
			findPath(path, entry);
		}

		return _paths;
	}
	
    /**
     * Finds all paths from an entry node.
     * @author Jessica Alcantara, Holly Hagenson
     */
	public void findPath(List<String> path, String node) {
		if (_adjacentNodes.keySet().contains(node)) {
			List<String> adj = _adjacentNodes.get(node);
			
			// Find all possible paths through each adjacent nodes
			for (String n : adj) {
				List<String> newPath = new ArrayList<String>(path);
				newPath.add(n);
				findPath(newPath,n);
			}
			
		// Add path to list at leaf node 
		} else {
			_paths.add(path);
		}
	}

}
