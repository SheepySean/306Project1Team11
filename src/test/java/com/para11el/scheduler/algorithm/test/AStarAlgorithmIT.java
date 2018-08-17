package com.para11el.scheduler.algorithm.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.BeforeClass;
import org.junit.Test;

import com.para11el.scheduler.algorithm.AStarAlgorithm;
import com.para11el.scheduler.algorithm.AStarAlgorithm;
import com.para11el.scheduler.algorithm.PruningManager;
import com.para11el.scheduler.algorithm.State;
import com.para11el.scheduler.algorithm.Task;

/**
 * Junit test to test the validity and optimality of the A* Algorithm.
 * 
 * @author Jessica Alcantara
 *
 */
public class AStarAlgorithmIT {
	private static Graph _testGraph;
	private AStarAlgorithm _aStar;
	private static SolutionValidity _validity;
	private static TestGraphManager _tgManager;
	private int _processors;
	private ArrayList<Task> _tasks = new ArrayList<Task>();
	
	/**
	 * Create graph to test output.
	 */
	@BeforeClass
	public static void initialise(){
		_validity = new SolutionValidity(); 
		_tgManager = new TestGraphManager(); 
		_testGraph = _tgManager.createGraph(); 
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
			int key = s.getCost();
			Task addedTask = s.getSchedule().get(1);
			stateMap.put(key, addedTask);
		}
		
