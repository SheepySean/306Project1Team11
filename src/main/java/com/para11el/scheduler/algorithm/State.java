package com.para11el.scheduler.algorithm;

import org.graphstream.graph.Node;

public class State {
	
	private Node _node;
	private int _cost;
	private int _scheduleLength;
	
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
	
}
