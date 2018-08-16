package com.para11el.scheduler.algorithm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

/**
 * Manager class for Nodes.
 * 
 * @author Jessica_Alcantara
 */
public class NodeManager {

	private Graph _graph;
	private int _maxCost;
	private HashMap<Node,Integer> _bottomLevel = new HashMap<Node,Integer>();

	/**
	 * Constructor for NodeManager
	 * @param graph Input Graph
	 * 
	 * @author Jessica_Alcantara
	 */
	public NodeManager(Graph graph) {
		_graph = graph;
		initialise();
	}

	/**
	 * Calculates the bottom level value for every node in the graph
	 * 
	 * @author Jessica_Alcantara
	 */
	public void initialise() {
		_graph.nodes().forEach((node)->{
			_maxCost = 0;
			calculateBottomLevel(node, 0);
			_bottomLevel.put(node, _maxCost);
		});
	}

	/**
	 * Calculates the maximum length path out of a node
	 * @param node Source node
	 * @param cost Current cost of path
	 * 
	 * @author Jessica_Alcantara
	 */
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
	
	/**
	 * Prints all the critical paths starting from each node in the input
	 * graph
	 * 
	 * @author Jessica_Alcantara
	 */
	public void printCriticalPaths() {
		for (Entry<Node, Integer> e : _bottomLevel.entrySet()) {
			System.out.println(e.getKey().getId() + " " + e.getValue());
		}
	}
	
	/**
	 * Returns the bottom level for a given node
	 * @param node Node
	 * 
	 * @author Jessica_Alcantara
	 */
	public int getBottomLevel(Node node) {
		return _bottomLevel.get(node);
	}

}
