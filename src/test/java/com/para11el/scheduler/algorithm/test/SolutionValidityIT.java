package com.para11el.scheduler.algorithm.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.junit.Test;

import com.para11el.scheduler.algorithm.SolutionSpaceManager;
import com.para11el.scheduler.algorithm.Task;

/**
 * Junit test to test the validity of our Solution.
 * 
 * @author Jessica Alcantara
 *
 */
public class SolutionValidityIT {
	
	private SolutionSpaceManager _ssManager;
	
	/**
	 * Unit test for the correct schedule order of a single entry multiple exit graph
	 * with one processor.
	 * @author Jessica Alcantara
	 */
	@Test
	public void testSingleEntryProcessorOrder() {
		System.out.println("testSingleEntryProcessorOrder()");
		_ssManager = new SolutionSpaceManager(
				new TestGraphManager().createSingleEntryMultipleExit(),1);
		_ssManager.initialise();
		TreeMap<String,Integer> solution = new TreeMap<String,Integer>();
		
		for (Task t : _ssManager.getOptimal()) {
			solution.put(t.getNode().getId(), t.getStartTime());
			System.out.println(t);
		}
		
		assertTrue(isBefore(solution.get("a"), solution.get("b")));
		assertTrue(isBefore(solution.get("a"), solution.get("d")));
		assertTrue(isBefore(solution.get("a"), solution.get("e")));
		assertTrue(isBefore(solution.get("b"), solution.get("c")));
		assertTrue(isBefore(solution.get("c"), solution.get("d")));
	}
	
	/**
	 * Unit test for the optimality of a single entry multiple exit graph
	 * with one processor.
	 * @author Jessica Alcantara
	 */
	@Test
	public void testSingleEntryProcessorOptimality() {
		System.out.println("testSingleEntryProcessorOptimality()");
		_ssManager = new SolutionSpaceManager(
				new TestGraphManager().createSingleEntryMultipleExit(),1);
		_ssManager.initialise();
		ArrayList<Task> schedule = _ssManager.getOptimal();
		int latestFinish = 0;
		
		for (Task task : schedule) {
			System.out.println(task);
			int startTime = task.getStartTime();
			int finishTime = (int) task.getNode().getAttribute("Weight")
					+ startTime;
			if (finishTime > latestFinish) {
				latestFinish = finishTime;
			}
		}
		
		assertEquals(latestFinish,9);
	}
	
	/**
	 * Unit test for no overlap in the schedule of a single exit multiple entry graph
	 * with one processor.
	 * @author Jessica Alcantara
	 */
	@Test
	public void testSingleExitProcessorOverlap() {
		System.out.println("testSingleExitProcessorOverlap()");
		_ssManager = new SolutionSpaceManager(
				new TestGraphManager().createSingleEntryMultipleExit(),1);
		_ssManager.initialise();
		ArrayList<Task> solution = _ssManager.getOptimal();
		
		System.out.println(solution);
		
		assertTrue(noSingleProcessorOverlap(solution));
	}
	
	/**
	 * Unit test for the correct schedule order of a single exit multiple entry graph
	 * with one processor.
	 * @author Jessica Alcantara
	 */
	@Test
	public void testSingleExitProcessorOrder() {
		System.out.println("testSingleExitProcessorOrder()");
		_ssManager = new SolutionSpaceManager(
				new TestGraphManager().createSingleEntryMultipleExit(),1);
		_ssManager.initialise();
		TreeMap<String,Integer> solution = new TreeMap<String,Integer>();
		
		for (Task t : _ssManager.getOptimal()) {
			solution.put(t.getNode().getId(), t.getStartTime());
			System.out.println(t);
		}
		
		assertTrue(isBefore(solution.get("a"), solution.get("c")));
		assertTrue(isBefore(solution.get("a"), solution.get("d")));
		assertTrue(isBefore(solution.get("b"), solution.get("c")));
	}
	
	/**
	 * Unit test for the optimality of a single exit multiple entry graph
	 * with one processor.
	 * @author Jessica Alcantara
	 */
	@Test
	public void testSingleExitProcessorOptimality() {
		System.out.println("testSingleExitProcessorOptimality()");
		_ssManager = new SolutionSpaceManager(
				new TestGraphManager().createSingleEntryMultipleExit(),1);
		_ssManager.initialise();
		ArrayList<Task> solution = _ssManager.getOptimal();
		int latestFinish = 0;
		
		for (Task task : solution) {
			System.out.println(task);
			int startTime = task.getStartTime();
			int finishTime = (int) task.getNode().getAttribute("Weight")
					+ startTime;
			if (finishTime > latestFinish) {
				latestFinish = finishTime;
			}
		}
		assertEquals(latestFinish,9);
	}
	
	/**
	 * Compares order of tasks
	 * @param taskA start time of taskA
	 * @param taskB start time of taskB
	 * @return Boolean true taskA starts before taskB
	 * @author Jessica Alcantara
	 */
	private Boolean isBefore(int taskA, int taskB) {	
		if (taskA < taskB) {
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * Checks if tasks times overlap
	 * @param schedule list representing a schedule
	 * @return Boolean true if no task time overlap
	 * @author Jessica Alcantara
	 */
	private Boolean noSingleProcessorOverlap(ArrayList<Task> schedule) {
		TreeMap<Integer,Integer> taskTimes = new TreeMap<Integer,Integer>();
		
		for (Task task : schedule) {
			int startTime = task.getStartTime();
			int finishTime = (int) task.getNode().getAttribute("Weight")
					+ startTime;
			taskTimes.put(startTime, finishTime);
		}
		
		int start = 0;
		int finish = 0;
		
		for (Entry<Integer, Integer> e : taskTimes.entrySet()) {
			if (e.getKey() < finish && e.getKey() > start) {
				return false;
			}
			start = e.getKey();
			finish = e.getValue();
		}
		
		return true;
	}

}
