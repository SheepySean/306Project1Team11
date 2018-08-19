package com.para11el.scheduler.algorithm.test;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.TreeMap;
import org.graphstream.graph.Graph;
import org.junit.*;

import com.para11el.scheduler.algorithm.DFSInitialiser;
import com.para11el.scheduler.algorithm.OptimalSchedule;
import com.para11el.scheduler.algorithm.Task;

/**
 * JUnit test class to test the behaviour of the DFSAlgorithm.
 * Output is correct if the finish time of the schedule is optimal,
 * tasks do not overlap each other and tasks are correctly ordered
 * according to their respective dependencies.
 * 
 * @author Holly Hagenson, Rebekah Berriman, Jessica Alcantara
 *
 */
public class DFSSequentialIT {
	private DFSInitialiser _dfsSchedule;
	private static SolutionValidity _validity;
	private static TestGraphManager _tgManager;  
	private static Graph _testGraph; 
	private int _processors;
	private int _cores = 1;
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
	 * Test output is correct for graph with multiple entry nodes on a single processor.
	 * @author Rebekah Berriman, Jessica Alcantara, Holly Hagenson
	 */
	@Test
	public void testMultipleEntryNodes(){
		_processors = 1;
		
		_testGraph = _tgManager.createMultiEntry(_testGraph);
		
		_dfsSchedule = new DFSInitialiser(_testGraph, _processors, _cores);
		_tasks = _dfsSchedule.buildSolution();
		
		OptimalSchedule preserveOptimal = OptimalSchedule.getInstance();
		
		// Check finish time of optimal schedule
		assertEquals(11, preserveOptimal.getOptimalTime());
		
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
	 * @author Rebekah Berriman, Holly Hagenson
	 */
	@Test
	public void testMultipleEntryNodesMultiProcessor(){
		_processors = 2;
		
		_testGraph = _tgManager.createMultiEntry(_testGraph);
		
		_dfsSchedule = new DFSInitialiser(_testGraph, _processors, _cores);
		_tasks = _dfsSchedule.buildSolution();
		
		OptimalSchedule preserveOptimal = OptimalSchedule.getInstance();
		
		// Check finish time of optimal schedule
		assertEquals(9, preserveOptimal.getOptimalTime());
		
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
	 * @author Rebekah Berriman, Jessica Alcantara, Holly Hagenson
	 */
	@Test
	public void testMultipleExitNodes(){
		_processors = 1; 
		
		_testGraph = _tgManager.createMultiExit(_testGraph);
		
		_dfsSchedule = new DFSInitialiser(_testGraph, _processors, _cores);
		_tasks = _dfsSchedule.buildSolution();
		
		OptimalSchedule preserveOptimal = OptimalSchedule.getInstance();
		
		// Check finish time of optimal schedule
		assertEquals(13, preserveOptimal.getOptimalTime());
		
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
	 * @author Rebekah Berriman, Holly Hagenson
	 */
	@Test
	public void testMultipleExitNodesMultiProcessor(){
		_processors = 2; 
		
		_testGraph = _tgManager.createMultiExit(_testGraph);
		
		_dfsSchedule = new DFSInitialiser(_testGraph, _processors, _cores);
		_tasks = _dfsSchedule.buildSolution();
		
		OptimalSchedule preserveOptimal = OptimalSchedule.getInstance();
		
		// Check finish time of optimal schedule
		assertEquals(12, preserveOptimal.getOptimalTime());
		
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
	 *  @author Rebekah Berriman, Holly Hagenson
	 */
	@Test 
	public void testSequentialGraphSingle(){
		_processors = 1;
		
		_testGraph = _tgManager.createSequential(_testGraph);
		
		_dfsSchedule = new DFSInitialiser(_testGraph, _processors, _cores);
		_tasks = _dfsSchedule.buildSolution();
		
		OptimalSchedule preserveOptimal = OptimalSchedule.getInstance();
		
		// Check finish time of optimal schedule
		assertEquals(8, preserveOptimal.getOptimalTime());
		
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
	 *  @author Rebekah Berriman, Holly Hagenson
	 */
	@Test 
	public void testSequentialGraphMulti(){
		_processors = 3;
		
		_testGraph = _tgManager.createSequential(_testGraph);
		
		_dfsSchedule = new DFSInitialiser(_testGraph, _processors, _cores);
		_tasks = _dfsSchedule.buildSolution();
		
		OptimalSchedule preserveOptimal = OptimalSchedule.getInstance();
		
		// Check finish time of optimal schedule
		assertEquals(8, preserveOptimal.getOptimalTime());

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
	 *  @author Rebekah Berriman, Holly Hagenson
	 */
	@Test 
	public void testOutputGraph(){
		_processors = 1;
		
		_dfsSchedule = new DFSInitialiser(_testGraph, _processors, _cores);
		_tasks = _dfsSchedule.buildSolution();
		
		Graph outputGraph = _dfsSchedule.getGraph(_tasks);

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
	 *  @author Rebekah Berriman, Holly Hagenson
	 */
	@Test 
	public void testOutputGraphMultipleProcessors(){
		_processors = 3;
		
		_dfsSchedule = new DFSInitialiser(_testGraph, _processors, _cores);
		_tasks = _dfsSchedule.buildSolution();
		
		Graph outputGraph = _dfsSchedule.getGraph(_tasks);

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
