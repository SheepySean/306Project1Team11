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
		int criticalPathEstimate = criticalPathEstimate(partialSolution);
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
	 * Returns the task with the latest finish time
	 * @param schedule Partial schedule of tasks
	 * @return Task with the latest finish time
	 * 
	 * @author Holly Hagenson
	 */
	public Task getLastTask(ArrayList<Task> schedule) {
		int latestFinish = 0;
		Task lastTask = null;
		
		for(Task task : schedule){
			int taskFinish = task.getFinishTime();
			if (taskFinish > latestFinish){
				latestFinish = taskFinish; 
				lastTask = task; 
			}
		}
		return lastTask;
	}
	
	/**
	 * Calculates the critical path estimate based on:
	 * 		Cpe(S) = max{startTime(n) + bottomLevel(n)}
	 * @param solution Task schedule for current solution
	 * @return int of critical path estimate
	 * 
	 * @author Jessica Alcantara
	 */
	public int criticalPathEstimate(ArrayList<Task> schedule) {
		int max = 0;
		for (Task task : schedule) {
			int help = task.getStartTime() + _nm.getBottomLevel(task.getNode());
			if (help > max) {
				max = help;
			}
		}
		return max;
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
