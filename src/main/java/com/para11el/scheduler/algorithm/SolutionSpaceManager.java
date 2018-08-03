package com.para11el.scheduler.algorithm;

import java.util.ArrayList;
import java.util.Iterator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

/**
 * DFS Recursive Algorithm to find the solution 
 * @author Rebekah Berriman and Tina Chen
 *
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
	 * @param g = input graph
	 * @param p = number of processors
	 * 
	 * @author Tina Chen, Rebekah Berriman
	 */
	public SolutionSpaceManager(Graph graph, int processor) {
		_graph = graph;
		_processors = processor;
		_cores = 1;

		initialise();
		getOptimal();
		for (int i = 0; i < _allSolutions.size(); i++) {
			System.out.println(_allSolutions.get(i));
		}
		
		System.out.println("THIS IS OPTIMAL: " + _solution );

	}

	/**
	 * Constructor for SolutionSpaceManager if user specifies number
	 * of cores for execution in parallel
	 * @param g = input graph
	 * @param p = number of processors
	 * @param c = number of cores
	 * 
	 * @author Tina Chen, Rebekah Berriman
	 */
	public SolutionSpaceManager(Graph graph, int processor, int cores) {
		_graph = graph;
		_processors = processor;
		_cores = cores;
	}

	/**
	 * Finds the solution
	 * @author Rebekah Berriman and Tina Chen
	 */
	private void initialise() {
		
		System.out.println("number of processors!!!!!: " + _processors);

		for (Node node : _graph.getNodeSet()) {
			if (node.getInDegree() == 0) {
				for (int i = 1; i <= _processors; i++) {
					System.out.println("process");
					Task t = new Task(node, 0, i);
					ArrayList<Task> _solutionPart = new ArrayList<Task>();
					_solutionPart.add(t);
					buildRecursiveSolution(_solutionPart);

				}
			}
		}
	}

	/**
	 * PURPOSE: at each recursion, add new node as early as possible on each processor
	 *
	 *@author Rebekah Berriman and Tina Chen
	 */
	private ArrayList<Task> buildRecursiveSolutionOG(ArrayList<Task> solutionArrayList) {		
		ArrayList<Task> privateSolutionArrayList = (ArrayList<Task>) solutionArrayList.clone();
		ArrayList<Node> availableNodes = availableNode(solutionArrayList);	
		if (availableNodes.size() != 0 ) {
			System.out.println("hi");
			System.out.println(availableNodes); //[b,c] 
			for (Node node : availableNodes) { // [b]
				System.out.println("this is node: " + node);
				for (int i = 1; i <= _processors; i++) {
					System.out.println("this is processor: " + _processors);
					int startTime = getStartTime(privateSolutionArrayList, node, i);
					Task task = new Task(node, startTime, i);
					privateSolutionArrayList.add(task);			
					System.out.println("this is proviate solution arry: " + privateSolutionArrayList);
					ArrayList<Task> newSolution = buildRecursiveSolutionOG(privateSolutionArrayList);
					addSolution(newSolution);					
				}
			}
			return null;
		}
		System.out.println("---------");
		System.out.println("returned");
		System.out.println("---------");
		return privateSolutionArrayList;	
	}

	/**
	 * PURPOSE: at each recursion, add new node as early as possible on each processor
	 *
	 *@author Rebekah Berriman and Tina Chen
	 */
	private void buildRecursiveSolution(ArrayList<Task> solutionArrayList) {		

		/*
		 * we are passing in a solution arraylist
		 */

		ArrayList<Node> availableNodes = availableNode(solutionArrayList);

		ArrayList<Task> privateSolutionArrayList = (ArrayList<Task>) solutionArrayList.clone();
		
		System.out.println("num of processors : " + _processors);


		if (availableNodes.size() != 0) {

			System.out.println("num of avail nodes: " + availableNodes.size());
			for (Node node : availableNodes) {

				for (int i = 1; i <= _processors; i++) {
					privateSolutionArrayList = (ArrayList<Task>) solutionArrayList.clone();

					System.out.println("this is node: " + node);
					int startTime = getStartTime(privateSolutionArrayList, node, i);
					Task task = new Task(node, startTime, i);
					privateSolutionArrayList.add(task);			
					buildRecursiveSolution(privateSolutionArrayList);	
				}

			}
		} else {
			System.out.println("hi");
			if (privateSolutionArrayList.size() == _graph.getNodeCount()) {
				addSolution(privateSolutionArrayList);
			}
		}

		//		ArrayList<Task> privateSolutionArrayList = (ArrayList<Task>) solutionArrayList.clone();
		//		ArrayList<Node> availableNodes = availableNode(solutionArrayList);	
		//		if (availableNodes.size() != 0 ) {
		//			System.out.println("hi");
		//			System.out.println(availableNodes); //[b,c] 
		//			for (Node node : availableNodes) { // [b]
		//				System.out.println("this is node: " + node);
		//				for (int i = 1; i <= _processors; i++) {
		//					System.out.println("this is processor: " + _processors);
		//					int startTime = getStartTime(privateSolutionArrayList, node, i);
		//					Task task = new Task(node, startTime, i);
		//					privateSolutionArrayList.add(task);			
		//					System.out.println("this is proviate solution arry: " + privateSolutionArrayList);
		//					ArrayList<Task> newSolution = buildRecursiveSolution(privateSolutionArrayList);
		//					addSolution(newSolution);					
		//				}
		//			}
		//			return null;
		//		}
		//		System.out.println("---------");
		//		System.out.println("returned");
		//		System.out.println("---------");
		//		return privateSolutionArrayList;	



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

				double nodeWeightDouble = task.get_node().getAttribute("Weight");
				int nodeWeightInt = (int)nodeWeightDouble;
				if (task.get_processor() == processor) {
					possibleTime = task.get_startTime() + nodeWeightInt;
				} else {
					double edgeWeightDouble = task.get_node().getEdgeToward(node).getAttribute("Weight");
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
	 * @author Sean Oldfield and Rebekah Berriman
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
				double nodeWeightDouble = task.get_node().getAttribute("Weight");
				int nodeWeightInt = (int)nodeWeightDouble;
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
			if (!scheduledNodes.contains(node)) { //if Node is not already scheduled
				ArrayList<Node> _parents = getParents(node);
				if (_parents.size() == 0) { //Node has no parents so can be scheduled
					_available.add(node);
				} else {
					boolean availableNode = true;
					for (Node parentNode : _parents) {
						if (!scheduledNodes.contains(parentNode)) { //if the schedule does not contain a parent node of the node, set availableNode to false
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
		}

	}

	/**
	 * Searches the possible solutions for the optimal solution (earliest finish time)
	 * Returns a full task schedule solution with the shortest completion time.
	 * @return an ArrayList<Task> of the optimal solution
	 * 
	 * @author Rebekah Berriman
	 */
	private void getOptimal() {
		int minimumTime=0;

		for (int possibleSolution=0; possibleSolution<_allSolutions.size(); possibleSolution++) {
			//Set the finish time of this solution
			int solutionTime = 0; 
			for (int processor=0; processor <= _processors; processor++) {
				int possibleSolutionTime = getProcessorFinishTime(_allSolutions.get(possibleSolution), processor);
				//if the finishTime of one processor is later than another, update the finish time of the task
				if (solutionTime < possibleSolutionTime) {
					solutionTime = possibleSolutionTime;
				}
			}

			//if the minimum run time is still zero, or the solution time is less than the minimal time, update it
			if (minimumTime == 0 || minimumTime > solutionTime ) {
				minimumTime = solutionTime;
				_solution = _allSolutions.get(possibleSolution);
			}

			//System.out.println("Optimal solution: " + _solution);
		}

		//Call labelGraph() to label the nodes of the graph with the start time and processor of the optimal solution
		labelGraph();
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
		return _graph;
	}
}
