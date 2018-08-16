package com.para11el.scheduler.algorithm.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.*;

import com.para11el.scheduler.algorithm.DFSAlgorithm;
import com.para11el.scheduler.algorithm.Task;

/**
 * JUnit test to test the behaviour of the DFSAlgorithm.
 * 
 * @author Holly Hagenson, Rebekah Berriman
 *
 */
public class DFSAlgorithmIT {
	private DFSAlgorithm _ssManager; 
	private static TestGraphManager _tgManager; 
	private static Graph _testGraph; 
	private int _processors;
	private ArrayList<Task> _tasks = new ArrayList<Task>();
	
	/**
	 * Create multiple graphs to test output.
	 */
	@BeforeClass
	public static void initialise(){
		_tgManager = new TestGraphManager(); 
		_testGraph = _tgManager.createGraph(); 
	}
	
	/**
	 * Test output is correct for multiple processors
	 * @author Rebekah Berriman, Holly Hagenson
	 */
	@Test
	public void testMultipleProcessors(){
		_processors = 2; 
		
		_ssManager = new DFSAlgorithm(_testGraph, _processors);
		_tasks = _ssManager.buildSolution();
		
		//Check finish time of optimal schedule meets the true optimal
		assertEquals(12, _ssManager.getOptimalFinishTime());
		
	}
	
	/**
	 * Test output is correct for graph with multiple entry nodes on a single processor.
	 * @author Rebekah Berriman
	 */
	@Test
	public void testMultipleEntryNodes(){
		_processors = 1;
		
		_tgManager.addNode("6", 3, _testGraph);
		_tgManager.addEdge("6 -> 3", "6", "3", 3, _testGraph);
		
		_ssManager = new DFSAlgorithm(_testGraph, _processors);
		_tasks = _ssManager.buildSolution();
		
		//Check finish time of optimal schedule
		assertEquals(18, _ssManager.getOptimalFinishTime());
		
		//Iterate through the tasks in the schedule
		for (Task task : _tasks){
			//As only a single processor all tasks should be on the first processor
			assertEquals(1, task.getProcessor());
		}
		
		_tgManager.deleteNode("6", _testGraph);
		_tgManager.deleteEdge("6 -> 3", _testGraph); 
	}
	
	/**
	 * Test output is correct for graph with multiple entry nodes on two processors.
	 * @author Rebekah Berriman
	 */
	@Test
	public void testMultipleEntryNodesMultiProcessor(){
		_processors = 2;
		
		_tgManager.addNode("6", 3, _testGraph);
		_tgManager.addEdge("6 -> 3", "6", "3", 3, _testGraph);
		
		_ssManager = new DFSAlgorithm(_testGraph, _processors);
		_tasks = _ssManager.buildSolution();
		
		//Ensure that the optimal finish time is met
		assertEquals(12, _ssManager.getOptimalFinishTime());
		
		_tgManager.deleteNode("6", _testGraph);
		_tgManager.deleteEdge("6 -> 3", _testGraph);
	}
	
	/**
	 * Test output is correct for graph with multiple exit nodes on a single processor
	 * @author Rebekah Berriman
	 */
	@Test
	public void testMultipleExitNodes(){
		_processors = 1; 
		
		_ssManager = new DFSAlgorithm(_testGraph, _processors);
		_tasks = _ssManager.buildSolution();
		
		assertEquals(15, _ssManager.getOptimalFinishTime());
		
	}
	
	/**
	 * Test output is correct for graph with multiple exit nodes on multiple processors
	 * @author Rebekah Berriman
	 */
	@Test
	public void testMultipleExitNodesMultiProcessor(){
		_processors = 2; 
		
		_ssManager = new DFSAlgorithm(_testGraph, _processors);
		_tasks = _ssManager.buildSolution();
		
		//Check finish time of optimal schedule is accurate
		assertEquals(12, _ssManager.getOptimalFinishTime());
		
	}
	
	/**
	 * Test that the finish time of a sequential graph on a single processor is the 
	 * actual finish time of the optimal solution.
	 *  @author Rebekah Berriman
	 */
	@Test 
	public void testSequentialGraphSingle(){
		_processors = 1;
		
		_ssManager = new DFSAlgorithm(_testGraph, _processors);
		_tasks = _ssManager.buildSolution();
		
		//Check finish time is optimal
		assertEquals(15, _ssManager.getOptimalFinishTime());
		
		//Checks all tasks are scheduled on the correct processor
		for (Task task : _tasks) {
			assertTrue(task.getProcessor() == 1);
		}
	
	}
	
	/**
	 * Test that the finish time of a sequential graph on multiple processors is the 
	 * actual finish time of the optimal solution.
	 *  @author Rebekah Berriman
	 */
	@Test 
	public void testSequentialGraphMulti(){
		_processors = 3;
		
		_tgManager.deleteNode("5", _testGraph);
		_tgManager.deleteEdge("2 -> 5", _testGraph);
		_tgManager.deleteNode("3", _testGraph);
		_tgManager.deleteEdge("1 -> 3", _testGraph);
		
		_ssManager = new DFSAlgorithm(_testGraph, _processors);
		_tasks = new ArrayList<Task>(); 
		_tasks = _ssManager.buildSolution();
		
		//Check finish time is optimal
		assertEquals(8, _ssManager.getOptimalFinishTime());
		
		//Check all tasks are scheduled on the first processor
		for (Task task : _tasks) {
			assertTrue(task.getProcessor() == 1);
		}
		
		_tgManager.addNode("5", 5, _testGraph);
		_tgManager.addEdge("2 -> 5", "2", "5", 5, _testGraph);
		_tgManager.addNode("3", 2, _testGraph);
		_tgManager.addEdge("1 -> 3", "1", "3", 2, _testGraph);
	}
	
	/**
	 * Test that the output graph has the three labels for each node that we expect:
	 *  - Weight
	 *  - Start
	 *  - Processor
	 *  and that the output graph only has one label for each edge:
	 *  - Weight
	 *  @author Rebekah Berriman
	 */
	@Test 
	public void testOutputGraph(){
		_processors = 1;
		
		_ssManager = new DFSAlgorithm(_testGraph, _processors);
		_tasks = _ssManager.buildSolution();
		Graph outputGraph = _ssManager.getGraph(_tasks);

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
	 *  @author Rebekah Berriman
	 */
	@Test 
	public void testOutputGraphMultipleProcessors(){
		_processors = 3;
		
		_ssManager = new DFSAlgorithm(_testGraph, _processors);
		_tasks = _ssManager.buildSolution();
		Graph outputGraph = _ssManager.getGraph(_tasks);

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
