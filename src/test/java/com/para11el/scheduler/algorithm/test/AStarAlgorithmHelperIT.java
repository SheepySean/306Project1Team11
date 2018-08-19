package com.para11el.scheduler.algorithm.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.junit.BeforeClass;
import org.junit.Test;

import com.para11el.scheduler.algorithm.AStarAlgorithm;
import com.para11el.scheduler.algorithm.State;
import com.para11el.scheduler.algorithm.Task;

/**
 * JUnit test class to test the behaviour of the A* Algorithm helper methods.
 * 
 * @author Holly Hagenson, Jessica Alcantara
 *
 */
public class AStarAlgorithmHelperIT {
	
	private static TestGraphManager _tgManager;  
	private static Graph _testGraph; 
	private static AStarAlgorithm _aStar; 
	private int _processors;
	
	/**
	 * Create graph to test output.
	 */
	@BeforeClass
	public static void initialise(){
		_tgManager = new TestGraphManager(); 
		_testGraph = _tgManager.createGraph(); 
		_aStar = new AStarAlgorithm(); 
	}
	
	/**
	 * Unit test for getting available nodes. 
	 * @author Jessica Alcantara
	 */
	@Test
	public void testAvailableNodes() {		
		// Create test schedule
		ArrayList<Task> testSchedule = new ArrayList<Task>(); 
		testSchedule.add(new Task(_testGraph.getNode("1"),0,1));
		testSchedule.add(new Task(_testGraph.getNode("2"),3,1));
		testSchedule.add(new Task(_testGraph.getNode("5"),12,2));
		
		List<Node> expected = new ArrayList<Node>();
		expected.add(_testGraph.getNode("3"));
		expected.add(_testGraph.getNode("4"));
		
		AStarAlgorithm aStar = new AStarAlgorithm(_testGraph,2);
		List<Node> available = aStar.getAvailableNodes(testSchedule);
		
		assertEquals(expected, available); 
	}
	
	/**
	 * Unit test for getting the parents of a node
	 * @author Jessica Alcantara
	 */
	@Test
	public void testGetParents() {	
		ArrayList<Node> expectedParents = new ArrayList<Node>();
		expectedParents.add(_testGraph.getNode("1"));
		
		List<Node> foundParents = _aStar.getParents(_testGraph.getNode("2"));
		assertEquals(expectedParents, foundParents); 
	}
	
	/**
	 * Unit test for getting the graph labeled with the solution
	 * @author Jessica Alcantara
	 */
	@Test
	public void testGetLabeledGraph() {		
		// Create test schedule
		ArrayList<Task> testSchedule = new ArrayList<Task>(); 
		testSchedule.add(new Task(_testGraph.getNode("1"),0,1));
		testSchedule.add(new Task(_testGraph.getNode("2"),3,1));
		testSchedule.add(new Task(_testGraph.getNode("3"),7,1));
		testSchedule.add(new Task(_testGraph.getNode("4"),9,1));
		testSchedule.add(new Task(_testGraph.getNode("5"),10,1));

		AStarAlgorithm aStar = new AStarAlgorithm(_testGraph,1);
		_testGraph = aStar.getGraph(testSchedule);
		
		assertEquals(_testGraph.getNode("1").getAttributeCount(), 3); 
		assertEquals(_testGraph.getNode("2").getAttribute("Start"), 3); 
		assertEquals(_testGraph.getNode("3").getAttribute("Processor"), 1); 
		
		_testGraph = _tgManager.returnToOriginal();
	}
	
	/**
	 * Unit test for the ascending ordering of the state comparator.
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
	 * Unit test for expanding state
	 * @author Jessica Alcantara
	 */
	@Test
	public void testExpandState(){
		_processors = 2;
		_aStar = new AStarAlgorithm(_testGraph, _processors);
		
		// Create state schedule
		ArrayList<Task> stateSchedule = new ArrayList<Task>(); 
		stateSchedule.add(new Task(_testGraph.getNode("1"),0,1));
		
		// Create state
		State state = new State(_testGraph.getNode("1"),null,stateSchedule,0);
		_aStar.expandState(state);
		
		// Map state schedules
		HashMap<Integer,Task> stateMap = new HashMap<Integer,Task>();
		Queue<State> states = _aStar.getStates();
		for (State s : states) {
			if (s.getSchedule().size()>1) {
				int key = s.getCost();
				Task addedTask = s.getSchedule().get(1);
				stateMap.put(key, addedTask);
			}
		}
		
		// Costs of each state are: 7, 10, 12, 13
		assertEquals(stateMap.get(7).getStartTime(),3);  // Node 3 on Processor 1
		assertEquals(stateMap.get(10).getStartTime(),5); // Node 3 on Processor 2
		assertEquals(stateMap.get(12).getStartTime(),3); // Node 5 on Processor 1
		assertEquals(stateMap.get(13).getStartTime(),4); // Node 5 on Processor 2
	}
}
