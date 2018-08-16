package com.para11el.scheduler.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

public class NodeManager {
	
	private Graph _graph;
	private HashMap<Node,Integer> _criticalPath = new HashMap<Node,Integer>();
	
	public NodeManager(Graph graph) {
		_graph = graph;
		initialise();
	}
	
	public void initialise() {
		_graph.nodes().forEach((node)->{
			calculateBottomLevel(node);
		});
	}
	
	public void calculateBottomLevel(Node node) {
		if (node.getOutDegree() == 0) {
			_criticalPath.put(node, ((Number)node.getAttribute("Weight")).intValue());
		} else {
			/*Iterator<Edge> edges = source.leavingEdges().iterator();
			while(edges.hasNext()){
				Edge currentEdge = edges.next(); 
				if (currentEdge.getNode0().equals(source)){
					List<Node> newPath = new ArrayList<Node>(path); 
					newPath.add(currentEdge.getNode1());
					findLongestPath(newPath, currentEdge.getNode1());					
				}
			}*/
		}
		
	}

}
