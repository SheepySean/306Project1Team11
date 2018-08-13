package com.para11el.scheduler.algorithm.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.Test;

import com.para11el.scheduler.algorithm.CostFunctionManager;
import com.para11el.scheduler.algorithm.Task;

/**
 * Junit test to test the correctness of the Cost Function Manager.
 * 
 * @author Jessica Alcantara
 *
 */
public class CostFunctionManagerIT {
	
	private static final int TOTAL_WEIGHTS = 15;

	/**
	 * Unit test for calculating the total idle time in a schedule
	 * @author Jessica Alcantara
	 */
	@Test
	public void testCalculateIdleTime() {
		int processors = 3;
		CostFunctionManager cfm = new CostFunctionManager(TOTAL_WEIGHTS, processors);
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
	
	//TODO:
	/*
	 * * Test cost function calculation
	 * * Test idle time calculation
	 * * Test bottom level calculation
	 * * Test critical path estimate calculation
	 * * Test bounded time estimate
	 */
	
	
}
