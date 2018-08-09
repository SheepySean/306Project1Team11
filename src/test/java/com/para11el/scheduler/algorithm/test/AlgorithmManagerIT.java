package com.para11el.scheduler.algorithm.test;

import static org.junit.Assert.assertTrue;

import java.util.PriorityQueue;
import java.util.Queue;

import org.junit.Test;

import com.para11el.scheduler.algorithm.AlgorithmManager;
import com.para11el.scheduler.algorithm.State;

public class AlgorithmManagerIT {
	
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
		
		AlgorithmManager am = new AlgorithmManager();
		Queue<State> states = new PriorityQueue<State>(am.getStateComparator());
		states.add(stateOne);
		states.add(stateTwo);
		states.add(stateThree);
		
		assertTrue(states.poll().getCost() == 4);
		assertTrue(states.poll().getCost() == 9);
		assertTrue(states.poll().getCost() == 12);
	}

}
