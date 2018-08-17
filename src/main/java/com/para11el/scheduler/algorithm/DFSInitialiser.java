package com.para11el.scheduler.algorithm;

import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

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

	/**
	 * Begin the DFS algorithm using parallelisation for 
	 * each of the root nodes
	 * 
	 * @author Tina Chen
	 */
	public void initialise() {
		setMaximumTime();
		OptimalSchedule preserveOptimal = OptimalSchedule.getInstance();
		preserveOptimal.initialise(_minimumTime);
		
		ForkJoinPool forkJoinPool = new ForkJoinPool(_cores);
		
		_graph.nodes().forEach((node) -> {
			if (node.getInDegree() == 0) {
				Task t = new Task(node, 0, 1);
				ArrayList<Task> rootNodeSchedule = new ArrayList<Task>();
				rootNodeSchedule.add(t);

				// create new parallelisation class DFSForkJoin
				DFSForkJoin dfsForkJoin = new DFSForkJoin(_graph, _processors,
						_cores, rootNodeSchedule);

				forkJoinPool.invoke(dfsForkJoin);	
			}
		});
		
		while (!forkJoinPool.isQuiescent()) {
			//Make all threads have completed before
			//leaving the function
		}	
	}
	
	/**
	 * 
	 * @return the optimal schedule
	 * 
	 * @author Tina Chen
	 */
	public ArrayList<Task> buildSolution() {
		//Build the solution recursively
		initialise();
		
		//Gets the schedule from the instance
		OptimalSchedule preserveOptimal = OptimalSchedule.getInstance();
		return preserveOptimal.getOptimalSchedule();
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
	 * Returns and labels the graph with the startTime and processor numbers of each of the
	 * nodes for the optimal solution
	 * @param solution Final solution
	 * @return graph of the nodes with labels
	 * 
	 * @author Rebekah Berriman
	 */
	public Graph getGraph(ArrayList<Task> solution) {
		for (Task task : solution) {
			Node node = task.getNode();
			node.setAttribute("Start", task.getStartTime());
			node.setAttribute("Processor", task.getProcessor());
		}
		return _graph;
	}
}
