package com.para11el.scheduler.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

public class CostFunctionManager {
	
	private ArrayList<Node> _exitNodes = new ArrayList<Node>(); 
	private HashSet<Node> _settledNodes; 
	private HashSet<Node> _unsettledNodes; 
	
	public void bottomLevel(Node node, Graph g){
		
		getExitNodes(g); 
		
		_settledNodes = new HashSet<Node>();
		_unsettledNodes = new HashSet<Node>();
		
		//node.setVisited(true); 
		
		for (Node n : _exitNodes){
			findLongestPath(node, n);
		}
		
		
	}
	
	public void getExitNodes (Graph g){
		Collection<Node> nodes = g.getNodeSet();
		for (Node n : nodes){
			if (n.getOutDegree() == 0){
				_exitNodes.add(n); 
			}
		}
	}
	
	private void findLongestPath(Node source, Node target){
		int max, dist = 0; 
		
		for (Edge e : source.getLeavingEdgeSet()){
			//if (e.getNode0().visited() == false){
				dist = e.getAttribute("Weight"); 
			//}
		}
	}

}
