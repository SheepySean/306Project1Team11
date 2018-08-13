package com.para11el.scheduler.algorithm;

import java.util.ArrayList;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

/**
 * Class to calculate the cost function of the A* algorithm.
 * 
 * @author Holly Hagenson
 *
 */
public class CostFunctionManager {
	
	private int _max = 0; 
	private int _dist = 0; 
	
	private int _totalWeight;
	private int _processors;
	
	public CostFunctionManager(int totalWeight, int processor) {
		_totalWeight = totalWeight;
		_processors = processor;	
	}
	
    /**
     * Calculates the cost function f(s) by the following equation:
     * 		f(s) = max{f(s-1), Cpe(S), Bt(s)}
     * @param parentState Parent state of the new node
     * @param newNode New node to calculate the cost function of 
     * @return int representing the cost
     * 
     * @author Jessica Alcantara
     */
	public int calculateCostFunction(State parentState, Node newNode, 
			ArrayList<Task> partialSolution) {
		int parentCost;
		int boundedTime = calculateBoundedTime(partialSolution);
		int criticalPathEstimate = calculateCriticalPathEstimate(parentState, partialSolution);
		
		// Check if parent node exists
		if (parentState == null) {
			parentCost = 0;
		} else {
			parentCost = parentState.getCost();
		}
		
		int maxCost = Math.max(parentCost, boundedTime);
		maxCost = Math.max(maxCost, criticalPathEstimate);
		return maxCost;
	}
	
	/**
	 * Calculates the critical path estimate based on:
	 * 		Cpe(S) = startTime(nlast) + bottomLevel(nlast)
	 * 
	 * @param state Nlast, node with latest finish time in partial schedule
	 * @param solution, task schedule for current solution
	 * @return int of critical path estimate
	 * 
	 * @author Holly Hagenson
	 */
	public int calculateCriticalPathEstimate(State state, ArrayList<Task> solution) {
		Task parentTask = state.findNode(state.getNode(), solution);
		int bottomLevel = bottomLevel(state.getNode());
		int startTime = parentTask.getStartTime(); 
		
		return startTime + bottomLevel;
	}
	
	/**
	 * Calculates the bottom level of a node. 
	 * 
	 * @param node Node to calculate bottom level of
	 * @return int of bottom level value
	 * 
	 * @author Holly Hagenson
	 */
	public int bottomLevel(Node node){
		// Create path with source node
		List<Node> path = new ArrayList<Node>(); 
		path.add(node); 
		
		_max = 0; 

		findLongestPath(path, node);
		
		return _max; 
	}
	
	/**
	 * Finds the longest path from the given node to a leaf node.
	 * 
	 * @param path Current path of nodes
	 * @param source Node to find paths from
	 * 
	 * @author Holly Hagenson
	 */
	private void findLongestPath(List<Node> path, Node source){
		// Calculate cost of current path at leaf node
		if (source.getOutDegree() == 0){
			for (Node n : path){
				_dist += ((Number)n.getAttribute("Weight")).intValue();
			}
			// Update maximum path length
			if (_dist > _max){
				_max = _dist;
			}
			_dist = 0; 
		} else {
			// Traverse through graph to find all paths from source
			for (Edge e : source.getLeavingEdgeSet()) {
				List<Node> newPath = new ArrayList<Node>(path); 
				newPath.add(e.getNode1());
				findLongestPath(newPath, e.getNode1()); 
			}
		}	
	}
	
	/**
	 * Calculates the bounded time based on:
	 * 		Bt(S) = idle(S) + sum(weights(all nodes))
	 * @param solution Task schedule for current solution
	 * @return int of the bounded time
	 * 
	 * @author Jessica Alcantara
	 */
	public int calculateBoundedTime(ArrayList<Task> solution) {
		int idleTime = calculateIdleTime(solution);
		int boundedTime = (idleTime + _totalWeight)/_processors;
		return boundedTime;
	}
	
	/**
	 * Calculates the sum of all idle times on each processor.
	 * @param solution Task schedule for current solution
	 * @return int of the idle time
	 * 
	 * @author Jessica Alcantara
	 */
	public int calculateIdleTime(ArrayList<Task> solution) {
		int idleTime = 0;
		int finishTime;
		int time;
		for (int i=1; i<= _processors; i++) {
			finishTime = 0;
			for (Task task : solution) {
				// Sum the idle time on each processor
				if (i == task.getProcessor()) {
					time = task.getStartTime() - finishTime;
					// Check if the processor is idle
					if (time != 0) {
						idleTime += Math.abs(time);
					}
					finishTime = task.getStartTime() + 
							((Number)task.getNode().getAttribute("Weight")).intValue();
					System.out.println("Finish Time: " + finishTime);
				}
			}
		}
		return idleTime;
	}

}
