package com.para11el.scheduler.algorithm.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.stream.Stream;

import org.graphstream.graph.Node;
import org.junit.Test;

import com.para11el.scheduler.algorithm.State;
import com.para11el.scheduler.algorithm.Task;

/**
 * Class to test functionality of the model classes: Task and State.
 * 
 * @author Jessica Alcantara
 *
 */
public class ModelIT {
	
	/**
	 * Unit test to check that tasks with the same node id and start time are 
	 * given the same hashcode for Task
	 * 
	 * @author Jessica Alcantara
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
	 * equal for Task
	 * 
	 * @author Jessica Alcantara
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
	 * are equal for Task
	 * 
	 * @author Jessica Alcantara
	 */
	@Test
	public void testTaskListContainsTask() {
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
	 * Unit test to check task weight calculation on Task
	 * 
	 * @author Jessica Alcantara
	 */
	@Test
	public void testTaskGetWeight() {
		MockNode nodeA = new MockNode(null,"A",5);
		Task taskA = new Task(nodeA,0,1);

		assertEquals(5,taskA.getWeight());
	}
	
	/**
	 * Unit test to check task finish time calculation on Task
	 * 
	 * @author Jessica Alcantara
	 */
	@Test
	public void testTaskGetFinishTime() {
		MockNode nodeA = new MockNode(null,"A",5);
		Task taskA = new Task(nodeA,5,1);

		assertEquals(10,taskA.getFinishTime());
	}
	
	/**
	 * Unit test to check that state is a complete solution for State
	 * 
	 * @author Jessica Alcantara
	 */
	@Test
	public void testStateIsComplete() {
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
		
		State testState = new State(null,null,completeSchedule,0);
		Stream<Node> nodes = Stream.of(nodeA, nodeB, nodeC);

		assertTrue(testState.isComplete(nodes));
	}
	
	/**
	 * Unit test to check that state contains node for State
	 * 
	 * @author Jessica Alcantara
	 */
	@Test
	public void testStateContainsNode() {
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
		State testState = new State(null,null,completeSchedule,0);

		assertTrue(testState.stateContainsNode(nodeA,completeSchedule));
	}

}
