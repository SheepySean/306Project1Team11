package com.para11el.scheduler.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import com.para11el.scheduler.ui.ViewerPaneController;

/**
 * Class that represents a task in DFS to be done in parallel.
 * 
 * @author Tina Chen
 */
public class DFSForkJoin extends RecursiveAction implements Parallelised {

	/**
	 * Protected variables
	 */
	private static final long serialVersionUID = 1L;
	protected Graph _graph;
	protected int _processors;
	protected int _cores;
	protected ArrayList<Task> _solutionList;

	/**
	 * Constructor for DFSForkJoin
	 * @param graph Input Graph
	 * @param processors Number of processors to schedule on
	 * @param cores Number of cores
	 * @param solutionList Scheduled tasks in the solution
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
		ViewerPaneController.setStatus("Using DFS to expand states in the schedule");
		// Finding available nodes to add
		ArrayList<Node> availableNodes = getAvailableNodes(_solutionList);
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

	/**
	 * Returns the GraphStream graph
	 * @return GraphStream graph
	 * 
	 * @author Sean Oldfield
	 */
	@Override
	public Graph getGSGraph() {
		return _graph;
	}
}
