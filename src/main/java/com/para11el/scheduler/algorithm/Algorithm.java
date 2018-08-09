package com.para11el.scheduler.algorithm;

import java.util.ArrayList;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

/**
 * Abstract superclass to represent the general concept of a scheduling algorithm. 
 * This class defines state common to all scheduling algorithm instances. Algorithm
 * subclasses must override the method buildSolution() to handle specific algorithm
 * specific scheduling.
 * 
 * @author Jessica Alcantara
 * 
 */
public abstract class Algorithm {
	
	protected Graph _graph;
	protected int _processors;
	protected int _cores;
	protected ArrayList<Task> _solution = new ArrayList<Task>();
	
	public Algorithm() {}
	
	/**
	 * Constructor for AlgorithmManager for sequential solution
	 * @param graph input graph
	 * @param processor number of processors
	 * 
	 * @author Jessica Alcantara
	 */
	public Algorithm(Graph graph, int processor) {
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
	public Algorithm(Graph graph, int processor, int cores) {
		_graph = graph;
		_processors = processor;
		_cores = cores;
	}
	
	public abstract ArrayList<Task> buildSolution();
	
	/**
	 * Labels the graph with the startTime and processor numbers of each of the nodes for the optimal solution
	 * 
	 * @author Rebekah Berriman
	 */
	private void labelGraph() {
		for (Task task : _solution) {
			Node node = task.getNode();
			node.addAttribute("Start", task.getStartTime());
			node.addAttribute("Processor", task.getProcessor());
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
