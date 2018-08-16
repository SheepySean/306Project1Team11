package com.para11el.scheduler.algorithm.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.stream.Stream;

import org.graphstream.graph.Node;
import org.junit.BeforeClass;
import org.junit.Test;

import com.para11el.scheduler.algorithm.PruningManager;
import com.para11el.scheduler.algorithm.State;
import com.para11el.scheduler.algorithm.Task;

public class PruningManagerIT {
	
	/**
	 * Unit test to check that pruner identifies schedules with
	 * the same allocations regardless of processor number
	 * 
	 * @author Jessica Alcantara
	 */
	@Test
	public void testCompareAllocations() {
		MockNode nodeA = new MockNode(null,"A",5);
		MockNode nodeB = new MockNode(null,"B",5);
		MockNode nodeC = new MockNode(null,"C",5);
		
		MockNode node1 = new MockNode(null,"1",5);
		MockNode node2 = new MockNode(null,"2",5);
		MockNode node3 = new MockNode(null,"3",5);
		
		Task taskA = new Task(nodeA,0,1);
		Task taskB = new Task(nodeB,5,2);
		Task taskC = new Task(nodeC,0,1);
		
		Task task1 = new Task(node1,0,2);
		Task task2 = new Task(node2,5,1);
		Task task3 = new Task(node3,0,2);
		
		ArrayList<Task> tasksA = new ArrayList<Task>();
		tasksA.add(taskA);
		tasksA.add(taskB);
		tasksA.add(taskC);
		
		ArrayList<Task> tasksB = new ArrayList<Task>();
		tasksB.add(task1);
		tasksB.add(task2);
		tasksB.add(task3);
		
		PruningManager pm = new PruningManager();
		assertTrue(pm.compareAllocation(tasksA, tasksB));
	}
	
	/**
	 * Unit test to check pruner identifies schedules with the 
	 * same makespan
	 * 
	 * @author Jessica Alcantara
	 */
	@Test
	public void testCompareMakespan() {	
		MockNode nodeA = new MockNode(null,"A",5);
		MockNode nodeB = new MockNode(null,"B",5);
		MockNode nodeC = new MockNode(null,"C",5);
		
		Task taskA = new Task(nodeA,0,1);
		Task taskB = new Task(nodeB,5,1);
		Task taskC = new Task(nodeC,4,2);

		ArrayList<Task> tasksA = new ArrayList<Task>();
		tasksA.add(taskA);
		tasksA.add(taskB);
		tasksA.add(taskC);
		
		PruningManager pm = new PruningManager();
		pm.setMakespan(10);
		assertTrue(pm.compareMakespan(tasksA));
	}
	
	/**
	 * Unit test to check correctly calculates schedule finish time
	 * 
	 * @author Jessica Alcantara
	 */
	@Test
	public void testGetScheduleFinish() {
		MockNode nodeA = new MockNode(null,"A",5);
		MockNode nodeB = new MockNode(null,"B",5);
		MockNode nodeC = new MockNode(null,"C",5);
		Task taskA = new Task(nodeA,0,1);
		Task taskB = new Task(nodeB,5,2);
		Task taskC = new Task(nodeC,0,1);
	
		ArrayList<Task> completeSchedule = new ArrayList<Task>();
		completeSchedule.add(taskA);
		completeSchedule.add(taskB);
		completeSchedule.add(taskC);
		

		PruningManager pm = new PruningManager();
		assertEquals(10,pm.getScheduleFinishTime(completeSchedule));
	}
	
	/**
	 * Unit test to check that pruner identifies schedules with
	 * the same number of tasks
	 * 
	 * @author Jessica Alcantara
	 */
	@Test
	public void testSameNumberOfTasks() {
		Task taskA = new Task(null,0,1);
		Task taskB = new Task(null,5,2);
		Task taskC = new Task(null,0,1);
		
		Task task1 = new Task(null,0,2);
		Task task2 = new Task(null,5,1);
		Task task3 = new Task(null,0,2);
		
		ArrayList<Task> tasksA = new ArrayList<Task>();
		tasksA.add(taskA);
		tasksA.add(taskB);
		tasksA.add(taskC);
		
		ArrayList<Task> tasksB = new ArrayList<Task>();
		tasksB.add(task1);
		tasksB.add(task2);
		tasksB.add(task3);
		
		PruningManager pm = new PruningManager();
		assertTrue(pm.sameNumberOfTasks(tasksA, tasksB));
	}
	
	/**
	 * Unit test to check that pruner identifies schedules with
	 * the same ordering of tasks
	 * 
	 * @author Jessica Alcantara
	 */
	@Test
	public void testTaskOrder() {
		MockNode nodeA = new MockNode(null,"A",5);
		MockNode nodeB = new MockNode(null,"B",5);
		MockNode nodeC = new MockNode(null,"C",5);
		
		Task taskA = new Task(nodeA,0,1);
		Task taskB = new Task(nodeB,15,2);
		Task taskC = new Task(nodeC,5,1);
		
		Task task1 = new Task(nodeA,0,2);
		Task task2 = new Task(nodeB,15,1);
		Task task3 = new Task(nodeC,5,2);
		
		ArrayList<Task> tasksA = new ArrayList<Task>();
		tasksA.add(taskA);
		tasksA.add(taskB);
		tasksA.add(taskC);
		
		ArrayList<Task> tasksB = new ArrayList<Task>();
		tasksB.add(task1);
		tasksB.add(task2);
		tasksB.add(task3);
		
		PruningManager pm = new PruningManager();
		assertTrue(pm.compareOrder(tasksA, tasksB));
	}

}
