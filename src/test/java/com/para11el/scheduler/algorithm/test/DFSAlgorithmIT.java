package com.para11el.scheduler.algorithm.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.*;

import com.para11el.scheduler.algorithm.SolutionSpaceManager;
import com.para11el.scheduler.algorithm.Task;

/**
 * JUnit test to test the behaviour of the SolutionSpaceManager.
 * 
 * @author Holly Hagenson, Rebekah Berriman
 *
 */
public class DFSAlgorithmIT {
	private SolutionSpaceManager _ssManager; 
	private static Graph _graph1;
	private static Graph _graph2;
	private static Graph _graph3;
	private static Graph _graph4;
	private static Graph _graph5;
	private int _processors;
	private ArrayList<Task> _tasks = new ArrayList<Task>();
	
	/**
	 * Create multiple graphs to test output.
	 */
	@BeforeClass
	public static void initialise(){
		createGraphs();
	}
	
	/**
	 * Test output is correct for multiple processors
	 * @author Rebekah Berriman, Holly Hagenson
	 */
	@Test
	public void testMultipleProcessors(){
		_processors = 2; 
		
		_ssManager = new SolutionSpaceManager(_graph1, _processors);
		_ssManager.initialise();
		
		_tasks = _ssManager.getOptimal();
		
		//Check finish time of optimal schedule meets the true optimal
		assertEquals(10, _ssManager.getOptimalFinishTime());

		//Iterate through the tasks in the schedule
		for (Task task : _tasks){
			if (task.getNode().getId().equals("1")){
				assertEquals(0, task.getStartTime());
				assertEquals(1, task.getProcessor());
			}
			if (task.getNode().getId().equals("2")){
				assertEquals(3, task.getStartTime());
				assertEquals(1, task.getProcessor());
			}
			if (task.getNode().getId().equals("3")){
				assertEquals(5, task.getStartTime());
				assertEquals(2, task.getProcessor());
			}
			if (task.getNode().getId().equals("4")){
				assertEquals(9, task.getStartTime());
				assertEquals(1, task.getProcessor());
			}
		}
		
	}
	
	/**
	 * Test output is correct for graph with multiple entry nodes on a single processor.
	 * @author Rebekah Berriman
	 */
	@Test
	public void testMultipleEntryNodes(){
		_processors = 1;
		
		_ssManager = new SolutionSpaceManager(_graph2, _processors);
		_ssManager.initialise();
		
		_tasks = _ssManager.getOptimal();
		
		//Check finish time of optimal schedule
		assertEquals(18, _ssManager.getOptimalFinishTime());
		
		//Iterate through the tasks in the schedule
		for (Task task : _tasks){
			//As only a single processor all tasks should be on the first processor
			assertEquals(1, task.getProcessor());
			if (task.getNode().getId().equals("1")){
				assertEquals(10, task.getStartTime());
			}
			if (task.getNode().getId().equals("2")){
				assertEquals(4, task.getStartTime());
			}
			if (task.getNode().getId().equals("3")){
				assertEquals(0, task.getStartTime());
			}
			if (task.getNode().getId().equals("4")){
				assertEquals(15, task.getStartTime());
			}
		}
	}
	
	/**
	 * Test output is correct for graph with multiple entry nodes on two processors.
	 * @author Rebekah Berriman
	 */
	@Test
	public void testMultipleEntryNodesMultiProcessor(){
		_processors = 2;
		
		_ssManager = new SolutionSpaceManager(_graph2, _processors);
		_ssManager.initialise();
		
		_tasks = _ssManager.getOptimal();
		
		//Ensure that the optimal finish time is met
		assertEquals(12, _ssManager.getOptimalFinishTime());
		
		//Iterate through the tasks of the optimal solution
		for (Task task : _tasks){
			if (task.getNode().getId().equals("1")){
				assertEquals(4, task.getStartTime());
				assertEquals(1, task.getProcessor());
			}
			if (task.getNode().getId().equals("2")){
				assertEquals(0, task.getStartTime());
				assertEquals(2, task.getProcessor());
			}
			if (task.getNode().getId().equals("3")){
				assertEquals(0, task.getStartTime());
				assertEquals(1, task.getProcessor());
			}
			if (task.getNode().getId().equals("4")){
				assertEquals(9, task.getStartTime());
				assertEquals(1, task.getProcessor());
			}
		}
	}
	
