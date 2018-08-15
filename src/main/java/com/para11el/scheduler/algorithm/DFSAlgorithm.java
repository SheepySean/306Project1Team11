package com.para11el.scheduler.algorithm;

import java.util.ArrayList;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

/**
 * DFS Recursive Algorithm to find the optimal solution.
 * This algorithm implementation does not support parallelisation.
 * 
 * @author Rebekah Berriman, Tina Chen
 */
public class DFSAlgorithm extends Algorithm {

	private int _minimumTime;
	private ArrayList<Task> _optimalSolution = new ArrayList<Task>();

	public DFSAlgorithm() {
		super();
	}
	
	public DFSAlgorithm(Graph graph, int processor) {
		super(graph, processor);
	}

	public DFSAlgorithm(Graph graph, int processor, int cores) {
		super(graph, processor, cores);
	}

	/**
	 * Initialises the root nodes in a schedule for each available processor
	 * and begins the recursive buildRecursiveSolution() call
	 * 
	 * @author Rebekah Berriman, Tina Chen
	 */
	public void initialise() {
		setMaximumTime();

		_graph.nodes().forEach((node) -> {
			if (node.getInDegree() == 0) {
				Task t = new Task(node, 0, 1);
				ArrayList<Task> solutionPart = new ArrayList<Task>();
				solutionPart.add(t);
				buildRecursiveSolution(solutionPart);
			}
		});
	}
	
	/**
	 * Sets the absolute maximum time to complete all tasks (sequentially on a single 
	 * processor), which can then be used for bounding. Sets a private int _minimumTime
	 * 
	 * @author Rebekah Berriman
	 */
	private void setMaximumTime() {
		_minimumTime=0;
		_graph.nodes().forEach((node) -> {
			_minimumTime+= ((Number)node.getAttribute("Weight")).intValue();
		});
	}

	/**
	 * Recursively builds a potential schedule to the total solution schedule 
	 * ArrayList<ArrayList<Task>> using a DFS approach
	 * @param solutionArrayList List of the current scheduled nodes
	 *
	 * @author Rebekah Berriman, Tina Chen
	 */
	@SuppressWarnings("unchecked")
	private void buildRecursiveSolution(ArrayList<Task> solutionArrayList) {		
		// Finding available nodes to add
		ArrayList<Node> availableNodes = availableNode(solutionArrayList);
		ArrayList<Task> privateSolutionArrayList = (ArrayList<Task>)solutionArrayList.clone();

		if (availableNodes.size() != 0) {
			for (Node node : availableNodes) {
				// For each available processor add available node to possible schedule
				for (int i = 1; i <= _processors; i++) {
					privateSolutionArrayList = (ArrayList<Task>) solutionArrayList.clone();		
					int startTime = getEarliestStartTime(node, privateSolutionArrayList, i);
					if (startTime > _minimumTime) {
						break;
					}
					Task task = new Task(node, startTime, i);
					privateSolutionArrayList.add(task);			
					buildRecursiveSolution(privateSolutionArrayList);	
				}
			}
		} else {
			// Add schedule to solution space if there are no more available nodes
			if (privateSolutionArrayList.size() == _graph.getNodeCount()) {
				findOptimal(privateSolutionArrayList);
			}
		}
	}

	/**
	 * Checks if the new solution is a better solution that currently stored.
	 * Returns a full task schedule solution with the shortest completion time.
	 * @return an ArrayList<Task> of the optimal solution
	 * 
	 * @author Rebekah Berriman
	 */
	@SuppressWarnings("unchecked")
	private void findOptimal(ArrayList<Task> solution) {
		if (solution != null) {
			ArrayList<Task> newSolution = (ArrayList<Task>)solution.clone();
			
			int solutionTime = 0; 
			for (int processor=1; processor <= _processors; processor++) {
				int possibleSolutionTime = getProcessorFinishTime(newSolution, processor);
				
				if (possibleSolutionTime > _minimumTime) {
					return;
				}
				// If the finishTime of one processor is later than another, update the finish time of the task
				if (solutionTime < possibleSolutionTime) {
					solutionTime = possibleSolutionTime;
				}
			}

			// If the solution time is less than or equal to the minimal time, update the _solution
			if (_minimumTime >= solutionTime) {
				_minimumTime = solutionTime;
				_optimalSolution = newSolution;
			}
		}
	}

	/**
	 * Returns the optimal schedule in an arraylist of type Task.
	 * 
	 * @return the optimal schedule
	 * 
	 * @author Rebekah Berriman
	 */
	public ArrayList<Task> getOptimal() {
		return _optimalSolution;
	}
	
	/**
	 * Ensures that testing can ensure that the minimum test time is found
	 * @return int of the finish time of the optimal solution (the earliest time that the tasks can be computed).
	 * 
	 * @author Rebekah Berriman
	 */
	public int getOptimalFinishTime() {
		return _minimumTime;
	}
}
