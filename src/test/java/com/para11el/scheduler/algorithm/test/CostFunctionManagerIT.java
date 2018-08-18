package com.para11el.scheduler.algorithm.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import com.para11el.scheduler.algorithm.AStarAlgorithm;
import com.para11el.scheduler.algorithm.CostFunctionManager;
import com.para11el.scheduler.algorithm.NodeManager;
import com.para11el.scheduler.algorithm.Task;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

/**
 * Junit test to test the correctness of the Cost Function Manager.
 * 
 * @author Jessica Alcantara
 *
 */
public class CostFunctionManagerIT {
	private static Graph _testGraph;
	private static CostFunctionManager _cfm; 
	private static TestGraphManager _tgManager; 
	private static NodeManager _nm; 
	private static AStarAlgorithm _aStar; 
	private ArrayList<Task> _tasks;
	
	@BeforeClass
	public static void initialise(){
		_tgManager = new TestGraphManager(); 
		_testGraph = _tgManager.createGraph();
		_nm = new NodeManager(_testGraph);  
	}

	/**
	 * Unit test for calculating the total idle time in a schedule
	 * @author Jessica Alcantara
	 */
	@Test
	public void testCalculateIdleTime() {
		int processors = 3;
		CostFunctionManager cfm = new CostFunctionManager(_nm, 11, processors);
		ArrayList<Task> testSolution = new ArrayList<Task>();
		testSolution.add(new Task(new MockNode(null,"A",2),0,1));
		testSolution.add(new Task(new MockNode(null,"B",2),1,2));
		testSolution.add(new Task(new MockNode(null,"C",3),0,3));
		testSolution.add(new Task(new MockNode(null,"D",1),3,1));
		testSolution.add(new Task(new MockNode(null,"E",1),4,2));
		testSolution.add(new Task(new MockNode(null,"F",2),3,3));
		
		int idleTime = cfm.calculateIdleTime(testSolution);
		assertEquals(3,idleTime);
	}
	
	/**
	 * Unit test for calculating the total bounded time in a schedule
	 * @author Jessica Alcantara
	 */
	@Test
	public void testCalculateBoundTime() {
		int processors = 3;
		CostFunctionManager cfm = new CostFunctionManager(_nm, 11, processors);
		ArrayList<Task> testSolution = new ArrayList<Task>();
		testSolution.add(new Task(new MockNode(null,"A",2),0,1));
		testSolution.add(new Task(new MockNode(null,"B",2),1,2));
		testSolution.add(new Task(new MockNode(null,"C",3),0,3));
		testSolution.add(new Task(new MockNode(null,"D",1),3,1));
		testSolution.add(new Task(new MockNode(null,"E",1),4,2));
		testSolution.add(new Task(new MockNode(null,"F",2),3,3));
		
		int boundedTime = cfm.calculateBoundedTime(testSolution);
		assertEquals(4,boundedTime);
	}
	
	/**
	 * Test that the task of a given node is returned by findNodeTask.
	 * 
	 * @author Holly Hagenson
	 */
	@Test
	public void testFindNodeTask(){
		_aStar = new AStarAlgorithm(_testGraph, 1);
		
		_tasks = _aStar.buildSolution();
		
		_cfm = new CostFunctionManager(_nm, 15, 1); 
		
		assertEquals(_cfm.findNodeTask(_testGraph.getNode("2"), _tasks), _tasks.get(1)); 
	}
	
	/**
	 * Unit test to calculate the critical path estimate of a graph.
	 * 
	 * @author Holly Hagenson
	 */
	@Test
	public void testCalculateCriticalPathEstimate(){
		_aStar = new AStarAlgorithm(_testGraph, 1);
		_tasks = _aStar.buildSolution();
		
		_cfm = new CostFunctionManager(_nm, 15, 2); 
		
		Node nlast = _testGraph.getNode("4"); 

		int criticalPathEstimate = _cfm.criticalPathEstimate(_tasks, nlast);
		
		assertEquals(criticalPathEstimate, 13);
	}
	
	
	/**
	 * Unit test to calculate the overall cost function of a state.
	 * @author Holly Hagenson 
	 */
	@Test
	public void testCostFunction(){
		_aStar = new AStarAlgorithm(_testGraph, 1);
		_tasks = _aStar.buildSolution();
		
		_cfm = new CostFunctionManager(_nm, 15, 2);
		
		int cost = _cfm.calculateCostFunction(null, _testGraph.getNode("4"), _tasks); 
		assertEquals(cost, 13); 
	}
		
}