	/**
	 * Test output is correct for graph with multiple exit nodes on a single processor
	 * @author Rebekah Berriman
	 */
	@Test
	public void testMultipleExitNodes(){
		_processors = 1; 
		
		_ssManager = new SolutionSpaceManager(_graph3, _processors);
		_ssManager.initialise();
		
		_tasks = _ssManager.getOptimal();
		
		assertEquals(14, _ssManager.getOptimalFinishTime());
		
		//Iterate through the scheduled tasks
		for (Task t : _tasks){
			//Only one processor so each task should be on processor 1
			assertEquals(1, t.getProcessor());
			if (t.getNode().getId().equals("1")){
				assertEquals(0, t.getStartTime());
			}
			if (t.getNode().getId().equals("2")){
				assertEquals(11, t.getStartTime());
			}
			if (t.getNode().getId().equals("3")){
				assertEquals(9, t.getStartTime());
			}
			if (t.getNode().getId().equals("4")){
				assertEquals(4, t.getStartTime());
			}
		}
	}
	
	/**
	 * Test output is correct for graph with multiple exit nodes on multiple processors
	 * @author Rebekah Berriman
	 */
	@Test
	public void testMultipleExitNodesMultiProcessor(){
		_processors = 2; 
		
		_ssManager = new SolutionSpaceManager(_graph3, _processors);
		_ssManager.initialise();
		
		_tasks = _ssManager.getOptimal();
		
		//Check finish time of optimal schedule is accurate
		assertEquals(11, _ssManager.getOptimalFinishTime());
		
		//Iterate through the scheduled tasks
		for (Task task : _tasks){
			if (task.getNode().getId().equals("1")){
				assertEquals(0, task.getStartTime());
				assertEquals(1, task.getProcessor());
			}
			if (task.getNode().getId().equals("2")){
				assertEquals(6, task.getStartTime());
				assertEquals(1, task.getProcessor());
			}
			if (task.getNode().getId().equals("3")){
				assertEquals(4, task.getStartTime());
				assertEquals(1, task.getProcessor());
			}
			if (task.getNode().getId().equals("4")){
				assertEquals(6, task.getStartTime());
				assertEquals(2, task.getProcessor());
			}
		}
	}
	
	/**
	 * Test that the finish time of a sequential graph on a single processor is the actual finish time of the optimal solution
	 *  @author Rebekah Berriman
	 */
	@Test 
	public void testSequentialGraphSingle(){
		_processors = 1;
		
		_ssManager = new SolutionSpaceManager(_graph5, _processors);
		_ssManager.initialise();
		
		_tasks = _ssManager.getOptimal();
		
		//Check finish time is optimal
		assertEquals(14, _ssManager.getOptimalFinishTime());
		
		//Checks all tasks are scheduled on the correct processor
		for (Task task : _tasks) {
			assertTrue(task.getProcessor() == 1);
		}
	
	}
	
