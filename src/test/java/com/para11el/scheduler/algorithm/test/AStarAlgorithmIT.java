package com.para11el.scheduler.algorithm.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.PriorityQueue;
import java.util.Queue;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.Test;

import com.para11el.scheduler.algorithm.AStarAlgorithm;
import com.para11el.scheduler.algorithm.PruningManager;
import com.para11el.scheduler.algorithm.State;
import com.para11el.scheduler.algorithm.Task;

/**
 * JUnit test to test the validity and optimality of the A* Algorithm.
 * 
 * @author Jessica Alcantara
 *
 */
public class AStarAlgorithmIT {
	private static Graph _graph1;
	
	/**
	 * Unit test for the ascending ordering of the state comparator.
	 * 
	 * @author Jessica Alcantara
	 */
	@Test
	public void testStateComparator() {
		// Create states and set their costs
		State stateOne = new State();
		stateOne.setCost(4);
		State stateTwo = new State();
		stateTwo.setCost(12);
		State stateThree = new State();
		stateThree.setCost(9);
		
		// 
		AStarAlgorithm am = new AStarAlgorithm();
		Queue<State> states = new PriorityQueue<State>(am.getStateComparator());
		states.add(stateOne);
		states.add(stateTwo);
		states.add(stateThree);
		
		assertTrue(states.poll().getCost() == 4);
		assertTrue(states.poll().getCost() == 9);
		assertTrue(states.poll().getCost() == 12);
	}
	
	/**
	 * Unit test for calculating the total weight of nodes in a graph.
	 * 
	 * @author Holly Hagenson
	 */
	@Test
	public void testCalculateTotalWeight() {
		// Create collection of nodes to find total weight of
		Collection<Node> nodes = new ArrayList<Node>(); 
		nodes.add(new MockNode(null, "A", 2)); 
		nodes.add(new MockNode(null, "B", 4)); 
		nodes.add(new MockNode(null, "C", 3)); 
		nodes.add(new MockNode(null, "D", 5)); 
		nodes.add(new MockNode(null, "E", 3)); 
		
		AStarAlgorithm am = new AStarAlgorithm(); 
		int totalWeight = am.calculateTotalWeight(nodes.stream()); 
		
		assertEquals(totalWeight, 17); 	
	}
	
	/**
	 * Unit test for getting the earliest start time of a node. 
	 * 
	 * @author Holly Hagenson
	 */
	@Test
	public void testGetEarliestStartTime() {		
		createGraph(); 
		
		// Create test schedule to get earliest start time of
		ArrayList<Task> testSchedule = new ArrayList<Task>(); 
		testSchedule.add(new Task(_graph1.getNode("1"),0,1));
		testSchedule.add(new Task(_graph1.getNode("2"),3,1));
		testSchedule.add(new Task(_graph1.getNode("3"),5,2));
		
		AStarAlgorithm am = new AStarAlgorithm(); 
		int start = am.getEarliestStartTime(_graph1.getNode("4"), testSchedule, 1);
		
		assertEquals(start, 7); 
	}
	
	/**
	 * Unit test to check for duplicate states within priority queue.
	 * 
	 * @author Holly Hagenson
	 */
	@Test
	public void testCheckDuplicates(){
		// Create test schedules and states to compare against each other
		ArrayList<Task> testSchedule = new ArrayList<Task>();  
		Node nodeB  = new MockNode(null,"B",2);
		testSchedule.add(new Task(new MockNode(null,"A",2),0,1));
		testSchedule.add(new Task(nodeB,1,2));
		testSchedule.add(new Task(new MockNode(null,"C",3),0,2));
		testSchedule.add(new Task(new MockNode(null,"D",1),3,1));
		
		State newState = new State(nodeB, null, testSchedule, 12); 
		
		ArrayList<Task> testSchedule1 = new ArrayList<Task>();  
		Node node2  = new MockNode(null,"B",2);
		testSchedule1.add(new Task(new MockNode(null,"A",2),0,1));
		testSchedule1.add(new Task(node2,1,2));
		testSchedule1.add(new Task(new MockNode(null,"C",3),0,2));
		testSchedule1.add(new Task(new MockNode(null,"D",1),3,1));
		
		State queuedState = new State(node2, null, testSchedule1, 12);
		
		AStarAlgorithm am = new AStarAlgorithm();
		Queue<State> states = new PriorityQueue<State>(am.getStateComparator());
		states.add(queuedState);
		
		PruningManager pm = new PruningManager();
		assertTrue(pm.doPrune(newState, states)); 
	}
	
