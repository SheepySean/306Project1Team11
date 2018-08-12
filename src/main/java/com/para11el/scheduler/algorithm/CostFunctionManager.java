package com.para11el.scheduler.algorithm;

import java.util.ArrayList;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

/**
 * Class to help calculate the cost function of the A* algorithm.
 * 
 * @author Holly Hagenson
 *
 */
public class CostFunctionManager {
	
	private List<Node> _longestPath = new ArrayList<Node>(); 
	private int _max; 
	private int _dist; 
	
	public CostFunctionManager() {
		_max = 0; 
		_dist = 0; 
	}
	
	/**
	 * Calculates the bottom level of a node. 
	 * 
	 * @param node Node to calculate bottom level of
	 * @param graph Input graph that node is a part of
	 * @return int of bottom level value
	 * 
	 * @author Holly Hagenson
	 */
	public int bottomLevel(Node node, Graph graph){
		// Create path with source node
		List<Node> path = new ArrayList<Node>(); 
		path.add(node); 
		
		_max = 0; 

		findLongestPath(path, node);
		
		return _max; 
	}
	
	/**
	 * Finds the longest path from the given node to a leaf node.
	 * 
	 * @param path Current path of nodes
	 * @param source Node to find paths from
	 * 
	 * @author Holly Hagenson
	 */
	private void findLongestPath(List<Node> path, Node source){
		// Calculate cost of current path at leaf node
		if (source.getOutDegree() == 0){
			for (Node n : path){
				_dist += (double)n.getAttribute("Weight");
			}
			// Update maximum path length
			if (_dist > _max){
				_max = _dist;
				_longestPath = path; 
			}
			_dist = 0; 
		} else {
			// Traverse through graph to find all paths from source
			for (Edge e : source.getLeavingEdgeSet()) {
				List<Node> newPath = new ArrayList<Node>(path); 
				newPath.add(e.getNode1());
				findLongestPath(newPath, e.getNode1()); 
			}
		}
		
	}

}