	/**
	 * Test that the finish time of a sequential graph on multiple processors is the actual finish time of the optimal solution
	 *  @author Rebekah Berriman
	 */
	@Test 
	public void testSequentialGraphMulti(){
		_processors = 3;
		
		_ssManager = new SolutionSpaceManager(_graph5, _processors);
		_ssManager.initialise();
		
		_tasks = _ssManager.getOptimal();
		
		//Check finish time is optimal
		assertEquals(14, _ssManager.getOptimalFinishTime());
		
		//Check all tasks are scheduled on the first processor
		for (Task task : _tasks) {
			assertTrue(task.getProcessor() == 1);
		}
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
		
		_ssManager = new SolutionSpaceManager(_graph4, _processors);
		_ssManager.initialise();
		
		_tasks = _ssManager.getOptimal();
		Graph outputGraph = _ssManager.getGraph();

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
		
		_ssManager = new SolutionSpaceManager(_graph4, _processors);
		_ssManager.initialise();
		
		_tasks = _ssManager.getOptimal();
		Graph outputGraph = _ssManager.getGraph();

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
	
	/**
	 * Graphs to use for testing
	 * @author Holly Hagenson
	 */
	public static void createGraphs(){
		_graph1 = new SingleGraph("graphWithMultipleProcessors");
		_graph1.addNode("1");
		_graph1.addNode("2");
		_graph1.addNode("3");
		_graph1.addNode("4");
		_graph1.getNode("1").setAttribute("Weight", 3.0);
		_graph1.getNode("2").setAttribute("Weight", 4.0);
		_graph1.getNode("3").setAttribute("Weight", 2.0);
		_graph1.getNode("4").setAttribute("Weight", 1.0);
		_graph1.addEdge("1 -> 2", "1", "2", true);
		_graph1.addEdge("1 -> 3", "1", "3", true);
		_graph1.addEdge("2 -> 4", "2", "4", true);
		_graph1.addEdge("3 -> 4", "3", "4", true);
		_graph1.getEdge("1 -> 2").setAttribute("Weight", 1.0);
		_graph1.getEdge("1 -> 3").setAttribute("Weight", 2.0);
		_graph1.getEdge("2 -> 4").setAttribute("Weight", 3.0);
		_graph1.getEdge("3 -> 4").setAttribute("Weight", 2.0);
		
		_graph2 = new SingleGraph("graphWithMultipleEntries");
		_graph2.addNode("1");
		_graph2.addNode("2");
		_graph2.addNode("3");
		_graph2.addNode("4");
		_graph2.getNode("1").setAttribute("Weight", 5.0);
		_graph2.getNode("2").setAttribute("Weight", 6.0);
		_graph2.getNode("3").setAttribute("Weight", 4.0);
		_graph2.getNode("4").setAttribute("Weight", 3.0);
		_graph2.addEdge("1 -> 4", "1", "4", true);
		_graph2.addEdge("2 -> 4", "2", "4", true);
		_graph2.addEdge("3 -> 4", "3", "4", true);
		_graph2.getEdge("1 -> 4").setAttribute("Weight", 1.0);
		_graph2.getEdge("2 -> 4").setAttribute("Weight", 2.0);
		_graph2.getEdge("3 -> 4").setAttribute("Weight", 2.0);
		
		_graph3 = new SingleGraph("graphWithMultipleExits");
		_graph3.addNode("1");
		_graph3.addNode("2");
		_graph3.addNode("3");
		_graph3.addNode("4");
		_graph3.getNode("1").setAttribute("Weight", 4.0);
		_graph3.getNode("2").setAttribute("Weight", 3.0);
		_graph3.getNode("3").setAttribute("Weight", 2.0);
		_graph3.getNode("4").setAttribute("Weight", 5.0);
		_graph3.addEdge("1 -> 2", "1", "2", true);
		_graph3.addEdge("1 -> 3", "1", "3", true);
		_graph3.addEdge("1 -> 4", "1", "4", true);
		_graph3.getEdge("1 -> 2").setAttribute("Weight", 3.0);
		_graph3.getEdge("1 -> 3").setAttribute("Weight", 4.0);
		_graph3.getEdge("1 -> 4").setAttribute("Weight", 2.0);
		
		_graph4 = new SingleGraph("inputGraph");
		_graph4.addNode("1");
		_graph4.addNode("2"); 
		_graph4.getNode("1").setAttribute("Weight", 4.0);
		_graph4.getNode("2").setAttribute("Weight", 3.0);
		_graph4.addEdge("1 -> 2", "1", "2", true);
		_graph4.getEdge("1 -> 2").setAttribute("Weight", 1.0);
		
		_graph5 = new SingleGraph("sequentialGraph");
		_graph5.addNode("1");
		_graph5.addNode("2");
		_graph5.addNode("3");
		_graph5.addNode("4");
		_graph5.getNode("1").setAttribute("Weight", 4.0);
		_graph5.getNode("2").setAttribute("Weight", 3.0);
		_graph5.getNode("3").setAttribute("Weight", 4.0);
		_graph5.getNode("4").setAttribute("Weight", 3.0);
		_graph5.addEdge("1 -> 2", "1", "2", true);
		_graph5.getEdge("1 -> 2").setAttribute("Weight", 1.0);
		_graph5.addEdge("2 -> 3", "2", "3", true);
		_graph5.getEdge("2 -> 3").setAttribute("Weight", 1.0);
		_graph5.addEdge("3 -> 4", "3", "4", true);
		_graph5.getEdge("3 -> 4").setAttribute("Weight", 1.0);
	}

}
