package com.para11el.scheduler.algorithm.test;

import static org.junit.Assert.assertTrue;

import java.util.PriorityQueue;
import java.util.Queue;

import org.junit.Test;

import com.para11el.scheduler.algorithm.AStarAlgorithm;
import com.para11el.scheduler.algorithm.State;

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
	 * Unit test for calculating the total weights of nodes in a graph.
	 * @author 
	 */
	@Test
	public void testCalculateTotalWeight() {
		
	}

}
