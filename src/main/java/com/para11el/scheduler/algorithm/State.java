package com.para11el.scheduler.algorithm;

import java.util.ArrayList;
import java.util.Collection;

import org.graphstream.graph.Node;

/**
 * State class to represent an object state.
 * 
 * @author Jessica Alcantara
 */
public class State {
	
	private Node _node;
	private State _parent;
	private ArrayList<Task> _schedule;
	private int _cost;
	
	public State() {}
	
	/**
	 * State constructor
	 * 
	 * @param node a node in input graph
	 * @param cost cost calculated by cost function
	 * 
	 * @author Jessica Alcantara
	 */
	public State(Node node, State parent, ArrayList<Task> schedule, int cost) {
		_node = node;
		_parent = parent;
		_schedule = schedule;
		_cost = cost;
	}
	
	public Node getNode() {
		return _node;
	}
	
	public State getParent() {
		return _parent;
	}
	
	public ArrayList<Task> getSchedule() {
		return _schedule;
	}
	
	public int getCost() {
		return _cost;
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
	
	/**
	 * Checks that if the state is a complete solution where all input
	 * tasks are scheduled.
	 * @param nodes All nodes of the input graph
	 * @return boolean true if solution is complete
	 * 
	 * @author Jessica Alcantara
	 */
	public Boolean isComplete(Collection<Node> nodes) {
		if (_schedule.size() == nodes.size()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Finds the task corresponding to the last node in the state
	 * @param node in the inputGraph
	 * @param tasks in the partial solution
	 * @return Task object node
	 * 
	 * @author Sean Oldfield, Rebekah Berriman
	 */
	public Task findNode(Node node, ArrayList<Task> tasks) {
		for (Task task : tasks) {
			if (task.getNode().equals(node)) {
				return task;
			}
		}
		return null;
	}
}
