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
	public SolutionSpaceManager(Graph g, int p) {
		_graph = g;
		_processors = p;
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
	public SolutionSpaceManager(Graph g, int p, int c) {
		_graph = g;
		_processors = p;
		_cores = c;
	}
	
	private void initialise() {
		
		for (Node n : _graph.getNodeSet()) {
			if (n.getInDegree() == 0) {
				for (int i = 1; i <= _processors; i++) {
					
					Task t = new Task(n, 0, i);
					ArrayList<Task> _solutionPart = new ArrayList<Task>();
					_solutionPart.add(t);
					buildRecursiveSolution(_solutionPart);
					
				}
			}
		}
	}
	
	/*
	 * PURPOSE: at each recursion, add new node as early as possible on each processor
	 *
	 */
	
	private void buildRecursiveSolution(ArrayList<Task> s) {
		
	}
	
	
	/**
	 * Returns an ArrayList<Node> of the parents nodes a node has
	 * @param n = the node to find parents of
	 * 
	 * @author Tina Chen 
	 */
	private ArrayList<Node> getParents(Node n) {
		
		ArrayList<Node> _parents = new ArrayList<Node>();
		Iterator<Edge> i = n.getEnteringEdgeIterator();
		
		while (i.hasNext()) {
			_parents.add(i.next().getSourceNode());
		}
		return _parents;
	}
	
	
	private ArrayList<Node>  availableNode(ArrayList<Task> s) {
		ArrayList<Task> _scheduledNodes =  s;
		ArrayList<Node> _available = new ArrayList<Node>();
		
		
		for (Node n : _graph.getNodeSet()) {
			if (!_scheduledNodes.contains(n)) {
				if (n.getInDegree() == 0) {
					_available.add(n);
				} else {
					
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
	private void addSolution(ArrayList<Task> s) {
		_allSolutions.add(s);
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
