package com.para11el.scheduler.algorithm.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.Test;

import com.para11el.scheduler.algorithm.CostFunctionManager;
import com.para11el.scheduler.algorithm.State;
import com.para11el.scheduler.algorithm.Task;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

/**
 * JUnit test to test the correctness of the Cost Function Manager.
 * 
 * @author Jessica Alcantara
 *
 */
public class CostFunctionManagerIT {
	private static Graph _graph1;

	/**
	 * Unit test for calculating the total idle time in a schedule
	 * 
	 * @author Jessica Alcantara
	 */
	@Test
	public void testCalculateIdleTime() {
		int processors = 3;
		CostFunctionManager cfm = new CostFunctionManager(11, processors);
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
	 * 
	 * @author Jessica Alcantara
	 */
	@Test
	public void testCalculateBoundTime() {
		int processors = 3;
		CostFunctionManager cfm = new CostFunctionManager(11, processors);
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
	 * Unit test to calculate the critical path estimate of a graph.
	 * 
	 * @author Holly Hagenson
	 */
	@Test
	public void testCalculateCriticalPathEstimate(){
		createGraph();
		
		Node nlast = _graph1.getNode("2");
		nlast.setAttribute("Start", "3");
		Task lastTask = new Task(nlast, 3, 1);
		
		CostFunctionManager cfm = new CostFunctionManager(15, 2);
		
		ArrayList<Task> testSolution = new ArrayList<Task>();
		testSolution.add(new Task(_graph1.getNode("1"),0,1));
		testSolution.add(new Task(_graph1.getNode("2"),3,1));
		
		int criticalPathEstimate = cfm.calculateCriticalPathEstimate(lastTask, testSolution);
		
		assertEquals(criticalPathEstimate, 12);
	}
	
	/**
	 * Unit test to calculate bottom level path of given node.
	 * 
	 * @author Holly Hagenson
	 */
	@Test
	public void testCalculateBottomLevel(){
		createGraph(); 
		int processors = 2; 
		CostFunctionManager cfm = new CostFunctionManager(15, processors);
		
		int bottomLevel = cfm.bottomLevel(_graph1.getNode("2"));
		assertEquals(9, bottomLevel); 
	}
	
	/**
	 * Unit test to calculate cost function of a state.
	 * 
	 * @author Holly Hagenson 
	 */
	/*@Test
	public void testCostFunction(){
		createGraph(); 
		
		State parentState = new State();
		parentState.setCost(12);
		
		ArrayList<Task> testSolution = new ArrayList<Task>();
		testSolution.add(new Task(_graph1.getNode("1"),0,1));
		testSolution.add(new Task(_graph1.getNode("2"),3,1));
		
		CostFunctionManager cfm = new CostFunctionManager(15, 2);
		
		int cost = cfm.calculateCostFunction(parentState, _graph1.getNode("2"), testSolution); 
		
		assertEquals(cost, 12); 
	}*/
	
	/**
	 * Create graphstream graph to use for testing.
	 * 
	 * @author Holly Hagenson
	 */
	public static void createGraph(){
		_graph1 = new SingleGraph("graphWithMultipleProcessors");
		_graph1.addNode("1");
		_graph1.addNode("2");
		_graph1.addNode("3");
		_graph1.addNode("4");
		_graph1.addNode("5");
		_graph1.getNode("1").setAttribute("Weight", 3.0);
		_graph1.getNode("2").setAttribute("Weight", 4.0);
		_graph1.getNode("3").setAttribute("Weight", 2.0);
		_graph1.getNode("4").setAttribute("Weight", 1.0);
		_graph1.getNode("5").setAttribute("Weight", 5.0);
		_graph1.addEdge("1 -> 2", "1", "2", true);
		_graph1.addEdge("1 -> 3", "1", "3", true);
		_graph1.addEdge("2 -> 4", "2", "4", true);
		_graph1.addEdge("2 -> 5", "2", "5", true);
		_graph1.getEdge("1 -> 2").setAttribute("Weight", 1.0);
		_graph1.getEdge("1 -> 3").setAttribute("Weight", 2.0);
		_graph1.getEdge("2 -> 4").setAttribute("Weight", 3.0);
		_graph1.getEdge("2 -> 5").setAttribute("Weight", 5.0);
		
	}	
}
