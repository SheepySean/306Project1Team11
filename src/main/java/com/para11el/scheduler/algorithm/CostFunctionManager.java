package com.para11el.scheduler.algorithm;

import java.util.ArrayList;

import org.graphstream.graph.Node;

/**
 * Manager class to perform calculations for the cost function of the A* algorithm.
 * 
 * @author Holly Hagenson, Jessica Alcantara
 *
 */
public class CostFunctionManager {

	private NodeManager _nm;
	private int _totalWeight;
	private int _processors;
	
	/**
	 * Constructor for CostFunctionManager
	 * @param nm Node Manager that handles bottom level calculations
	 * @param totalWeight Total weight of nodes in the input graph
	 * @param processors Number of processors
	 * 
	 * @author Jessica Alcantara
	 */
	public CostFunctionManager(NodeManager nm, int totalWeight, int processors) {
		_nm = nm;
		_totalWeight = totalWeight;
		_processors = processors;	
	}
	
    /**
     * Calculates the cost function f(s) by the following equation:
     * 		f(s) = max{f(s-1), Cpe(S), Bt(s)}
     * @param parentState Parent state of the new node
     * @param newNode New node to calculate the cost function of 
     * @param partialSolution Schedule representing the state
     * @return int representing the cost
     * 
     * @author Jessica Alcantara
     */
	public int calculateCostFunction(State parentState, Node newNode, 
			ArrayList<Task> partialSolution) {
		int parentCost;
		int criticalPathEstimate = criticalPathEstimate(partialSolution, newNode);
		int boundedTime = calculateBoundedTime(partialSolution);
		
		// Check if parent state exists
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
	 * 		Cpe(S) = startTime(nlast) + bottomLevel(nlast)}
	 * @param solution Task schedule for current solution
	 * @param lastAdded Node last added to the state
	 * @return int of critical path estimate
	 * 
	 * @author Jessica Alcantara
	 */
	public int criticalPathEstimate(ArrayList<Task> schedule, Node lastAdded) {
		Task task = findNodeTask(lastAdded,schedule);
		int cpe = task.getStartTime() + _nm.getBottomLevel(lastAdded);
		return cpe;
	}
	
	/**
	 * Returns the task corresponding to a node
	 * @param node From input graph
	 * @param schedule ArrayList of scheduled tasks
	 * @return Task object node
	 * 
	 * @author Sean Oldfield
	 */
	public Task findNodeTask(Node node, ArrayList<Task> schedule) {
		for (Task task : schedule) {
			if (task.getNode().equals(node)) {
				return task;
			}
		}
		return null;
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
	 * Calculates the sum of all idle times on each processor
	 * @param solution Task schedule for current solution
	 * @return int of the idle time
	 * 
	 * @author Jessica Alcantara
	 */
	public int calculateIdleTime(ArrayList<Task> solution) {
		int idleTime = 0;
		int finishTime, time;
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
					finishTime = task.getFinishTime();
				}
			}
		}
		return idleTime;
	}

}
