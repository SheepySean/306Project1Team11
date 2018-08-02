package com.para11el.scheduler.algorithm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

public class SolutionSpaceManager {
	
	private Graph _graph;
	private int _processors;
	private int _cores;
	
	private ArrayList<Task> _solution = new ArrayList<Task>();
	private List<List<Task>> _allSolutions = new ArrayList<List<Task>>();
	
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
	 * PURPOSE: at each recursion, add new node as early as possible on each processor
	 *
	 *@author Rebekah Berriman and Tina Chen
	 */
	
	private void buildRecursiveSolution(ArrayList<Task> solutionArrayList) {
		ArrayList<Node> availableNodes = availableNode(solutionArrayList);
		if (availableNodes.size() == 0) {
			addSolution(solutionArrayList);
			return;
		}
		
		for (Node node : availableNodes) {
			for (int i = 1; i <= _processors; i++) {
				int startTime = getStartTime(solutionArrayList, node, i);
				Task task = new Task(node, startTime, i);
				solutionArrayList.add(task);
				buildRecursiveSolution(solutionArrayList);
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
				Task t = solutionArrayList.get(solutionArrayList.indexOf(node));
				if (t.get_processor() == processor) {
					possibleTime = t.get_startTime() + Integer.parseInt((t.get_node()).getAttribute("Weight"));
				} else {
					possibleTime = t.get_startTime() + Integer.parseInt((t.get_node()).getAttribute("Weight")) + Integer.parseInt((t.get_node()).getEdgeFrom(parents).getAttribute("Weight"));
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
	 * Returns an int of the finishTime of the last task on the processor
	 * @param s currently scheduled tasks
	 * @param p processor to schedule it on
	 * @return int of the finishTime
	 * 
	 * @author Rebekah Berriman
	 */
	private int getProcessorFinishTime(ArrayList<Task> s, int p) {
		int finishTime = 0;
		int possibleTime;
		
		for (Task t : s) {
			if (t.get_processor() == p) {
				possibleTime = t.get_startTime() + Integer.parseInt((t.get_node()).getAttribute("Weight"));
				if (finishTime < possibleTime) {
					finishTime = possibleTime;
				}
			}	
		}
		
		return finishTime;
	}
	
	/**
	 * Returns an ArrayList<Node> of the parents nodes a node has
	 * @param n = the node to find parents of
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
	 * 
	 * @param scheduledNodes
	 * @return ArrayList of available nodes
	 * 
	 * @author Rebekah Berriman and Tina Chen
	 */
	private ArrayList<Node>  availableNode(ArrayList<Task> scheduledNodes) {
		ArrayList<Task> _scheduledNodes =  scheduledNodes;
		ArrayList<Node> _available = new ArrayList<Node>();
		
		
		for (Node node : _graph.getNodeSet()) {
			if (!_scheduledNodes.contains(node)) { //if Node is not already scheduled
				ArrayList<Node> _parents = getParents(node);
				if (_parents.size() == 0) { //Node has no parents so can be scheduled
					_available.add(node);
				} else {
					boolean availableNode = true;
					for (Node parentNode : _parents) {
						if (!_scheduledNodes.contains(parentNode)) { //if schedule
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
		_allSolutions.add(solution);
	}
	
	// get start time of last task in list IN EACH PROCESSOR and then add task time to find smallest
	/**
	 * Returns the full task schedule solution with the shorted completion time
	 * @return an ArrayList<Task> of the optimal solution
	 * 
	 * @author 
	 */
	private ArrayList<Task> getOptimal() {
		return _solution;
	}
}
