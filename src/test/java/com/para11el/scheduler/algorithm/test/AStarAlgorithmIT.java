package com.para11el.scheduler.algorithm.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.PriorityQueue;
import java.util.Queue;

import org.graphstream.graph.Node;
import org.junit.Test;

import com.para11el.scheduler.algorithm.AStarAlgorithm;
import com.para11el.scheduler.algorithm.State;
import com.para11el.scheduler.algorithm.Task;

/**
 * Junit test to test the validity and optimality of the A* Algorithm.
 * 
 * @author Jessica Alcantara
 *
 */
public class AStarAlgorithmIT {
	
	/**
	 * Unit test for the ascending ordering of the state comparator.
	 * @author Jessica Alcantara
	 */
	@Test
	public void testStateComparator() {
		State stateOne = new State();
		stateOne.setCost(4);
		State stateTwo = new State();
		stateTwo.setCost(12);
		State stateThree = new State();
		stateThree.setCost(9);
		
		AStarAlgorithm am = new AStarAlgorithm();
		Queue<State> states = new PriorityQueue<State>(am.getStateComparator());
		states.add(stateOne);
		states.add(stateTwo);
		states.add(stateThree);
		
		assertTrue(states.poll().getCost() == 4);
		assertTrue(states.poll().getCost() == 9);
		assertTrue(states.poll().getCost() == 12);
	}
	
	/**
	 * Unit test for calculating the total weight of nodes in a graph.
	 * @author Holly Hagenson
	 */
	@Test
	public void testCalculateTotalWeight() {
		Collection<Node> nodes = new ArrayList<Node>(); 
		nodes.add(new MockNode(null, "A", 2)); 
		nodes.add(new MockNode(null, "B", 4)); 
		nodes.add(new MockNode(null, "C", 3)); 
		nodes.add(new MockNode(null, "D", 5)); 
		nodes.add(new MockNode(null, "E", 3)); 
		
		AStarAlgorithm am = new AStarAlgorithm(); 
		int totalWeight = am.calculateTotalWeight(nodes); 
		
		assertEquals(totalWeight, 17); 	
	}
	
	@Test
	public void testGetEarliestStartTime() {
		AStarAlgorithm am = new AStarAlgorithm(); 
		
	}

}
