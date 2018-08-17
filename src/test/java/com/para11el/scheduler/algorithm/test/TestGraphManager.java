package com.para11el.scheduler.algorithm.test;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

/**
 * Manager class that creates example Graph objects for testing.
 *
 * @author Holly Hagenson
 */
public class TestGraphManager {
	
	public TestGraphManager() {}
	
	/**
	 * Create example graph to use for testing purposes.
	 * 
	 * @author Holly Hagenson
	 */
	public Graph createGraph() {
		Graph testGraph = new SingleGraph("testGraph");
		testGraph.addNode("1");
		testGraph.addNode("2");
		testGraph.addNode("3");
		testGraph.addNode("4");
		testGraph.addNode("5");
		testGraph.getNode("1").setAttribute("Weight", 3);
		testGraph.getNode("2").setAttribute("Weight", 4);
		testGraph.getNode("3").setAttribute("Weight", 2);
		testGraph.getNode("4").setAttribute("Weight", 1);
		testGraph.getNode("5").setAttribute("Weight", 5);
		testGraph.addEdge("1 -> 2", "1", "2", true);
		testGraph.addEdge("1 -> 3", "1", "3", true);
		testGraph.addEdge("2 -> 4", "2", "4", true);
		testGraph.addEdge("2 -> 5", "2", "5", true);
		testGraph.getEdge("1 -> 2").setAttribute("Weight", 1);
		testGraph.getEdge("1 -> 3").setAttribute("Weight", 2);
		testGraph.getEdge("2 -> 4").setAttribute("Weight", 3);
		testGraph.getEdge("2 -> 5").setAttribute("Weight", 5);
		return testGraph; 
	}
	
	/**
	 * Add a node to a test graph.
	 * @param nodeId Id of node to add
	 * @param nodeWeight Weight value of node to add
	 * @param graph Graph to add node to 
	 * @return Graph with node added
	 * 
	 * @author Holly Hagenson
	 */
	private Graph addNode(String nodeId, int nodeWeight, Graph graph){
		graph.addNode(nodeId);
		graph.getNode(nodeId).setAttribute("Weight", nodeWeight);
		return graph; 
	}
	
	/**
	 * Delete a node from a test graph.
	 * @param nodeId Id of node to delete
	 * @param graph Graph to delete node from
	 * @return Graph with node deleted
	 * 
	 * @author Holly Hagenson
	 */
	private Graph deleteNode(String nodeId, Graph graph){
		graph.removeNode(nodeId); 
		return graph; 
	}
	
	/**
	 * Add an edge to a test graph.
	 * @param edgeId Id of edge to add
	 * @param fromNode Node edge is coming from
	 * @param toNode Node edge is going to
	 * @param edgeWeight Weight value of edge to add
	 * @param graph Graph to add edge to
	 * @return Graph with edge added
	 * 
	 * @author Holly Hagenson
	 */
	private Graph addEdge(String edgeId, String fromNode, String toNode, int edgeWeight, Graph graph){
		graph.addEdge(edgeId, fromNode, toNode, true);
		graph.getEdge(edgeId).setAttribute("Weight", edgeWeight);
		return graph;
	}
	
	/**
	 * Delete an edge from a test graph.
	 * @param edgeId Id of edge to delete
	 * @param graph Graph to delete graph from
	 * @return Graph with edge deleted
	 * 
	 * @author Holly Hagenson
	 */
	private Graph deleteEdge(String edgeId, Graph graph){
		Edge edge = graph.getEdge(edgeId);
		graph.removeEdge(edge);
		return graph; 
	}
	
	/**
	 * Modify test graph to have multiple entry nodes and a single exit
	 * @param graph to modify 
	 * @return Graph with multiple entries
	 * 
	 * @author Holly Hagenson
	 */
	public Graph createMultiEntry(Graph graph){
		graph = addNode("6", 3, graph);
		graph = addEdge("6 -> 2", "6", "2", 3, graph);
		graph = deleteEdge("1 -> 3", graph);
		graph = deleteEdge("2 -> 5", graph);
		graph = deleteNode("3", graph);
		graph = deleteNode("5", graph);
		
		return graph;
	}
	
	/**
	 * Modify test graph to have multiple exit nodes and a single entry
	 * @param graph to modify
	 * @return Graph with multiple exits
	 * 
	 * @author Holly Hagenson
	 */
	public Graph createMultiExit(Graph graph){
		graph = deleteEdge("1 -> 3", graph);
		graph = deleteNode("3", graph); 
		
		return graph; 
	}
	
	/**
	 * Modify test graph to create a sequential graph
	 * @param graph to modify
	 * @return Graph in sequential form
	 * 
	 * @author Holly Hagenson
	 */
	public Graph createSequential(Graph graph){
		graph = deleteEdge("1 -> 3", graph);
		graph = deleteEdge("2 -> 5", graph);
		graph = deleteNode("3", graph);
		graph = deleteNode("5", graph);
		
		return graph;
	}
	
	/**
	 * Return graph to original test graph format
	 * @return Graph in original format
	 * 
	 * @author Holly Hagenson
	 */
	public Graph returnToOriginal(){
		Graph graph = createGraph(); 
		
		return graph; 
	}

}
