package com.para11el.scheduler.algorithm;

import java.util.ArrayList;
import org.graphstream.graph.Graph;

/**
 * Abstract interface to represent the general concept of a scheduling algorithm. 
 * 
 * @author Jessica Alcantara
 * 
 */
public interface Algorithm {
	
	/**
	 * Method signature for producing and returning an optimal solution
	 * @return List of tasks representing the optimal solution
	 */
	public ArrayList<Task> buildSolution();
	
	/**
	 * Method signature for initializing the algorithm
	 */
	public void initialise();

	/**
	 * Method signature for returning a graph labeled with the optimal solution
	 * @param solution Schedule representing the optimal solution
	 * @return Graph of the optimal solution
	 */
	public Graph getGraph(ArrayList<Task> solution);

}
