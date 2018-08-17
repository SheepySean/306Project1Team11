package com.para11el.scheduler.algorithm;

import java.util.ArrayList;

import org.graphstream.graph.Graph;

public class DFSInitialiser {
	
	protected Graph _graph;
	protected int _processors;
	protected int _cores;

	private int _minimumTime;
	
	/**
	 * Default constructor for DFS Algorithm
	 * 
	 * @author Tina Chen
	 */
	public DFSInitialiser() {
		super();
	}
	/**
	 * Constructor for DFS Algorithm for sequential solution
	 * @param graph of tasks to be scheduled
	 * @param processor number to schedule tasks on
	 * 
	 * @author Tina Chen
	 */
	public DFSInitialiser(Graph graph, int processor) {
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
	public DFSInitialiser(Graph graph, int processor, int cores) {
		_graph = graph;
		_processors = processor;
		_cores = cores;
	}
	
	private void initialise() {
		setMaximumTime();

		_graph.nodes().forEach((node) -> {
			if (node.getInDegree() == 0) {
				Task t = new Task(node, 0, 1);
				ArrayList<Task> solutionPart = new ArrayList<Task>();
				solutionPart.add(t);
				
				// add the fork join stuff here
				
				//buildRecursiveSolution(solutionPart);
				
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

}
