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
public class DFSParallelIT {
	private static SolutionValidity _validity;
	private static TestGraphManager _tgManager;  
	private static Graph _testGraph; 
	private int _processors;
	
	private DFSInitialiser _dfsSequential;
	private DFSInitialiser _dfsParallel;
	
	private int _sequentialCores = 1;
	private int _parallelCores = 8;
	
	private ArrayList<Task> _sequentialSchedule = new ArrayList<Task>();
	private ArrayList<Task> _parallelSchedule = new ArrayList<Task>();
	
	private long _sequentialTimer;
	private long _parallelTimer;
	
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
	 * @author Rebekah Berriman, Jessica Alcantara, Holly Hagenson
	 */
	@Test
	public void testMultipleEntryNodes(){
		System.out.println("testMultipleEntryNodes");
		_processors = 1;
		
		_testGraph = _tgManager.createMultiEntry(_testGraph);
		
		long start = System.currentTimeMillis();
		_dfsSequential = new DFSInitialiser(_testGraph, _processors, _sequentialCores);
		_sequentialSchedule = _dfsSequential.buildSolution();
		OptimalSchedule sequentialOptimal = OptimalSchedule.getInstance();
		_sequentialScheduleTime = sequentialOptimal.getOptimalTime();
		_sequentialTimer = System.currentTimeMillis() - start;
		System.out.println("Sequential time: " + _sequentialTimer);
		
		start = System.currentTimeMillis();
		_dfsParallel = new DFSInitialiser(_testGraph, _processors, _parallelCores);
		_parallelSchedule = _dfsParallel.buildSolution();
		OptimalSchedule parallelOptimal = OptimalSchedule.getInstance();
		_parallelScheduleTime = parallelOptimal.getOptimalTime();
		_parallelTimer = System.currentTimeMillis() - start;
		System.out.println("Parallel time: " + _parallelTimer);
		
		
		// Check finish time of optimal schedule
		assertEquals(11, _sequentialScheduleTime);
		assertEquals(_parallelScheduleTime, _sequentialScheduleTime);
		
		//Check parallel is faster
		//assertTrue(_parallelTimer > _sequentialTimer);
		
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
	 * @author Rebekah Berriman, Holly Hagenson
	 */
	@Test
	public void testMultipleEntryNodesMultiProcessor(){
		System.out.println("testMultipleEntryNodesMultiProcessor");
		_processors = 2;
		
		_testGraph = _tgManager.createMultiEntry(_testGraph);
		
		long start = System.currentTimeMillis();
		_dfsSequential = new DFSInitialiser(_testGraph, _processors, _sequentialCores);
		_sequentialSchedule = _dfsSequential.buildSolution();
		OptimalSchedule sequentialOptimal = OptimalSchedule.getInstance();
		_sequentialScheduleTime = sequentialOptimal.getOptimalTime();
		_sequentialTimer = System.currentTimeMillis() - start;
		System.out.println("Sequential time: " + _sequentialTimer);
		
		
		start = System.currentTimeMillis();
		_dfsParallel = new DFSInitialiser(_testGraph, _processors, _parallelCores);
		_parallelSchedule = _dfsParallel.buildSolution();
		OptimalSchedule parallelOptimal = OptimalSchedule.getInstance();
		_parallelScheduleTime = parallelOptimal.getOptimalTime();
		_parallelTimer = System.currentTimeMillis() - start;
		System.out.println("Parallel time: " + _parallelTimer);
		
		// Check finish time of optimal schedule
		assertEquals(9, _sequentialScheduleTime);
		assertEquals(_sequentialScheduleTime, _parallelScheduleTime);
		
		//Check parallel is faster
		//assertTrue(_parallelTimer <= _sequentialTimer);
		
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
	 * @author Rebekah Berriman, Jessica Alcantara, Holly Hagenson
	 */
	@Test
	public void testMultipleExitNodes(){
		System.out.println("testMultipleExitNodes");
		_processors = 1; 
		
		_testGraph = _tgManager.createMultiExit(_testGraph);
		
		long start = System.currentTimeMillis();
		_dfsSequential = new DFSInitialiser(_testGraph, _processors, _sequentialCores);
		_sequentialSchedule = _dfsSequential.buildSolution();
		OptimalSchedule sequentialOptimal = OptimalSchedule.getInstance();
		_sequentialScheduleTime = sequentialOptimal.getOptimalTime();
		_sequentialTimer = System.currentTimeMillis() - start;
		System.out.println("Sequential time: " + _sequentialTimer);
		
		
		start = System.currentTimeMillis();
		_dfsParallel = new DFSInitialiser(_testGraph, _processors, _parallelCores);
		_parallelSchedule = _dfsParallel.buildSolution();
		OptimalSchedule parallelOptimal = OptimalSchedule.getInstance();
		_parallelScheduleTime = parallelOptimal.getOptimalTime();
		_parallelTimer = System.currentTimeMillis() - start;
		System.out.println("Parallel time: " + _parallelTimer);
		
		// Check finish time of optimal schedule
		assertEquals(13, _sequentialScheduleTime);
		assertEquals(_sequentialScheduleTime, _parallelScheduleTime);
		
		//assertTrue(_parallelTimer > _sequentialTimer);
		
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
	 * @author Rebekah Berriman, Holly Hagenson
	 */
	@Test
	public void testMultipleExitNodesMultiProcessor(){
		System.out.println("testMultipleExitNodesMultiProcessor");
		_processors = 2; 
		
		_testGraph = _tgManager.createMultiExit(_testGraph);
		
		long start = System.currentTimeMillis();
		_dfsSequential = new DFSInitialiser(_testGraph, _processors, _sequentialCores);
		_sequentialSchedule = _dfsSequential.buildSolution();
		OptimalSchedule sequentialOptimal = OptimalSchedule.getInstance();
		_sequentialScheduleTime = sequentialOptimal.getOptimalTime();
		_sequentialTimer = System.currentTimeMillis() - start;
		System.out.println("Sequential time: " + _sequentialTimer);
		
		
		start = System.currentTimeMillis();
		_dfsParallel = new DFSInitialiser(_testGraph, _processors, _parallelCores);
		_parallelSchedule = _dfsParallel.buildSolution();
		OptimalSchedule parallelOptimal = OptimalSchedule.getInstance();
		_parallelScheduleTime = parallelOptimal.getOptimalTime();
		_parallelTimer = System.currentTimeMillis() - start;
		System.out.println("Parallel time: " + _parallelTimer);
		
		// Check finish time of optimal schedule
		assertEquals(12, _sequentialScheduleTime);
		assertEquals(_sequentialScheduleTime, _parallelScheduleTime);
		
		//Check parallel is faster
		//assertTrue(_parallelTimer < _sequentialTimer);
		
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
	 *  @author Rebekah Berriman, Holly Hagenson
	 */
	@Test 
	public void testSequentialGraphSingle(){
		System.out.println("testSequentialGraphSingle");
		_processors = 1;
		
		_testGraph = _tgManager.createSequential(_testGraph);
		
		long start = System.currentTimeMillis();
		_dfsSequential = new DFSInitialiser(_testGraph, _processors, _sequentialCores);
		_sequentialSchedule = _dfsSequential.buildSolution();
		OptimalSchedule sequentialOptimal = OptimalSchedule.getInstance();
		_sequentialScheduleTime = sequentialOptimal.getOptimalTime();
		_sequentialTimer = System.currentTimeMillis() - start;
		System.out.println("Sequential time: " + _sequentialTimer);
		
		
		start = System.currentTimeMillis();
		_dfsParallel = new DFSInitialiser(_testGraph, _processors, _parallelCores);
		_parallelSchedule = _dfsParallel.buildSolution();
		OptimalSchedule parallelOptimal = OptimalSchedule.getInstance();
		_parallelScheduleTime = parallelOptimal.getOptimalTime();
		_parallelTimer = System.currentTimeMillis() - start;
		System.out.println("Parallel time: " + _parallelTimer);
		
		// Check finish time of optimal schedule
		assertEquals(8, _sequentialScheduleTime);
		assertEquals(_sequentialScheduleTime, _parallelScheduleTime);
		
		//Check parallel is faster
		//assertTrue(_parallelTimer < _sequentialTimer);
		
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
	 *  @author Rebekah Berriman, Holly Hagenson
	 */
	@Test 
	public void testSequentialGraphMulti(){
		System.out.println("testSequentialGraphMulti");
		_processors = 3;
		
		_testGraph = _tgManager.createSequential(_testGraph);
		
		long start = System.currentTimeMillis();
		_dfsSequential = new DFSInitialiser(_testGraph, _processors, _sequentialCores);
		_sequentialSchedule = _dfsSequential.buildSolution();
		OptimalSchedule sequentialOptimal = OptimalSchedule.getInstance();
		_sequentialScheduleTime = sequentialOptimal.getOptimalTime();
		_sequentialTimer = System.currentTimeMillis() - start;
		System.out.println("Sequential time: " + _sequentialTimer);
		
		
		start = System.currentTimeMillis();
		_dfsParallel = new DFSInitialiser(_testGraph, _processors, _parallelCores);
		_parallelSchedule = _dfsParallel.buildSolution();
		OptimalSchedule parallelOptimal = OptimalSchedule.getInstance();
		_parallelScheduleTime = parallelOptimal.getOptimalTime();
		_parallelTimer = System.currentTimeMillis() - start;
		System.out.println("Parallel time: " + _parallelTimer);
		
		// Check finish time of optimal schedule
		assertEquals(8, _sequentialScheduleTime);
		assertEquals(_sequentialScheduleTime, _parallelScheduleTime);

		//Check parallel is faster
		//assertTrue(_parallelTimer > _sequentialTimer);

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
	 *  @author Rebekah Berriman, Holly Hagenson
	 */
	@Test 
	public void testOutputGraph(){
		_processors = 1;

		_dfsParallel = new DFSInitialiser(_testGraph, _processors, _parallelCores);
		_parallelSchedule = _dfsParallel.buildSolution();
		OptimalSchedule parallelOptimal = OptimalSchedule.getInstance();
		_parallelScheduleTime = parallelOptimal.getOptimalTime();
		
		
		Graph outputGraph = _dfsParallel.getGraph(_parallelSchedule);

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
	 *  @author Rebekah Berriman, Holly Hagenson
	 */
	@Test 
	public void testOutputGraphMultipleProcessors(){
		_processors = 3;
		
		_dfsParallel = new DFSInitialiser(_testGraph, _processors, _parallelCores);
		_parallelSchedule = _dfsParallel.buildSolution();
		OptimalSchedule parallelOptimal = OptimalSchedule.getInstance();
		_parallelScheduleTime = parallelOptimal.getOptimalTime();
		
		
		Graph outputGraph = _dfsParallel.getGraph(_parallelSchedule);

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
