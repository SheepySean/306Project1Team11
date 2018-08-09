package com.para11el.scheduler.algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

public class AlgorithmManager {
	
	/**
	 * Comparator to sort states according to the results of the cost
	 * function.
	 * @author Jessica Alcantara
	 */
	private Comparator<State> _stateComparator = new Comparator<State>() {
		@Override
        public int compare(State o1, State o2) {
			return o1.compareTo(o2);
        }
	};
	
	private ArrayList<Task> _solution = new ArrayList<Task>();
	private Queue<State> _states = new PriorityQueue<State>(_stateComparator);
	private Graph _graph;
	private int _processors;
	private int _cores;
	
	public AlgorithmManager() {}
	
	/**
	 * Constructor for AlgorithmManager for sequential solution
	 * @param graph input graph
	 * @param processor number of processors
	 * 
	 * @author Jessica Alcantara
	 */
	public AlgorithmManager(Graph graph, int processor) {
		_graph = graph;
		_processors = processor;
		_cores = 1;
	}

	/**
	 * Constructor for AlgorithmManager for parallel solution
	 * @param graph input graph
	 * @param processor number of processors
	 * @param cores number of cores
	 * 
	 * @author Jessica Alcantara
	 */
	public AlgorithmManager(Graph graph, int processor, int cores) {
		_graph = graph;
		_processors = processor;
		_cores = cores;
	}
	
	/**
	 * Initializes the algorithm and build solution
	 * 
	 * @author Jessica Alcantara, Holly Hagenson
	 */
	public ArrayList<Task> getSolution() {
		while (_states.size() > 0) {
			// Add the most promising state to the solution
			State state = getFirstFreeTask();
			_solution.add(state.toTask());
			
			// Check if solution is complete
			if (isCompleteSolution(_solution)) {
				return _solution;
			}
			
			// Expand state into children
			pushChildren(state);
		}
		return _solution;
	}
	
	//TODO: find first task in _states that is free to be scheduled
	public State getFirstFreeTask() {
		return null;
	}
	
	//TODO: check if solution is complete
	public Boolean isCompleteSolution(ArrayList<Task> solution) {
		return true;
	}

	
    /**
     * Retrieves the children of a node and adds the state to the priority queue.
     * @param node Parent node
     * @author Jessica Alcantara
     */
	public void pushChildren(State state) {
		for (Edge edge : state.getNode().getLeavingEdgeSet()) {
			Node child = edge.getTargetNode();
			int currentScheduleLength = state.getScheduleLength() + 
					((Number) child.getAttribute("Weight")).intValue();
			_states.add(new State(child, 
					calculateCostFunction(state, child),
					currentScheduleLength));
		}
	}
	
    /**
     * Calculates the cost function f(s) by the following equation:
     * 		f(s) = max{f(s-1), Cpe(S), Bt(s)}
     * @param parentState Parent state of the new node
     * @param newNode New node to calculate the cost function of 
     * @return int representing the cost
     * @author Jessica Alcantara
     */
	public int calculateCostFunction(State parentState, Node newNode) {
		int parentCost = parentState.getCost();
		int boundedTime = calculateBoundedTime();
		int criticalPathEstimate = calculateCriticalPathEstimate();
		
		int maxCost = Math.max(parentCost, boundedTime);
		maxCost = Math.max(maxCost, criticalPathEstimate);
		return maxCost;
	}
	
	// TODO:
	public int calculateBoundedTime() {
		return 0;
	}
	
	// TODO:
	public int calculateCriticalPathEstimate() {
		return 0;
	}
	
	public Comparator<State> getStateComparator() {
		return _stateComparator;
	}
	

}
