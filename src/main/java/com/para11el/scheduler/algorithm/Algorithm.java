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
	
	/**
	 * Labels the graph with the startTime and processor numbers of each of the nodes for the optimal solution
	 * @param solution Final solution
	 * 
	 * @author Rebekah Berriman
	 */
	private void labelGraph(ArrayList<Task> solution) {
		for (Task task : solution) {
			Node node = task.getNode();
			node.setAttribute("Start", task.getStartTime());
			node.setAttribute("Processor", task.getProcessor());
		}
	}

	/**
	 * Returns the labeled graph.
	 * @param solution Final solution
	 * @return Graph of the nodes with labels
	 * 
	 * @author Rebekah Berriman
	 */
	public Graph getGraph(ArrayList<Task> solution) {
		labelGraph(solution);
		return _graph;
	}
	
	/**
	 * Returns an int of the finishTime of the last task on the processor
	 * @param currentSchedule is an ArrayList of the currently scheduled tasks
	 * @param processor to schedule it on
	 * @return int of the finishTime
	 * 
	 * @author Rebekah Berriman
	 */
	public int getProcessorFinishTime(ArrayList<Task> schedule, int processor) {
		int finishTime = 0;
		int processorFinish;

		for (Task task : schedule) {
			if (task.getProcessor() == processor) {
				processorFinish = task.getFinishTime();
				if (finishTime < processorFinish) {
					finishTime = processorFinish;
				}
			}	
		}
		return finishTime;
	}
	
	/**
	 * Return the latest finish time of a schedule
	 * @param schedule of tasks
	 * @return int of the latest finish time
	 * 
	 * @author Jessica Alcantara
	 */
	public int getScheduleFinishTime(ArrayList<Task> schedule) {
		int finishTime = 0;
		int processorFinish;
		
		for (int i=1; i<=_processors; i++)  {
			processorFinish = getProcessorFinishTime(schedule,i);
			if (processorFinish > finishTime) {
				finishTime = processorFinish;
			}
		}
		return finishTime;
	}
	
	/**
	 * Returns an ArrayList<Node> of the parents nodes a node has
	 * @param node the node to find parents of
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
	 * Returns the task corresponding to the node
	 * @param node in the inputGraph
	 * @param currentTasks 
	 * @return Task object node
	 * 
	 * @author Sean Oldfield
	 */
	public Task findNode(Node node, ArrayList<Task> currentTasks) {
		for (Task task : currentTasks) {
			if (task.getNode().equals(node)) {
				return task;
			}
		}
		return null;
	}
	
	/**
	 * Find the available nodes that can be scheduled given what nodes have already been scheduled.
	 * @param scheduledTasks
	 * @return ArrayList of available nodes
	 * 
	 * @author Rebekah Berriman, Tina Chen
	 */
	public ArrayList<Node> availableNode(ArrayList<Task> scheduledTasks) {
		ArrayList<Node> scheduledNodes =  new ArrayList<Node>();
		ArrayList<Node> available = new ArrayList<Node>();

		for (Task task : scheduledTasks) {
			scheduledNodes.add(task.getNode());
		}

		_graph.nodes().forEach((node) -> {
			if (!scheduledNodes.contains(node)) { // If Node is not already scheduled
				ArrayList<Node> parents = getParents(node);
				if (parents.size() == 0) { // Node has no parents so can be scheduled
					available.add(node);
				} else {
					boolean availableNode = true;
					for (Node parentNode : parents) {
						if (!scheduledNodes.contains(parentNode)) { // If the schedule does not contain a parent node of the node, set availableNode to false
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

}