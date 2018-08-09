package com.para11el.scheduler.algorithm;

import org.graphstream.graph.Node;

/**
 * State class to represent an object state.
 * 
 * @author Jessica Alcantara
 */
public class State {
	
	private Node _node;
	private int _cost;
	private int _scheduleLength;
	
	public State() {}
	
	/**
	 * Task constructor
	 * 
	 * @param node a node in input graph
	 * @param cost cost calculated by cost function
	 * @param scheduleLength the length of the partial schedule solution
	 * 
	 * @author Jessica Alcantara
	 */
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
	
	//TODO: conversion of state to task
	public Task toTask() {
		return null;
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