	/**
	 * Unit test to check finish time of valid, optimal solution when multiple 
	 * processors are used.
	 * 
	 * @author Holly Hagenson
	 */
	@Test 
	public void testMultiBuildSolutionFinish(){
		createGraph(); 
		
		AStarAlgorithm as = new AStarAlgorithm(_graph1, 2); 
		
		ArrayList<Task> solution = as.buildSolution(); 
		
		int maxFinish = 0;
		
		// For all tasks in the solution, find latest finish time
		for (Task t : solution){
			int nodeWeight = ((Number)t.getNode().getAttribute("Weight")).intValue();
			int finishTime = t.getStartTime() + nodeWeight;
			if (finishTime > maxFinish){
				maxFinish = finishTime; 
			}
		}
		
		assertEquals(maxFinish, 12); 
	}
	
	/**
	 * Unit test to check finish time of valid, optimal solution when a single 
	 * processor is used.
	 * 
	 * @author Holly Hagenson
	 */
	@Test 
	public void testSingleBuildSolutionFinish(){
		createGraph(); 
		
		AStarAlgorithm as = new AStarAlgorithm(_graph1, 1); 
		
		ArrayList<Task> solution = as.buildSolution(); 
		
		int maxFinish = 0;
		
		// For all tasks in the solution, find latest finish time
		for (Task t : solution){
			int nodeWeight = ((Number)t.getNode().getAttribute("Weight")).intValue();
			int finishTime = t.getStartTime() + nodeWeight;
			if (finishTime > maxFinish){
				maxFinish = finishTime; 
			}
		}
		
		assertEquals(maxFinish, 15); 
	}
	
	/**
	 * Unit test for checking attributes of nodes on output graph.
	 * 
	 * @author Holly Hagenson
	 */
	@Test
	public void testOutputGraph(){
		createGraph(); 

		AStarAlgorithm as = new AStarAlgorithm(_graph1, 2); 

		ArrayList<Task> solution = as.buildSolution();
		
		Graph outputGraph = as.getGraph(solution);
		
		//Iterate through the node set of the output graph
		outputGraph.nodes().forEach((node) -> {
			//Each node should have three attributes
			assertEquals(3, node.getAttributeCount());

			//Check that each node has the attributes we expect
			assertTrue(node.getAttribute("Weight")!=null);
			assertTrue(node.getAttribute("Start")!=null);
			assertTrue(node.getAttribute("Processor")!=null);
		});			
			
	}
	
	/**
	 * Method to create a GraphStream graph to use for testing purposes
	 * 
	 * @author Holly Hagenson
	 */
	public void createGraph(){
		_graph1 = new SingleGraph("graphWithMultipleProcessors");
		_graph1.addNode("1");
		_graph1.addNode("2");
		_graph1.addNode("3");
		_graph1.addNode("4");
		_graph1.addNode("5");
		_graph1.getNode("1").setAttribute("Weight", 3.0);
		_graph1.getNode("2").setAttribute("Weight", 4.0);
		_graph1.getNode("3").setAttribute("Weight", 2.0);
		_graph1.getNode("4").setAttribute("Weight", 1.0);
		_graph1.getNode("5").setAttribute("Weight", 5.0);
		_graph1.addEdge("1 -> 2", "1", "2", true);
		_graph1.addEdge("1 -> 3", "1", "3", true);
		_graph1.addEdge("2 -> 4", "2", "4", true);
		_graph1.addEdge("2 -> 5", "2", "5", true);
		_graph1.getEdge("1 -> 2").setAttribute("Weight", 1.0);
		_graph1.getEdge("1 -> 3").setAttribute("Weight", 2.0);
		_graph1.getEdge("2 -> 4").setAttribute("Weight", 3.0);
		_graph1.getEdge("2 -> 5").setAttribute("Weight", 5.0);
	}
}
