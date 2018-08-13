package com.para11el.scheduler.algorithm;

import org.graphstream.graph.Node;

/**
 * State class to represent an object state.
 * 
 * @author Jessica Alcantara
 */
public class State {
	
	private Node _node;
	private Node _parent;
	private int _cost;
	private int _scheduleLength;
	
	public State() {}
	
	/**
	 * State constructor
	 * 
	 * @param node a node in input graph
	 * @param cost cost calculated by cost function
	 * @param scheduleLength the length of the partial schedule solution
	 * 
	 * @author Jessica Alcantara
	 */
	public State(Node node, Node parent, int cost, int scheduleLength) {
		_node = node;
		_parent = parent;
		_cost = cost;
		_scheduleLength = scheduleLength;
	}
	
	public Node getNode() {
		return _node;
	}
	
	public Node getParent() {
		return _parent;
	}
	
	public int getCost() {
		return _cost;
	}
	
	public int getScheduleLength() {
		return _scheduleLength;
	}
	
	public void setCost(int cost) {
		_cost = cost;
	}
	
	/**
	 * Compares state based on the cost
	 * @param compareState
	 * @return int representing ascending order
	 */
	public int compareTo(State compareState) {
		int compareCost = ((State) compareState).getCost(); 
		return this._cost - compareCost;
	}
	
}
