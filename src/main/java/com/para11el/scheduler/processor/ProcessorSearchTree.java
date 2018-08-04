package com.para11el.scheduler.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

/**
 * Class that creates a search tree from the input graph.
 *
 * @author Jessica Alcantara, Holly Hagenson
 */
public class ProcessorSearchTree {
	
private HashMap<List<Node>, Double> _pathsWithCosts = new HashMap<List<Node>, Double>(); 
	
	private Graph _graph;
	
	private double _cost;
	
	public ProcessorSearchTree(Graph graph) {
		_graph = graph;
		_cost = 0;
	}
	
    /**
     * Returns the list of root nodes of a graph.
     * @return List of root nodes
     * @author Jessica Alcantara
     */
	public List<Node> getRoots() {
		List<Node> roots = new ArrayList<Node>();
		for (Node n : _graph.getNodeSet()) {
			if (n.getInDegree() == 0) {
				roots.add(n);
			}
		}
		return roots;
	}
	
    /**
     * Calculates all possible path of multiple entry and exits of a graph.
     * @return List of paths
     * @author Jessica Alcantara, Holly Hagenson
     */
	public HashMap<List<Node>, Double> calculateStateSpace() {
		List<Node> roots = getRoots();
		
		// Find paths for each entry of a graph
		for (Node entry : roots) {
			List<Node> path = new ArrayList<Node>();
			path.add(entry);
			findPath(path, entry);
		}

		return _pathsWithCosts;
	}
	
    /**
     * Finds all paths from an entry node.
     * @author Jessica Alcantara, Holly Hagenson
     */
	public void findPath(List<Node> path, Node node) {
		// Add path to HashMap with total path cost at leaf node
		if (node.getOutDegree() == 0) {
			// Find cost of path, assuming one processor
			for (Node n : path){
				_cost += Double.parseDouble(n.getAttribute("Weight").toString());
			}
			_pathsWithCosts.put(path, _cost); 
			_cost = 0; 
		} else {
			// Find all possible paths through each adjacent nodes
			for (Edge e : node.getEachLeavingEdge()) {
				List<Node> newPath = new ArrayList<Node>(path);
				newPath.add(e.getTargetNode());
				findPath(newPath,e.getTargetNode());
			}
		}
	}

}
