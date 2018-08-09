package com.para11el.scheduler.algorithm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

public class AlgorithmManager {
	
	Comparator<State> costComparator = new Comparator<State>() {
		@Override
        public int compare(State o1, State o2) {
			//TODO:
			/*
			 * Create comparator based on cost function
			 */
			return 0;
        }
	};
	
	private ArrayList<Task> _solution = new ArrayList<Task>();
	private Queue<State> _states = new PriorityQueue<State>(costComparator);
	
	
	/**
	 * PSEUDOCODE:
	 * 
	 * while _tasks.size() > 0 {
	 * 		State s = _tasks.pop();
	 *     _solution.add(s); // must check if task is free to be scheduled
	 *     if (_solution.isCompleteValidSolution) {
	 *     		return _solution;
	 *     }
	 *     
	 *     _tasks.add(children_of_s); // find the children of s and their cost functions
	 *     
	 *     
	 *  TO DO:
	 *  
	 * * Code the cost function
	 * * Code a check for free tasks
	 * * Code a check for a complete solution = checking that the size is the same as size of graph
	 * * Code BFS - finding the adjacent nodes
	 * 
	 * 
	 */
	
    /**
     * Retrieves the children of a node and adds the state to the priority queue.
     * @param node Parent node
     * @author Jessica Alcantara
     */
	public void pushChildren(State state) {
		for (Edge edge : state.getNode().getLeavingEdgeSet()) {
			Node child = edge.getTargetNode();
			int currentScheduleLength = state.getScheduleLength() + 
					((Number) child.getAttribute("Weight")).intValue();
			_states.add(new State(child, 
					calculateCostFunction(state, child),
					currentScheduleLength));
		}
	}
	
    /**
     * Calculates the cost function f(s) by the following equation:
     * 		f(s) = max{f(s-1), Cpe(S), Bt(s)}
     * @param parentState Parent state of the new node
     * @param newNode New node to calculate the cost function of 
     * @return int representing the cost
     * @author Jessica Alcantara
     */
	public int calculateCostFunction(State parentState, Node newNode) {
		int parentCost = parentState.getCost();
		int boundedTime = calculateBoundedTime();
		int criticalPathEstimate = calculateCriticalPathEstimate();
		
		int maxCost = Math.max(parentCost, boundedTime);
		maxCost = Math.max(maxCost, criticalPathEstimate);
		return maxCost;
	}
	
	// TODO:
	public int calculateBoundedTime() {
		return 0;
	}
	
	// TODO:
	public int calculateCriticalPathEstimate() {
		return 0;
	}
	
	

}
