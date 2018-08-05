package com.para11el.scheduler.algorithm;

import java.util.ArrayList;
import java.util.Iterator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

/**
 * DFS Recursive Algorithm to find the solution 
 * 
 * @author Rebekah Berriman, Tina Chen
 */
public class SolutionSpaceManager {

	private Graph _graph;
	private int _processors;
	private int _cores;

	private ArrayList<Task> _solution = new ArrayList<Task>();
	private static ArrayList<ArrayList<Task>> _allSolutions = new ArrayList<ArrayList<Task>>();

	/**
	 * Constructor for SolutionSpaceManager if user does not specify number
	 * of cores for execution in parallel
	 * @param g input graph
	 * @param p number of processors
	 * 
	 * @author Tina Chen, Rebekah Berriman
	 */
	public SolutionSpaceManager(Graph graph, int processor) {
		_graph = graph;
		_processors = processor;
		_cores = 1;
	}

	/**
	 * Constructor for SolutionSpaceManager if user specifies number
	 * of cores for execution in parallel
	 * @param g input graph
	 * @param p number of processors
	 * @param c number of cores
	 * 
	 * @author Tina Chen, Rebekah Berriman
	 */
	public SolutionSpaceManager(Graph graph, int processor, int cores) {
		_graph = graph;
		_processors = processor;
		_cores = cores;
	}

	/**
	 * Initialises the root nodes in a schedule for each available processor
	 * and begins the recursive buildRecursiveSolution() call
	 * 
	 * @author Rebekah Berriman, Tina Chen
	 */
	public void initialise() {

		for (Node node : _graph.getNodeSet()) {
			if (node.getInDegree() == 0) {
				for (int i = 1; i <= _processors; i++) {
					Task t = new Task(node, 0, i);
					ArrayList<Task> _solutionPart = new ArrayList<Task>();
					_solutionPart.add(t);
					buildRecursiveSolution(_solutionPart);
				}
			}
		}
	}

	/**
	 * Recursively builds a potential schedule to the total solution schedule 
	 * ArrayList<ArrayList<Task>> using a DFS approach
	 * 
	 * @param solutionArrayList of the current scheduled nodes
	 *
	 * @author Rebekah Berriman, Tina Chen
	 */
	private void buildRecursiveSolution(ArrayList<Task> solutionArrayList) {		

		// Finding available nodes to add
		ArrayList<Node> availableNodes = availableNode(solutionArrayList);
		ArrayList<Task> privateSolutionArrayList = (ArrayList<Task>) solutionArrayList.clone();

		if (availableNodes.size() != 0) {
			for (Node node : availableNodes) {
				// For each available processor add available node to possible schedule
				for (int i = 1; i <= _processors; i++) {
					privateSolutionArrayList = (ArrayList<Task>) solutionArrayList.clone();		
					int startTime = getStartTime(privateSolutionArrayList, node, i);
					Task task = new Task(node, startTime, i);
					privateSolutionArrayList.add(task);			
					buildRecursiveSolution(privateSolutionArrayList);	
				}
			}
		} else {
			// If no more available nodes, add the possible solution schedule to full solution space
			if (privateSolutionArrayList.size() == _graph.getNodeCount()) {
				addSolution(privateSolutionArrayList);
			}
		}
	}


	/**
	 * Returns the earliest start time of the node on the processor
	 * @param solutionArrayList of the scheduled tasks
	 * @param node to be scheduled next
	 * @param processor for task to be scheduled on
	 * @return int of start time for the task
	 * 
	 * @author Rebekah Berriman
	 */
	private int getStartTime(ArrayList<Task> solutionArrayList, Node node, int processor) {
		int startTime = 0;
		int possibleTime;

		if (getParents(node).size() != 0) {
			for (Node parents : getParents(node)) {
				Task task = findNode(parents, solutionArrayList);
				int nodeWeightInt = ((Number) task.get_node().getAttribute("Weight")).intValue();
	
				if (task.get_processor() == processor) {
					possibleTime = task.get_startTime() + nodeWeightInt;
				} else {
					int edgeWeightDouble = ((Number) task.get_node().getEdgeToward(node).getAttribute("Weight")).intValue();
					int edgeWeightInt = (int)edgeWeightDouble;
					possibleTime = task.get_startTime() + nodeWeightInt + edgeWeightInt;
				}

				if (startTime < possibleTime) {
					startTime = possibleTime;
				}
			}		
		}

		possibleTime = getProcessorFinishTime(solutionArrayList, processor);
		if (startTime < possibleTime ) {
			startTime = possibleTime;
		}
		return startTime;
	}

	/**
	 * Find the node
	 * @param node in the inputGraph
	 * @param currentTasks 
	 * @return Task object node
	 * 
	 * @author Sean Oldfield, Rebekah Berriman
	 */
	private Task findNode(Node node, ArrayList<Task> currentTasks) {

		for (Task task : currentTasks) {
			if (task.get_node().equals(node)) {
				return task;
			}
		}
		return null;
	}

