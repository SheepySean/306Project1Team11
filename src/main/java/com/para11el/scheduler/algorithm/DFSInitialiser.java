package com.para11el.scheduler.algorithm;

import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

public class DFSInitialiser {

	protected Graph _graph;
	protected int _processors;
	protected int _cores;
	//private ForkJoinPool forkJoinPool ;

	private int _minimumTime;
	
	private ArrayList<ArrayList<Task>> _optimal = new ArrayList<ArrayList<Task>>();
	private ArrayList<Task> _solution;

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
		
		initialise();
		
	}

	public void initialise() {
		setMaximumTime();
		OptimalDFSSchedule preserveOptimal = OptimalDFSSchedule.getInstance();
		preserveOptimal.initialise(_minimumTime);
		

		ForkJoinPool forkJoinPool = new ForkJoinPool(_cores);
		
		int nodeCount=0;
		int countingLoops = 0;
		
		_graph.nodes().forEach((node) -> {
			if (node.getInDegree() == 0) {
			//	nodeCount++;
			}
		});
		
		_graph.nodes().forEach((node) -> {
			if (node.getInDegree() == 0) {
				Task t = new Task(node, 0, 1);
				ArrayList<Task> solutionPart = new ArrayList<Task>();
				solutionPart.add(t);

				// add the fork join stuff here

				

				DFSForkJoin dfsForkJoin = new DFSForkJoin(_graph, _processors,
						_cores, _minimumTime, solutionPart);

				forkJoinPool.invoke(dfsForkJoin);
				
				ArrayList<Task> sub = dfsForkJoin.getSolution();
				_optimal.add(sub);
				
				//countingLoops++;
			}
		});
		
		
		
		while (!forkJoinPool.isQuiescent()) {
			//Make sure it does not leave until it is done!
		}
	
	}
	
	public ArrayList<Task> buildSolution() {
		
		
		
		OptimalDFSSchedule preserveOptimal = OptimalDFSSchedule.getInstance();
		System.out.println(preserveOptimal.getOptimalSchedule());
		return preserveOptimal.getOptimalSchedule();
		//getOptimal();
		//return _solution;
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
	 * Searches the possible solutions for the optimal solution (earliest finish time)
	 * Returns a full task schedule solution with the shortest completion time.
	 * @return an ArrayList<Task> of the optimal solution
	 * 
	 * @author Rebekah Berriman
	 */
	private void getOptimal() {
		int minimumTime=0;

		for (int possibleSolution=0; possibleSolution<_optimal.size(); possibleSolution++) {
			//Set the finish time of this solution
			int solutionTime = 0; 
			for (int processor=0; processor <= _processors; processor++) {
				int possibleSolutionTime = getProcessorFinishTime(_optimal.get(possibleSolution), processor);
				//if the finishTime of one processor is later than another, update the finish time of the task
				if (solutionTime < possibleSolutionTime) {
					solutionTime = possibleSolutionTime;
				}
			}

			//if the minimum run time is still zero, or the solution time is less than the minimal time, update it
			if (minimumTime == 0 || minimumTime > solutionTime ) {
				minimumTime = solutionTime;
				_solution = _optimal.get(possibleSolution);
			}

			//System.out.println("Optimal solution: " + _solution);
		}
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
	 * Returns and labels the graph with the startTime and processor numbers of each of the
	 * nodes for the optimal solution
	 * @param solution Final solution
	 * @return graph of the nodes with labels
	 * 
	 * @author Rebekah Berriman
	 */
	public Graph getGraph(ArrayList<Task> solution) {
		System.out.println("solution size = " + solution.size());
		for (Task task : solution) {
			Node node = task.getNode();
			node.setAttribute("Start", task.getStartTime());
			node.setAttribute("Processor", task.getProcessor());
		}
		return _graph;
	}
}
