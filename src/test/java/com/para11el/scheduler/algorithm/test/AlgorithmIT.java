package com.para11el.scheduler.algorithm.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.junit.BeforeClass;
import org.junit.Test;

import com.para11el.scheduler.algorithm.AStarAlgorithm;
import com.para11el.scheduler.algorithm.Task;

public class AlgorithmIT {
	
	private static TestGraphManager _tgManager;  
	private static Graph _testGraph; 
	private static AStarAlgorithm _aStar; 
	
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
	 * Unit test for getting the earliest start time of a node. 
	 * @author Holly Hagenson
	 */
	@Test
	public void testGetEarliestStartTime() {		
		// Create test schedule to get earliest start time of
		ArrayList<Task> testSchedule = new ArrayList<Task>(); 
		testSchedule.add(new Task(_testGraph.getNode("1"),0,1));
		testSchedule.add(new Task(_testGraph.getNode("2"),3,1));
		testSchedule.add(new Task(_testGraph.getNode("3"),5,2));
		
		int start = _aStar.getEarliestStartTime(_testGraph.getNode("4"), testSchedule, 1);
		
		assertEquals(start, 7); 
	}
	
	/**
	 * Unit test for getting the finish time of a schedule. 
	 * @author Jessica Alcantara
	 */
	@Test
	public void testGetScheduleFinishTime() {		
		// Create test schedule
		ArrayList<Task> testSchedule = new ArrayList<Task>(); 
		testSchedule.add(new Task(_testGraph.getNode("1"),0,1));
		testSchedule.add(new Task(_testGraph.getNode("2"),3,1));
		testSchedule.add(new Task(_testGraph.getNode("5"),12,2));
		
		int finish = _aStar.getScheduleFinishTime(testSchedule);
		
		assertEquals(finish, 17); 
	}
	
	/**
	 * Unit test for getting the finish time on a given processor. 
	 * @author Jessica Alcantara
	 */
	@Test
	public void testGetProcessorFinishTime() {		
		// Create test schedule
		ArrayList<Task> testSchedule = new ArrayList<Task>(); 
		testSchedule.add(new Task(_testGraph.getNode("1"),0,1));
		testSchedule.add(new Task(_testGraph.getNode("2"),3,1));
		testSchedule.add(new Task(_testGraph.getNode("5"),12,2));
		
		int finish = _aStar.getProcessorFinishTime(testSchedule,1);
		
		assertEquals(finish, 7); 
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
		List<Node> available = aStar.availableNode(testSchedule);
		
		assertEquals(expected, available); 
	}
	
	/**
	 * Unit test for getting the task corresponding to the node. 
	 * @author Jessica Alcantara
	 */
	@Test
	public void testFindNodeTask() {		
		// Create test schedule
		ArrayList<Task> testSchedule = new ArrayList<Task>(); 
		Task expectedTask = new Task(_testGraph.getNode("1"),0,1);
		testSchedule.add(expectedTask);
		testSchedule.add(new Task(_testGraph.getNode("2"),3,1));
		testSchedule.add(new Task(_testGraph.getNode("5"),12,2));
		
		Task foundTask = _aStar.findNodeTask(_testGraph.getNode("1"), testSchedule);
		assertEquals(expectedTask, foundTask); 
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
}
