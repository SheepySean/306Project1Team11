package com.para11el.scheduler.algorithm;

import java.util.ArrayList;
import java.util.concurrent.RecursiveAction;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

/**
 * DFS Recursive Algorithm to find the optimal solution.
 * This algorithm implementation does not support parallelisation.
 * 
 * @author Rebekah Berriman, Tina Chen
 */
public class DFSParallelAlgorithm extends RecursiveAction {
	
	protected Graph _graph;
	protected int _processors;
	protected int _cores;

	private int _minimumTime;
	private ArrayList<Task> _optimalSchedule = new ArrayList<Task>();

	/**
	 * Default constructor for DFS Algorithm
	 * 
	 * @author Tina Chen
	 */
	public DFSParallelAlgorithm() {
		super();
	}
	/**
	 * Constructor for DFS Algorithm for sequential solution
	 * @param graph of tasks to be scheduled
	 * @param processor number to schedule tasks on
	 * 
	 * @author Tina Chen
	 */
	public DFSParallelAlgorithm(Graph graph, int processor) {
		_graph = graph;
		_processors = processor;
		_cores = 1;
	}

	/**
	 * Constructor for DFS Algorithm for parallel solution
	 * @param graph of tasks to be scheduled
	 * @param processor number to schedule tasks on
	 * @param cores to parallelise
	 * 
	 * @author Tina Chen
	 */
	public DFSParallelAlgorithm(Graph graph, int processor, int cores) {
		_graph = graph;
		_processors = processor;
		_cores = cores;
	}
	
	/**
	 * Calls initialise to begin the recursive DFS
	 * @return an array list of type task with the optimal schedule
	 * 
	 * @author Rebekah Berriman
	 */
	public ArrayList<Task> buildSolution() {
		initialise();
		return _optimalSchedule;
	}
	
	@Override
	protected void compute() {
		// TODO Auto-generated method stub
		
	}
	

	/**
	 * Initialises the root nodes in a schedule for each available processor
	 * and begins the recursive buildRecursiveSolution() call
	 * 
	 * @author Rebekah Berriman, Tina Chen
	 */
	private void initialise() {
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
	 * ArrayList<Task> using a DFS approach
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

			for (int processor=1; processor <= _processors; processor++) {
				int solutionFinishTime = getProcessorFinishTime(newSolution, processor);
				
				if (solutionFinishTime > _minimumTime) {
					return;
				}
				// Update latest finish time
				if (solutionTime < solutionFinishTime) {
					solutionTime = solutionFinishTime;
				}
			}

			// Update minimal time and optimal solution
			if (_minimumTime >= solutionTime) {
				_minimumTime = solutionTime;
				_optimalSchedule = newSolution;
			}
		}
	}

	/**
	 * Returns the optimal schedule in an ArrayList of type Task
	 * @return ArrayList representing the optimal schedule
	 * 
	 * @author Rebekah Berriman
	 */
	public ArrayList<Task> getOptimal() {
		return _optimalSchedule;
	}
	
	/**
	 * Returns the finish time of the optimal solution
	 * @return int of the finish time 
	 * 
	 * @author Rebekah Berriman
	 */
	public int getOptimalFinishTime() {
		return _minimumTime;
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
	 * @author Holly Hagenson, Rebekah Berriman
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
	
	
}