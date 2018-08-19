package com.para11el.scheduler.algorithm;

import java.util.ArrayList;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

/**
 * Abstract interface to represent the general concept of a scheduling algorithm. 
 * 
 * @author Jessica Alcantara, Sean Oldfield
 * 
 */
public interface Algorithm {
	
	/**
	 * Method signature for producing and returning an optimal solution
	 * @return List of tasks representing the optimal solution
	 */
	ArrayList<Task> buildSolution();
	
	/**
	 * Method signature for initializing the algorithm
	 */
	void initialise();

	/**
	 * Returns and labels the graph with the startTime and processor numbers of each of the
	 * nodes for the optimal solution
	 * @param solution Final solution
	 * @return graph of the nodes with labels
	 *
	 * @author Rebekah Berriman
	 */
	default Graph getGraph(ArrayList<Task> solution) {
		for (Task task : solution) {
			Node node = task.getNode();
			node.setAttribute("Start", task.getStartTime());
			node.setAttribute("Processor", task.getProcessor());
		}
		return getGSGraph();
	}

    /**
     * Method signature for getting a corresponding graph associated with
     * an Algorithm
     * @return GraphStream graph
     */
	Graph getGSGraph();

}
