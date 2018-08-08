package com.para11el.scheduler.algorithm.test;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

/**
 * Manager class that creates example Graph objects for testing.
 *
 * @author Jessica Alcantara
 */
public class TestGraphManager {
	
	/**
	 * Create example graph with a single exit and exit.
	 * @author Jessica Alcantara
	 */
	public Graph createSingleEntrySingleExit() {
		Graph graph = new SingleGraph("Single");
		graph.addNode("a").addAttribute("Weight", 2);
		graph.addNode("b").addAttribute("Weight", 1);
		graph.addNode("c").addAttribute("Weight", 1); 
		graph.addNode("d").addAttribute("Weight", 2);
		graph.addEdge("a -> b", "a", "b", true)
		.setAttribute("Weight", 4);
		graph.addEdge("a -> c", "a", "c", true)
		.setAttribute("Weight", 2);
		graph.addEdge("b -> d", "b", "d", true)
		.setAttribute("Weight", 12);
		graph.addEdge("c -> d", "c", "d", true)
		.setAttribute("Weight", 24);
		return graph;
	}
	
	/**
	 * Create example graph with a single entry and multiple exits.
	 * @author Jessica Alcantara
	 */
	public Graph createSingleEntryMultipleExit() {
		Graph graph = new SingleGraph("Single Entry");
		graph.addNode("a").addAttribute("Weight", 2);
		graph.addNode("b").addAttribute("Weight", 1);
		graph.addNode("c").addAttribute("Weight", 3); 
		graph.addNode("d").addAttribute("Weight", 2);
		graph.addNode("e").addAttribute("Weight", 1);
		graph.addEdge("a -> b", "a", "b", true)
		.setAttribute("Weight", 4);
		graph.addEdge("b -> c", "b", "c", true)
		.setAttribute("Weight", 9);
		graph.addEdge("c -> d", "c", "d", true)
		.setAttribute("Weight", 2);
		graph.addEdge("a -> d", "a", "d", true)
		.setAttribute("Weight", 5);
		graph.addEdge("a -> e", "a", "e", true)
		.setAttribute("Weight", 2);
		return graph;
	}
	
	/**
	 * Create example graph with a single exit and multiple entries.
	 * @author Jessica Alcantara
	 */
	public Graph createSingleExitMultipleEntry() {
		Graph graph = new SingleGraph("Single Exit");
		graph.addNode("a").addAttribute("Weight", 2);
		graph.addNode("b").addAttribute("Weight", 1);
		graph.addNode("c").addAttribute("Weight", 4); 
		graph.addNode("d").addAttribute("Weight", 2);
		graph.addEdge("a -> c", "a", "c", true)
		.setAttribute("Weight", 2);
		graph.addEdge("a -> d", "a", "d", true)
		.setAttribute("Weight", 5);
		graph.addEdge("b -> c", "b", "c", true)
		.setAttribute("Weight", 4);
		return graph;
	}

}
