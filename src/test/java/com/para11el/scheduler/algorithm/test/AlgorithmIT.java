package com.para11el.scheduler.algorithm.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.graphstream.graph.Graph;
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

}