	/**
	 * Returns an int of the finishTime of the last task on the processor
	 * @param currentSchedule is an ArrayList of the currently scheduled tasks
	 * @param processor to schedule it on
	 * @return int of the finishTime
	 * 
	 * @author Rebekah Berriman
	 */
	private int getProcessorFinishTime(ArrayList<Task> currentSchedule, int processor) {
		int finishTime = 0;
		int possibleTime;

		for (Task task : currentSchedule) {
			if (task.get_processor() == processor) {
				int nodeWeightInt = ((Number) task.get_node().getAttribute("Weight")).intValue();
				possibleTime = task.get_startTime() + nodeWeightInt;
				if (finishTime < possibleTime) {
					finishTime = possibleTime;
				}
			}	
		}
		return finishTime;
	}

	/**
	 * Returns an ArrayList<Node> of the parents nodes a node has
	 * @param n the node to find parents of
	 * 
	 * @author Tina Chen 
	 */
	private ArrayList<Node> getParents(Node node) {

		ArrayList<Node> _parents = new ArrayList<Node>();
		Iterator<Edge> edge = node.getEnteringEdgeIterator();

		while (edge.hasNext()) {
			_parents.add(edge.next().getSourceNode());
		}
		return _parents;
	}

	/**
	 * Find the available nodes that can be scheduled given what nodes have already been scheduled.
	 * @param scheduledNodes
	 * @return ArrayList of available nodes
	 * 
	 * @author Rebekah Berriman, Tina Chen
	 */
	private ArrayList<Node>  availableNode(ArrayList<Task> scheduledTasks) {
		ArrayList<Node> scheduledNodes =  new ArrayList<Node>();
		ArrayList<Node> _available = new ArrayList<Node>();

		for (Task task : scheduledTasks) {
			scheduledNodes.add(task.get_node());
		}

		for (Node node : _graph.getNodeSet()) {
			if (!scheduledNodes.contains(node)) { // If Node is not already scheduled
				ArrayList<Node> _parents = getParents(node);
				if (_parents.size() == 0) { // Node has no parents so can be scheduled
					_available.add(node);
				} else {
					boolean availableNode = true;
					for (Node parentNode : _parents) {
						if (!scheduledNodes.contains(parentNode)) { // If the schedule does not contain a parent node of the node, set availableNode to false
							availableNode = false;
						}
					}
					if (availableNode) {
						_available.add(node);
					}
				}
			}
		}
		return _available;	
	}

	/**
	 * Adds a solution to the solution list
	 * @param s = a full task schedule solution
	 * 
	 * @author Rebekah Berriman, Tina Chen
	 */
	private void addSolution(ArrayList<Task> solution) {
		if (solution != null) {
			ArrayList<Task> newSolution = (ArrayList<Task>) solution.clone();
			_allSolutions.add(newSolution);

			findOptimal();
		}
	}

	/**
	 * Searches the possible solutions for the optimal solution (earliest finish time)
	 * Returns a full task schedule solution with the shortest completion time.
	 * @return an ArrayList<Task> of the optimal solution
	 * 
	 * @author Rebekah Berriman
	 */
	private void findOptimal() {
		int minimumTime=0;

		for (int possibleSolution=0; possibleSolution<_allSolutions.size(); possibleSolution++) {
			// Set the finish time of this solution
			int solutionTime = 0; 
			for (int processor=0; processor <= _processors; processor++) {
				int possibleSolutionTime = getProcessorFinishTime(_allSolutions.get(possibleSolution), processor);
				// If the finishTime of one processor is later than another, update the finish time of the task
				if (solutionTime < possibleSolutionTime) {
					solutionTime = possibleSolutionTime;
				}
			}

			// If the minimum run time is still zero, or the solution time is less than the minimal time, update it
			if (minimumTime == 0 || minimumTime > solutionTime ) {
				minimumTime = solutionTime;
				_solution = _allSolutions.get(possibleSolution);
			}
		}

		//Clear all solutions array list and only add the current optimal back. 
		_allSolutions.clear();
		_allSolutions.add(_solution);
	}

	/**
	 * Returns the optimal schedule in an arraylist of type Task.
	 * 
	 * @return the optimal schedule
	 * 
	 * @author Rebekah Berriman
	 */
	public ArrayList<Task> getOptimal() {
		return _solution;
	}

	/**
	 * Labels the graph with the startTime and processor numbers of each of the nodes for the optimal solution
	 * @param optimalSolution
	 * 
	 * @author Rebekah Berriman
	 */
	private void labelGraph() {
		for (Task task : _solution) {
			Node node = task.get_node();
			node.addAttribute("Start Time", task.get_startTime());
			node.addAttribute("Processor", task.get_processor());
		}
	}

	/**
	 * Returns the labeled graph.
	 * @return Graph of the nodes with labels
	 * 
	 * @author Rebekah Berriman
	 */
	public Graph getGraph() {
		labelGraph();
		return _graph;
	}
}
