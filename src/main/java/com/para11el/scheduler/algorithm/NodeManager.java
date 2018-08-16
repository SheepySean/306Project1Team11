package com.para11el.scheduler.algorithm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

public class NodeManager {

	private Graph _graph;
	private int _maxCost;
	private HashMap<Node,Integer> _bottomLevel = new HashMap<Node,Integer>();

	public NodeManager(Graph graph) {
		_graph = graph;
		initialise();
	}

	public void initialise() {
		_graph.nodes().forEach((node)->{
			_maxCost = 0;
			calculateBottomLevel(node, 0);
			_bottomLevel.put(node, _maxCost);
		});
	}

	public void calculateBottomLevel(Node node, int cost) {
		int nodeCost = cost + ((Number)node.getAttribute("Weight")).intValue();
		if (node.getOutDegree() == 0) {
			if (nodeCost > _maxCost) {
				_maxCost = nodeCost;
			}
		} else {
			Iterator<Edge> edges = node.leavingEdges().iterator();
			while(edges.hasNext()){
				Edge currentEdge = edges.next();
				calculateBottomLevel(currentEdge.getTargetNode(), nodeCost);
			}
		}
	}
	
	public void printCriticalPaths() {
		for (Entry<Node, Integer> e : _bottomLevel.entrySet()) {
			System.out.println(e.getKey().getId() + " " + e.getValue());
		}
	}

}
