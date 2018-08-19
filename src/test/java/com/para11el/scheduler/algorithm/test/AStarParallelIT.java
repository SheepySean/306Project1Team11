package com.para11el.scheduler.algorithm.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.TreeMap;
import org.graphstream.graph.Graph;
import org.junit.BeforeClass;
import org.junit.Test;
import com.para11el.scheduler.algorithm.AStarAlgorithm;
import com.para11el.scheduler.algorithm.Task;

/**
 * JUnit test class to test the behaviour of the AStarAlgorithm with
 * parallelisation implemented.
 * Output is correct if the finish time of the schedule is optimal,
 * tasks do not overlap each other and tasks are correctly ordered
 * according to their respective dependencies.
 * 
 * @author Holly Hagenson
 *
 */
public class AStarParallelIT {
	private static SolutionValidity _validity;
	private static TestGraphManager _tgManager;  
	private static Graph _testGraph; 
	private int _processors;
	private AStarAlgorithm _aStarSequential;
	private AStarAlgorithm _aStarParallel; 
	private int _sequentialCores = 1;
	private int _parallelCores;
	private ArrayList<Task> _sequentialSchedule = new ArrayList<Task>();
	private ArrayList<Task> _parallelSchedule = new ArrayList<Task>();
	private int _sequentialScheduleTime;
	private int _parallelScheduleTime;
	
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
	 * @author Holly Hagenson
	 */
	@Test
	public void testMultipleEntryNodes(){
		_processors = 1;
		_parallelCores = 2 + (int)(Math.random() * ((16 - 2) + 1));
		
		_testGraph = _tgManager.createMultiEntry(_testGraph);
		
		_aStarSequential = new AStarAlgorithm(_testGraph, _processors, _sequentialCores);
		_sequentialSchedule = _aStarSequential.buildSolution();
		_sequentialScheduleTime = _validity.getOptimalFinishTime(_sequentialSchedule);
		
		_aStarParallel = new AStarAlgorithm(_testGraph, _processors, _parallelCores);
		_parallelSchedule = _aStarParallel.buildSolution();
		_parallelScheduleTime = _validity.getOptimalFinishTime(_parallelSchedule);		
		
		// Check finish time of optimal schedule
		assertEquals(11, _sequentialScheduleTime);
		assertEquals(_parallelScheduleTime, _sequentialScheduleTime);
		
		// Check that tasks do not overlap
		assertTrue(_validity.noTaskOverlap(_parallelSchedule));
		
		// Check that tasks are ordered correctly
		TreeMap<String,Integer> mappedNodes = _validity.mapTasksWithStartTime(_parallelSchedule);
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
		_parallelCores = 2 + (int)(Math.random() * ((16 - 2) + 1));
		
		_testGraph = _tgManager.createMultiEntry(_testGraph);
		
		_aStarSequential = new AStarAlgorithm(_testGraph, _processors, _sequentialCores);
		_sequentialSchedule = _aStarSequential.buildSolution();
		_sequentialScheduleTime = _validity.getOptimalFinishTime(_sequentialSchedule);
		
		_aStarParallel = new AStarAlgorithm(_testGraph, _processors, _parallelCores);
		_parallelSchedule = _aStarParallel.buildSolution();
		_parallelScheduleTime = _validity.getOptimalFinishTime(_parallelSchedule);
		
		// Check finish time of optimal schedule
		assertEquals(9, _sequentialScheduleTime);
		assertEquals(_sequentialScheduleTime, _parallelScheduleTime);
		
		// Check that tasks do not overlap
		ArrayList<Task> proc1Tasks = _validity.tasksOnSameProcessor(_parallelSchedule, 1);
		assertTrue(_validity.noTaskOverlap(proc1Tasks));
		ArrayList<Task> proc2Tasks = _validity.tasksOnSameProcessor(_parallelSchedule, 2);
		assertTrue(_validity.noTaskOverlap(proc2Tasks));
		
		// Check that tasks are ordered correctly
		TreeMap<String,Integer> mappedNodes = _validity.mapTasksWithStartTime(_parallelSchedule);
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
		_parallelCores = 2 + (int)(Math.random() * ((16 - 2) + 1));
		
		_testGraph = _tgManager.createMultiExit(_testGraph);
		
		_aStarSequential = new AStarAlgorithm(_testGraph, _processors, _sequentialCores);
		_sequentialSchedule = _aStarSequential.buildSolution();
		_sequentialScheduleTime = _validity.getOptimalFinishTime(_sequentialSchedule);
		
		_aStarParallel = new AStarAlgorithm(_testGraph, _processors, _parallelCores);
		_parallelSchedule = _aStarParallel.buildSolution();
		_parallelScheduleTime = _validity.getOptimalFinishTime(_parallelSchedule);
		
		// Check finish time of optimal schedule
		assertEquals(13, _sequentialScheduleTime);
		assertEquals(_sequentialScheduleTime, _parallelScheduleTime);
		
		// Check that tasks do not overlap
		assertTrue(_validity.noTaskOverlap(_parallelSchedule));
		
		// Check that tasks are ordered correctly
		TreeMap<String,Integer> mappedNodes = _validity.mapTasksWithStartTime(_parallelSchedule);
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
		_parallelCores = 2 + (int)(Math.random() * ((16 - 2) + 1));
		
		_testGraph = _tgManager.createMultiExit(_testGraph);
		
		_aStarSequential = new AStarAlgorithm(_testGraph, _processors, _sequentialCores);
		_sequentialSchedule = _aStarSequential.buildSolution();
		_sequentialScheduleTime = _validity.getOptimalFinishTime(_sequentialSchedule);
		
		_aStarParallel = new AStarAlgorithm(_testGraph, _processors, _parallelCores);
		_parallelSchedule = _aStarParallel.buildSolution();
		_parallelScheduleTime = _validity.getOptimalFinishTime(_parallelSchedule);
		
		// Check finish time of optimal schedule
		assertEquals(12, _sequentialScheduleTime);
		assertEquals(_sequentialScheduleTime, _parallelScheduleTime);
		
		// Check that tasks do not overlap
		ArrayList<Task> proc1Tasks = _validity.tasksOnSameProcessor(_parallelSchedule, 1);
		assertTrue(_validity.noTaskOverlap(proc1Tasks));
		ArrayList<Task> proc2Tasks = _validity.tasksOnSameProcessor(_parallelSchedule, 2);
		assertTrue(_validity.noTaskOverlap(proc2Tasks));

		// Check that tasks are ordered correctly
		TreeMap<String,Integer> mappedNodes = _validity.mapTasksWithStartTime(_parallelSchedule);
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
		_parallelCores = 2 + (int)(Math.random() * ((16 - 2) + 1));
		
		_testGraph = _tgManager.createSequential(_testGraph);
		
		_aStarSequential = new AStarAlgorithm(_testGraph, _processors, _sequentialCores);
		_sequentialSchedule = _aStarSequential.buildSolution();
		_sequentialScheduleTime = _validity.getOptimalFinishTime(_sequentialSchedule);		
		
		_aStarParallel = new AStarAlgorithm(_testGraph, _processors, _parallelCores);
		_parallelSchedule = _aStarParallel.buildSolution();
		_parallelScheduleTime = _validity.getOptimalFinishTime(_parallelSchedule);
		
		// Check finish time of optimal schedule
		assertEquals(8, _sequentialScheduleTime);
		assertEquals(_sequentialScheduleTime, _parallelScheduleTime);
		
		// Check that tasks do not overlap
		assertTrue(_validity.noTaskOverlap(_parallelSchedule));

		// Check that tasks are ordered correctly
		TreeMap<String,Integer> mappedNodes = _validity.mapTasksWithStartTime(_parallelSchedule);
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
		_parallelCores = 2 + (int)(Math.random() * ((16 - 2) + 1));
		
		_testGraph = _tgManager.createSequential(_testGraph);

		_aStarSequential = new AStarAlgorithm(_testGraph, _processors, _sequentialCores);
		_sequentialSchedule = _aStarSequential.buildSolution();
		_sequentialScheduleTime = _validity.getOptimalFinishTime(_sequentialSchedule);
		
		_aStarParallel = new AStarAlgorithm(_testGraph, _processors, _parallelCores);
		_parallelSchedule = _aStarParallel.buildSolution();
		_parallelScheduleTime = _validity.getOptimalFinishTime(_parallelSchedule);
		
		// Check finish time of optimal schedule
		assertEquals(8, _sequentialScheduleTime);
		assertEquals(_sequentialScheduleTime, _parallelScheduleTime);

		// Check that tasks do not overlap
		assertTrue(_validity.noTaskOverlap(_parallelSchedule));

		// Check that tasks are ordered correctly
		TreeMap<String,Integer> mappedNodes = _validity.mapTasksWithStartTime(_parallelSchedule);
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
		_parallelCores = 2 + (int)(Math.random() * ((16 - 2) + 1));

		_aStarParallel = new AStarAlgorithm(_testGraph, _processors, _parallelCores);
		_parallelSchedule = _aStarParallel.buildSolution();
		
		Graph outputGraph = _aStarParallel.getGraph(_parallelSchedule);

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
	 *  @author Holly Hagenson
	 */
	@Test 
	public void testOutputGraphMultipleProcessors(){
		_processors = 3;
		_parallelCores = 2 + (int)(Math.random() * ((16 - 2) + 1));
		
		_aStarParallel = new AStarAlgorithm(_testGraph, _processors, _parallelCores);
		_parallelSchedule = _aStarParallel.buildSolution();
		
		Graph outputGraph = _aStarParallel.getGraph(_parallelSchedule);

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
