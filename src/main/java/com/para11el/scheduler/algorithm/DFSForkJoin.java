package com.para11el.scheduler.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import com.para11el.scheduler.ui.ViewerPaneController;

/**
 * 
 * @author Tina Chen
 *
 */
public class DFSForkJoin extends RecursiveAction {

	/**
	 * Protected variables
	 */
	private static final long serialVersionUID = 1L;
	protected Graph _graph;
	protected int _processors;
	protected int _cores;
	protected ArrayList<Task> _optimalSchedule = new ArrayList<Task>();
	protected ArrayList<Task> _solutionList;

	/**
	 * Constructor for DFSForkJoin
	 * @param graph
	 * @param processors
	 * @param cores
	 * @param minimumTime
	 * @param solutionList
	 * 
	 * @author Tina Chen
	 */
	public DFSForkJoin(Graph graph, int processors, int cores, ArrayList<Task> solutionList) {
		_graph = graph;
		_processors = processors;
		_cores = cores;
		_solutionList = solutionList;
	}

	/**
	 * Computes the optimal solution for DFS using parallelisation,
	 * utilising subtasks on the input number of threads
	 * 
	 * @author Tina Chen
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void compute() {
		// Finding available nodes to add
		ArrayList<Node> availableNodes = availableNode(_solutionList);
		ArrayList<Task> private_solutionList = (ArrayList<Task>)_solutionList.clone();

		// Check if a timeout has occurred
		ViewerPaneController.getInstance();
		if (!ViewerPaneController.getTimeout()) {

			if (availableNodes.size() != 0) {

				// Create a list of DFSForkJoin classes for subtasking
				List<DFSForkJoin> subtasks = new ArrayList<DFSForkJoin>();

				for (Node node : availableNodes) {
					OptimalSchedule preserveOptimal = OptimalSchedule.getInstance();

					// For each available processor add available node to possible schedule
					for (int i = 1; i <= _processors; i++) {
						private_solutionList = (ArrayList<Task>) _solutionList.clone();		
						int startTime = getEarliestStartTime(node, private_solutionList, i);
						if (startTime > preserveOptimal.getOptimalTime()) {
							break;
						}
						Task task = new Task(node, startTime, i);
						private_solutionList.add(task);	

						// Create a new subtask using new DFSForkJoin class
						DFSForkJoin subtask = new DFSForkJoin(_graph, _processors,
								_cores, private_solutionList);

						subtasks.add(subtask);
					}
				}
				// Fork each subtask
				for(RecursiveAction subtask : subtasks){
					subtask.fork();
				}	
			} else {
				// Add schedule to solution space if there are no more available nodes
				if (private_solutionList.size() == _graph.getNodeCount()) {
					findOptimal(private_solutionList);
				}
			}
		}
	}

	/**
	 * Find the available nodes that can be scheduled given what nodes have already 
	 * been scheduled
	 * @param scheduledTasks
	 * @return ArrayList of available nodes
	 * 
	 * @author Rebekah Berriman, Tina Chen
	 */
	public ArrayList<Node> availableNode(ArrayList<Task> scheduledTasks) {
		ArrayList<Node> scheduledNodes =  new ArrayList<Node>();
		ArrayList<Node> available = new ArrayList<Node>();

		// Get list of scheduled nodes
		for (Task task : scheduledTasks) {
			scheduledNodes.add(task.getNode());
		}

		_graph.nodes().forEach((node) -> {
			// Find nodes that have not been scheduled
			if (!scheduledNodes.contains(node)) {
				ArrayList<Node> parents = getParents(node);

				// Add nodes with no parents to available nodes
				if (parents.size() == 0) {
					available.add(node);		
					// Check all the parents of the node have been scheduled
				} else {
					boolean availableNode = true;
					for (Node parentNode : parents) {
						// Node is not available if parent has not been scheduled
						if (!scheduledNodes.contains(parentNode)) {
							availableNode = false;
						}
					}
					if (availableNode) {
						available.add(node);
					}
				}
			}
		});
		return available;
	}

	/**
	 * Returns a list of parent nodes of a node
	 * @param node Node to find parents of
	 * 
	 * @author Tina Chen 
	 */
	public ArrayList<Node> getParents(Node node) {
		ArrayList<Node> parents = new ArrayList<Node>();
		node.enteringEdges().forEach((edge) -> {
			parents.add(edge.getSourceNode());
		});
		return parents;
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
	 * Returns an int of the finishTime of the last task on the processor
	 * @param schedule ArrayList of the scheduled tasks
	 * @param processor To find latest finish time of
	 * @return int of the finishTime
	 * 
	 * @author Rebekah Berriman
	 */
	public int getProcessorFinishTime(ArrayList<Task> schedule, int processor) {
		int processorFinishTime = 0;
		int taskFinishTime;

		for (Task task : schedule) {
			if (task.getProcessor() == processor) {
				taskFinishTime = task.getFinishTime();
				if (processorFinishTime < taskFinishTime) {
					processorFinishTime = taskFinishTime;
				}
			}	
		}
		return processorFinishTime;
	}

	/**
	 * Finds the earliest start time of a task on a processor with given dependencies
	 * @param node Node to be scheduled
	 * @param schedule ArrayList of scheduled tasks
	 * @param processor Processor to schedule the node on
	 * @return int of the earliest start time
	 * 
	 * @author Rebekah Berriman
	 */
	public int getEarliestStartTime(Node node, ArrayList<Task> schedule, int processor) {
		int processorFinishTime; 
		int nodeStartTime = 0;
		int parentLatestFinish = 0;

		// Get the latest finish time of the parents
		for (Node parent : getParents(node)){
			Task task = findNodeTask(parent, schedule); 
			int nodeWeight = task.getWeight();

			if (task.getProcessor() == processor) {
				parentLatestFinish = task.getStartTime() + nodeWeight; 
			} else{
				int edgeWeight = ((Number)task.getNode().getEdgeToward(node)
						.getAttribute("Weight")).intValue();
				parentLatestFinish = task.getStartTime() + nodeWeight + edgeWeight;  
			}
			if (parentLatestFinish > nodeStartTime){
				nodeStartTime = parentLatestFinish; 
			}
		}

		// Get the latest finish time of the current processor
		processorFinishTime = getProcessorFinishTime(schedule, processor);
		if (processorFinishTime > nodeStartTime) {
			nodeStartTime = processorFinishTime;
		}
		return nodeStartTime;
	}

	/**
	 * Compares the new solution to the current solution and changes the optimal solution
	 * to be the schedule with the shortest completion time
	 * @param solution List of scheduled tasks representing a possible solution
	 * 
	 * @author Rebekah Berriman
	 */
	@SuppressWarnings("unchecked")
	private void findOptimal(ArrayList<Task> solution) {
		if (solution != null) {
			int solutionTime = 0; 
			ArrayList<Task> newSolution = (ArrayList<Task>)solution.clone();
			OptimalSchedule preserveOptimal = OptimalSchedule.getInstance();

			for (int processor=1; processor <= _processors; processor++) {
				int solutionFinishTime = getProcessorFinishTime(newSolution, processor);

				if (solutionFinishTime > preserveOptimal.getOptimalTime()) {
					return;
				}
				// Update latest finish time
				if (solutionTime < solutionFinishTime) {
					solutionTime = solutionFinishTime;
				}
			}
			// Update minimal time and optimal solution
			if (preserveOptimal.getOptimalTime() >= solutionTime) {
				preserveOptimal.setOptimal(newSolution, solutionTime);
			}
		}
	}
	
}