		// Costs of each state are: 7, 10, 12, 13
		assertEquals(stateMap.get(7).getStartTime(),3);  // Node 3 on Processor 1
		assertEquals(stateMap.get(10).getStartTime(),5); // Node 3 on Processor 2
		assertEquals(stateMap.get(12).getStartTime(),3); // Node 5 on Processor 1
		assertEquals(stateMap.get(13).getStartTime(),4); // Node 5 on Processor 2
	}
	
	/**
	 * Test output is correct for graph with multiple entry nodes on a single processor.
	 * @author Holly Hagenson
	 */
	@Test
	public void testMultipleEntryNodes(){
		_processors = 1;
		
		_testGraph = _tgManager.createMultiEntry(_testGraph);
		
		_aStar = new AStarAlgorithm(_testGraph, _processors);
		_tasks = _aStar.buildSolution();
		
		// Check finish time of optimal schedule
		assertEquals(11, _validity.getOptimalFinishTime(_tasks));
		
		// Check that tasks do not overlap
		assertTrue(_validity.noTaskOverlap(_tasks));
		
		// Check that tasks are ordered correctly
		TreeMap<String,Integer> mappedNodes = _validity.mapTasksWithStartTime(_tasks);
		assertTrue(_validity.isBefore(mappedNodes.get("1"), mappedNodes.get("2")));
		assertTrue(_validity.isBefore(mappedNodes.get("6"), mappedNodes.get("2")));
		assertTrue(_validity.isBefore(mappedNodes.get("2"), mappedNodes.get("4")));
		
		_testGraph = _tgManager.returnToOriginal(); 
	}
	
	/**
	 * Test output is correct for graph with multiple entry nodes on two processors.
	 * @author Holly Hagenson
	 */
	@Test
	public void testMultipleEntryNodesMultiProcessor(){
		_processors = 2;
		
		_testGraph = _tgManager.createMultiEntry(_testGraph);
		
		_aStar = new AStarAlgorithm(_testGraph, _processors);
		_tasks = _aStar.buildSolution();
		
		// Check finish time of optimal schedule
		assertEquals(9, _validity.getOptimalFinishTime(_tasks));
		
		// Check that tasks do not overlap
		ArrayList<Task> proc1Tasks = _validity.tasksOnSameProcessor(_tasks, 1);
		assertTrue(_validity.noTaskOverlap(proc1Tasks));
		ArrayList<Task> proc2Tasks = _validity.tasksOnSameProcessor(_tasks, 2);
		assertTrue(_validity.noTaskOverlap(proc2Tasks));
		
		// Check that tasks are ordered correctly
		TreeMap<String,Integer> mappedNodes = _validity.mapTasksWithStartTime(_tasks);
		assertTrue(_validity.isBefore(mappedNodes.get("1"), mappedNodes.get("2")));
		assertTrue(_validity.isBefore(mappedNodes.get("6"), mappedNodes.get("2")));
		assertTrue(_validity.isBefore(mappedNodes.get("2"), mappedNodes.get("4")));
		
		_testGraph = _tgManager.returnToOriginal(); 
	}
	
	/**
	 * Test output is correct for graph with multiple exit nodes on a single processor
	 * @author Holly Hagenson
	 */
	@Test
	public void testMultipleExitNodes(){
		_processors = 1; 
		
		_testGraph = _tgManager.createMultiExit(_testGraph);
		
		_aStar = new AStarAlgorithm(_testGraph, _processors);
		_tasks = _aStar.buildSolution();
		
		// Check finish time of optimal schedule
		assertEquals(13, _validity.getOptimalFinishTime(_tasks));
		
		// Check that tasks do not overlap
		assertTrue(_validity.noTaskOverlap(_tasks));
		
		// Check that tasks are ordered correctly
		TreeMap<String,Integer> mappedNodes = _validity.mapTasksWithStartTime(_tasks);
		assertTrue(_validity.isBefore(mappedNodes.get("1"), mappedNodes.get("2")));
		assertTrue(_validity.isBefore(mappedNodes.get("2"), mappedNodes.get("5")));
		assertTrue(_validity.isBefore(mappedNodes.get("2"), mappedNodes.get("4")));
		
		_testGraph = _tgManager.returnToOriginal(); 
		
	}
	
	/**
	 * Test output is correct for graph with multiple exit nodes on multiple processors
	 * @author Holly Hagenson
	 */
	@Test
	public void testMultipleExitNodesMultiProcessor(){
		_processors = 2; 
		
		_testGraph = _tgManager.createMultiExit(_testGraph);
		
		_aStar = new AStarAlgorithm(_testGraph, _processors);
		_tasks = _aStar.buildSolution();
		
		// Check finish time of optimal schedule
		assertEquals(12, _validity.getOptimalFinishTime(_tasks));
		
		// Check that tasks do not overlap
		ArrayList<Task> proc1Tasks = _validity.tasksOnSameProcessor(_tasks, 1);
		assertTrue(_validity.noTaskOverlap(proc1Tasks));
		ArrayList<Task> proc2Tasks = _validity.tasksOnSameProcessor(_tasks, 2);
		assertTrue(_validity.noTaskOverlap(proc2Tasks));

		// Check that tasks are ordered correctly
		TreeMap<String,Integer> mappedNodes = _validity.mapTasksWithStartTime(_tasks);
		assertTrue(_validity.isBefore(mappedNodes.get("1"), mappedNodes.get("2")));
		assertTrue(_validity.isBefore(mappedNodes.get("2"), mappedNodes.get("5")));
		assertTrue(_validity.isBefore(mappedNodes.get("2"), mappedNodes.get("4")));
		
		_testGraph = _tgManager.returnToOriginal(); 
		
	}
	
	/**
	 * Test that the finish time of a sequential graph on a single processor is the 
	 * actual finish time of the optimal solution.
	 *  @author Holly Hagenson
	 */
	@Test 
	public void testSequentialGraphSingle(){
		_processors = 1;
		
		_testGraph = _tgManager.createSequential(_testGraph);
		
		_aStar = new AStarAlgorithm(_testGraph, _processors);
		_tasks = _aStar.buildSolution();
		
		// Check finish time of optimal schedule
		assertEquals(8, _validity.getOptimalFinishTime(_tasks));
		
		// Check that tasks do not overlap
		assertTrue(_validity.noTaskOverlap(_tasks));

		// Check that tasks are ordered correctly
		TreeMap<String,Integer> mappedNodes = _validity.mapTasksWithStartTime(_tasks);
		assertTrue(_validity.isBefore(mappedNodes.get("1"), mappedNodes.get("2")));
		assertTrue(_validity.isBefore(mappedNodes.get("2"), mappedNodes.get("4")));
		
		_testGraph = _tgManager.returnToOriginal();
	
	}
	
	/**
	 * Test that the finish time of a sequential graph on multiple processors is the 
	 * actual finish time of the optimal solution.
	 *  @author Holly Hagenson
	 */
	@Test 
	public void testSequentialGraphMulti(){
		_processors = 3;
		
		_testGraph = _tgManager.createSequential(_testGraph);
		
		_aStar = new AStarAlgorithm(_testGraph, _processors); 
		_tasks = _aStar.buildSolution();
		
		// Check finish time of optimal schedule
		assertEquals(8, _validity.getOptimalFinishTime(_tasks));

		// Check that tasks do not overlap
		assertTrue(_validity.noTaskOverlap(_tasks));

		// Check that tasks are ordered correctly
		TreeMap<String,Integer> mappedNodes = _validity.mapTasksWithStartTime(_tasks);
		assertTrue(_validity.isBefore(mappedNodes.get("1"), mappedNodes.get("2")));
		assertTrue(_validity.isBefore(mappedNodes.get("2"), mappedNodes.get("4")));

		_testGraph = _tgManager.returnToOriginal();
		
	}
	
	/**
	 * Test that the output graph has the three labels for each node that we expect:
	 *  - Weight
	 *  - Start
	 *  - Processor
	 *  and that the output graph only has one label for each edge:
	 *  - Weight
	 *  @author Holly Hagenson
	 */
	@Test 
	public void testOutputGraph(){
		_processors = 1;
		
		_aStar = new AStarAlgorithm(_testGraph, _processors);
		_tasks = _aStar.buildSolution();
		Graph outputGraph = _aStar.getGraph(_tasks);

		outputGraph.nodes().forEach((node) -> {
			//Each node should have three attributes
			assertEquals(3, node.getAttributeCount());
			
			//Check that each node has the attributes we expect
			assertTrue(node.getAttribute("Weight")!=null);
			assertTrue(node.getAttribute("Start")!=null);
			assertTrue(node.getAttribute("Processor")!=null);
		});
		
		outputGraph.edges().forEach((edge) -> {
			//Each edge should have one attribute
			assertEquals(1, edge.getAttributeCount());
			
			//Check that each edge has only the Weight attribute
			assertTrue(edge.getAttribute("Weight")!=null);
			assertTrue(edge.getAttribute("Start")==null);
			assertTrue(edge.getAttribute("Processor")==null);
		});		 
	}
	
	/**
	 * Test that the output graph for a task that can be scheduled on multiple processes
	 * has the three labels for each node that we expect:
	 *  - Weight
	 *  - Start
	 *  - Processor
	 *  and that the output graph only has one label for each edge:
	 *  - Weight
	 *  
	 *  @author Holly Hagenson
	 */
	@Test 
	public void testOutputGraphMultipleProcessors(){
		_processors = 3;
		
		_aStar = new AStarAlgorithm(_testGraph, _processors);
		_tasks = _aStar.buildSolution();
		Graph outputGraph = _aStar.getGraph(_tasks);

		//Iterate through the node set of the output graph
		outputGraph.nodes().forEach((node) -> {
			//Each node should have three attributes
			assertEquals(3, node.getAttributeCount());
			
			//Check that each node has the attributes we expect
			assertTrue(node.getAttribute("Weight")!=null);
			assertTrue(node.getAttribute("Start")!=null);
			assertTrue(node.getAttribute("Processor")!=null);
		});
		
		//Iterate through the edge set of the output graph 
		outputGraph.edges().forEach((edge) -> {
			//Each edge should have one attribute
			assertEquals(1, edge.getAttributeCount());
			
			//Check that each edge has only the Weight attribute
			assertTrue(edge.getAttribute("Weight")!=null);
			assertTrue(edge.getAttribute("Start")==null);
			assertTrue(edge.getAttribute("Processor")==null);
		});		 
	}

}
