package com.para11el.scheduler.algorithm;

import java.util.ArrayList;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

public class CostFunctionManager {
	
	private List<Node> _longestPath = new ArrayList<Node>(); 
	private int _max; 
	private int _dist; 
	
	public CostFunctionManager() {
		_max = 0; 
		_dist = 0; 
	}
	
	public int bottomLevel(Node node, Graph graph){
		List<Node> path = new ArrayList<Node>(); 
		path.add(node); 
		
		_max = 0; 

		findLongestPath(path, node);
		
		return _max; 
		
	}
	
	private void findLongestPath(List<Node> path, Node source){
		if (source.getOutDegree() == 0){
			for (Node n : path){
				_dist += (double)n.getAttribute("Weight");
			}
			if (_dist > _max){
				_max = _dist;
				_longestPath = path; 
			}
			_dist = 0; 
		} else {
			for (Edge e : source.getLeavingEdgeSet()) {
				List<Node> newPath = new ArrayList<Node>(path); 
				newPath.add(e.getNode1());
				findLongestPath(newPath, e.getNode1()); 
			}
		}
		
	}

}
