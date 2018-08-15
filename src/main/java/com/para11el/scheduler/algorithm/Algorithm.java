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
	 * Constructor for Algorithm for sequential solution
	 * @param graph Input graph
	 * @param processor Number of processors
	 * 
	 * @author Jessica Alcantara
	 */
	public Algorithm(Graph graph, int processor) {
		_graph = graph;
		_processors = processor;
		_cores = 1;
	}

	/**
	 * Constructor for Algorithm for parallel solution
	 * @param graph Input graph
	 * @param processor Number of processors
	 * @param cores Number of cores
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
	
	/**
	 * Finds the earliest start time of a task on a processor with given dependencies
	 * @param node Node to be scheduled
	 * @param schedule ArrayList of scheduled tasks
	 * @param processor Processor to schedule the node on
	 * @return int of the earliest start time
	 * 
	 * @author Holly Hagenson, Rebekah Berriman
	 */
	public int getEarliestStartTime(Node node, ArrayList<Task> schedule, int processor) {
		int processorFinish; 
		int startTime = 0;
		int parentLatestFinish = 0;

		// Get the latest finish time of the parents
		for (Node parent : getParents(node)){
			Task task = findNodeTask(parent, schedule); 
			int nodeWeight = task.getWeight();

			if (task.getProcessor() == processor) {
				parentLatestFinish = task.getStartTime() + nodeWeight; 
			} else{
				int edgeWeight = ((Number)task.getNode().getEdgeToward(node)
						.getAttribute("Weight")).intValue();
				parentLatestFinish = task.getStartTime() + nodeWeight + edgeWeight;  
			}
			if (parentLatestFinish > startTime){
				startTime = parentLatestFinish; 
			}
		}
		
		// Get the latest finish time of the current processor
		processorFinish = getProcessorFinishTime(schedule, processor);
		if (processorFinish > startTime) {
			startTime = processorFinish;
		}

		return startTime;
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
	 * Returns a list of parent nodes of a node
	 * @param node Node to find parents of
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
	 * Returns the task corresponding to a node
	 * @param node From input graph
	 * @param schedule ArrayList of scheduled tasks
	 * @return Task object node
	 * 
	 * @author Sean Oldfield
	 */
	public Task findNodeTask(Node node, ArrayList<Task> schedule) {
		for (Task task : schedule) {
			if (task.getNode().equals(node)) {
				return task;
			}
		}
		return null;
	}
	
	/**
	 * Find the available nodes that can be scheduled given what nodes have already 
	 * been scheduled
	 * @param scheduledTasks
	 * @return ArrayList of available nodes
	 * 
	 * @author Rebekah Berriman, Tina Chen
	 */
	public ArrayList<Node> availableNode(ArrayList<Task> scheduledTasks) {
		ArrayList<Node> scheduledNodes =  new ArrayList<Node>();
		ArrayList<Node> available = new ArrayList<Node>();

		// Get list of scheduled nodes
		for (Task task : scheduledTasks) {
			scheduledNodes.add(task.getNode());
		}

		_graph.nodes().forEach((node) -> {
			// Find nodes that have not been scheduled
			if (!scheduledNodes.contains(node)) {
				ArrayList<Node> parents = getParents(node);
				
				// Add nodes with no parents to available nodes
				if (parents.size() == 0) {
					available.add(node);		
				// Check all the parents of the node have been scheduled
				} else {
					boolean availableNode = true;
					for (Node parentNode : parents) {
						// Node is not available if parent has not been scheduled
						if (!scheduledNodes.contains(parentNode)) {
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
