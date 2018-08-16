package com.para11el.scheduler.algorithm.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import com.para11el.scheduler.algorithm.PruningManager;
import com.para11el.scheduler.algorithm.Task;

public class PruningManagerTest {
	
	/**
	 * Unit test to check that tasks with the same node id and start time are 
	 * given the same hashcode.
	 */
	@Test
	public void testTaskHashCodeEquality() {
		MockNode node = new MockNode(null,"test",5);
		Task taskOne = new Task(node,5,1);
		Task taskTwo = new Task(node,5,2);
		
		assertEquals(taskOne.hashCode(),taskTwo.hashCode());
	}
	
	/**
	 * Unit test to check that tasks with the same node id and start time are 
	 * equal.
	 */
	@Test
	public void testTaskObjectEquality() {
		MockNode node = new MockNode(null,"test",5);
		Task taskOne = new Task(node,5,1);
		Task taskTwo = new Task(node,5,2);
		
		assertEquals(taskOne,taskTwo);
	}
	
	/**
	 * Unit test to check that the contains method will return true if the tasks
	 * are equal.
	 */
	@Test
	public void testListContainsTask() {
		MockNode nodeOne = new MockNode(null,"testOne",5);
		MockNode nodeEqual = new MockNode(null,"testOne",5);
		MockNode nodeTwo = new MockNode(null,"testTwo",5);
		Task taskOne = new Task(nodeOne,5,1);
		Task taskTwo = new Task(nodeTwo,10,2);
		Task taskEqual = new Task(nodeEqual,5,3);
		Task taskNotContains = new Task(nodeOne,10,3);
		
		ArrayList<Task> tasks = new ArrayList<Task>();
		tasks.add(taskOne);
		tasks.add(taskTwo);
		
		assertTrue(tasks.contains(taskEqual));
		assertFalse(tasks.contains(taskNotContains));
	}
	
	/**
	 * Unit test to check that schedules with tasks allocated to the 
	 * same processor are duplicates.
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
	 * Unit test to check that schedules with tasks allocated to the 
	 * same processor are duplicates.
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
		pm.setMakespan(9);
		assertTrue(pm.compareMakespan(tasksA));
	}

}
