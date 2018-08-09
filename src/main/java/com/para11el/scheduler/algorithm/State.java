package com.para11el.scheduler.algorithm;

import org.graphstream.graph.Node;

public class State {
	
	private Node _node;
	private int _cost;
	private int _scheduleLength;
	
	public State() {}
	
	public State(Node node, int cost, int scheduleLength) {
		_node = node;
		_cost = cost;
		_scheduleLength = scheduleLength;
	}
	
	public Node getNode() {
		return _node;
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
