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
import com.para11el.scheduler.algorithm.State;
import com.para11el.scheduler.algorithm.Task;

/**
 * Junit test to test the validity and optimality of the A* Algorithm.
 * 
 * @author Jessica Alcantara
 *
 */
public class AStarAlgorithmIT {
	private static Graph _graph1;
	
	/**
	 * Unit test for the ascending ordering of the state comparator.
	 * @author Jessica Alcantara
	 */
	@Test
	public void testStateComparator() {
		State stateOne = new State();
		stateOne.setCost(4);
		State stateTwo = new State();
		stateTwo.setCost(12);
		State stateThree = new State();
		stateThree.setCost(9);
		
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
	 * @author Holly Hagenson
	 */
	@Test
	public void testCalculateTotalWeight() {
		Collection<Node> nodes = new ArrayList<Node>(); 
		nodes.add(new MockNode(null, "A", 2)); 
		nodes.add(new MockNode(null, "B", 4)); 
		nodes.add(new MockNode(null, "C", 3)); 
		nodes.add(new MockNode(null, "D", 5)); 
		nodes.add(new MockNode(null, "E", 3)); 
		
		AStarAlgorithm am = new AStarAlgorithm(); 
		int totalWeight = am.calculateTotalWeight(nodes); 
		
		assertEquals(totalWeight, 17); 	
	}
	
	/**
	 * Unit test for getting the earliest start time of a node. 
	 * @author Holly Hagenson
	 */
	@Test
	public void testGetEarliestStartTime() {		
		createGraph(); 
		
		ArrayList<Task> testSchedule = new ArrayList<Task>(); 
		testSchedule.add(new Task(_graph1.getNode("1"),0,1));
		testSchedule.add(new Task(_graph1.getNode("2"),3,1));
		testSchedule.add(new Task(_graph1.getNode("3"),5,2));
		
		AStarAlgorithm am = new AStarAlgorithm(); 
		int start = am.getEarliestStartTime(_graph1.getNode("4"), testSchedule, 1);
		
		assertEquals(start, 7); 
	}
	
	@Test
	public void testScheduleContainsNode() {
		ArrayList<Task> testSchedule = new ArrayList<Task>();
		Node node = new MockNode(null,"C",3);  
		testSchedule.add(new Task(new MockNode(null,"A",2),0,1));
		testSchedule.add(new Task(new MockNode(null,"B",2),1,2));
		testSchedule.add(new Task(node,0,3));
		testSchedule.add(new Task(new MockNode(null,"D",1),3,1));
		testSchedule.add(new Task(new MockNode(null,"E",1),4,2));
		testSchedule.add(new Task(new MockNode(null,"F",2),3,3));
		
		AStarAlgorithm am = new AStarAlgorithm();
		assertTrue(am.scheduleContainsNode(node, testSchedule));
	}
	
	/**@Test 
	public void testBuildSolution(){
		createGraph(); 
		
		AStarAlgorithm as = new AStarAlgorithm(_graph1, 2); 
		
		ArrayList<Task> solution = as.buildSolution(); 
		
		for (Task t : solution){
			if (t.getNode().getId().equals("1")){
				assertEquals(t.getStartTime(), 0);
				assertEquals(t.getProcessor(), 1);
			}
			if (t.getNode().getId().equals("2")){
				assertEquals(t.getStartTime(), 4);
				assertEquals(t.getProcessor(), 1);
			}
			if (t.getNode().getId().equals("3")){
				assertEquals(t.getStartTime(), 7);
				assertEquals(t.getProcessor(), 2);
			}
			if (t.getNode().getId().equals("4")){
				assertEquals(t.getStartTime(), 10);
				assertEquals(t.getProcessor(), 2);
			}
			if (t.getNode().getId().equals("5")){
				assertEquals(t.getStartTime(), 7);
				assertEquals(t.getProcessor(), 1);
			}
		}
	}**/
	
	public void createGraph(){
		_graph1 = new SingleGraph("graphWithMultipleProcessors");
		_graph1.addNode("1");
		_graph1.addNode("2");
		_graph1.addNode("3");
		_graph1.addNode("4");
		_graph1.addNode("5");
		_graph1.getNode("1").addAttribute("Weight", 3.0);
		_graph1.getNode("2").addAttribute("Weight", 4.0);
		_graph1.getNode("3").addAttribute("Weight", 2.0);
		_graph1.getNode("4").addAttribute("Weight", 1.0);
		_graph1.getNode("5").addAttribute("Weight", 5.0);
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
