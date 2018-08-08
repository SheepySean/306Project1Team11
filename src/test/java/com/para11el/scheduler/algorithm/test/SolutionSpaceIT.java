package com.para11el.scheduler.algorithm.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.*;

import com.para11el.scheduler.algorithm.SolutionSpaceManager;
import com.para11el.scheduler.algorithm.Task;

/**
 * Junit test to test the behaviour of the SolutionSpaceManager.
 * 
 * @author Holly Hagenson
 *
 */
public class SolutionSpaceIT {
	private SolutionSpaceManager _ssManager; 
	private static Graph _graph1;
	private static Graph _graph2;
	private static Graph _graph3;
	private static Graph _graph4;
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
	 * Test output is correct for multiple processors being utilised.
	 */
	@Test
	public void testMultipleProcessors(){		
		_processors = 2; 
		
		_ssManager = new SolutionSpaceManager(_graph1, _processors);
		_ssManager.initialise();
		
		//_tasks = _ssManager.getOptimal();
		
		Graph output = _ssManager.getGraph();

		for (Task t : _tasks){
			if (t.getNode().getId().equals("1")){
				assertEquals(t.getStartTime(), 0);
				assertEquals(t.getProcessor(), 1);
			}
			if (t.getNode().getId().equals("2")){
				assertEquals(t.getStartTime(), 4);
				assertEquals(t.getProcessor(), 2);
			}
			if (t.getNode().getId().equals("3")){
				assertEquals(t.getStartTime(), 3);
				assertEquals(t.getProcessor(), 1);
			}
			if (t.getNode().getId().equals("4")){
				assertEquals(t.getStartTime(), 8);
				assertEquals(t.getProcessor(), 2);
			}
		}
		
	}
	
	/**
	 * Test output is correct for graph with multiple entry nodes.
	 */
	@Test
	public void testMultipleEntryNodes(){
		_processors = 1;
		
		_ssManager = new SolutionSpaceManager(_graph2, _processors);
		_ssManager.initialise();
		
		//_tasks = _ssManager.getOptimal();
		for (Task t : _tasks){
			if (t.getNode().getId().equals("1")){
				assertEquals(t.getStartTime(), 0);
			}
			if (t.getNode().getId().equals("2")){
				assertEquals(t.getStartTime(), 5);
			}
			if (t.getNode().getId().equals("3")){
				assertEquals(t.getStartTime(), 11);
			}
			if (t.getNode().getId().equals("4")){
				assertEquals(t.getStartTime(), 15);
			}
		}
	}
	
	/**
	 * Test output is correct for graph with multiple exit nodes.
	 */
	@Test
	public void testMultipleExitNodes(){
		
		_processors = 1; 
		
		_ssManager = new SolutionSpaceManager(_graph3, _processors);
		_ssManager.initialise();
		
		_tasks = _ssManager.getOptimal();
		for (Task t : _tasks){
			if (t.getNode().getId().equals("1")){
				assertEquals(t.getStartTime(), 0);
			}
			if (t.getNode().getId().equals("2")){
				assertEquals(t.getStartTime(), 4);
			}
			if (t.getNode().getId().equals("3")){
				assertEquals(t.getStartTime(), 3);
			}
			if (t.getNode().getId().equals("4")){
				assertEquals(t.getStartTime(), 8);
			}
		}
	}
	
	@Test 
	public void testOutputGraph(){
		
		_processors = 1;
		
		_ssManager = new SolutionSpaceManager(_graph4, _processors);
		_ssManager.initialise();
		
		_tasks = _ssManager.getOptimal();
		Graph outputGraph = _ssManager.getGraph();
		
		for(Node n : outputGraph.getNodeSet()){
			assertEquals(n.getAttributeCount(), 1);
		}
		 
	}
	
	
	public static void createGraphs(){
		_graph1 = new SingleGraph("graphWithMultipleProcessors");
		_graph1.addNode("1");
		_graph1.addNode("2");
		_graph1.addNode("3");
		_graph1.addNode("4");
		_graph1.getNode("1").addAttribute("Weight", 3.0);
		_graph1.getNode("2").addAttribute("Weight", 4.0);
		_graph1.getNode("3").addAttribute("Weight", 2.0);
		_graph1.getNode("4").addAttribute("Weight", 1.0);
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
		_graph2.getNode("1").addAttribute("Weight", 5.0);
		_graph2.getNode("2").addAttribute("Weight", 6.0);
		_graph2.getNode("3").addAttribute("Weight", 4.0);
		_graph2.getNode("4").addAttribute("Weight", 3.0);
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
		_graph3.getNode("1").addAttribute("Weight", 4.0);
		_graph3.getNode("2").addAttribute("Weight", 3.0);
		_graph3.getNode("3").addAttribute("Weight", 2.0);
		_graph3.getNode("4").addAttribute("Weight", 5.0);
		_graph3.addEdge("1 -> 2", "1", "2", true);
		_graph3.addEdge("1 -> 3", "1", "3", true);
		_graph3.addEdge("1 -> 4", "1", "4", true);
		_graph3.getEdge("1 -> 2").setAttribute("Weight", 3.0);
		_graph3.getEdge("1 -> 3").setAttribute("Weight", 4.0);
		_graph3.getEdge("1 -> 4").setAttribute("Weight", 2.0);
		
		_graph4 = new SingleGraph("inputGraph");
		_graph4.addNode("1");
		_graph4.addNode("2"); 
		_graph4.getNode("1").addAttribute("Weight", 4.0);
		_graph4.getNode("2").addAttribute("Weight", 3.0);
		_graph4.addEdge("1 -> 2", "1", "2", true);
		_graph4.getEdge("1 -> 2").setAttribute("Weight", 1.0);
	}

}
