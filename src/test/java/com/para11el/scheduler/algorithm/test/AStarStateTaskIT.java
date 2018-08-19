package com.para11el.scheduler.algorithm.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.graphstream.graph.Graph;
import org.junit.BeforeClass;
import org.junit.Test;

import com.para11el.scheduler.algorithm.AStarStateTask;
import com.para11el.scheduler.algorithm.Task;

/**
 * JUnit test class to test the behaviour of the AStarStateTask class.
 * 
 * @author Holly Hagenson, Jessica Alcantara
 *
 */
public class AStarStateTaskIT {
	
	private static TestGraphManager _tgManager;  
	private static Graph _testGraph; 
	private static AStarStateTask _aStarStateTask; 
	
	/**
	 * Create graph to test output.
	 */
	@BeforeClass
	public static void initialise(){
		_tgManager = new TestGraphManager(); 
		_testGraph = _tgManager.createGraph(); 
		_aStarStateTask = new AStarStateTask(); 
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
		
		Task foundTask = _aStarStateTask.findNodeTask(_testGraph.getNode("1"), testSchedule);
		assertEquals(expectedTask, foundTask); 
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
		
		int start = _aStarStateTask.getEarliestStartTime(_testGraph.getNode("4"), testSchedule, 1);
		
		assertEquals(start, 7); 
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
		
		int finish = _aStarStateTask.getProcessorFinishTime(testSchedule,1);
		
		assertEquals(finish, 7); 
	}

}
