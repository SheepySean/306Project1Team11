package com.para11el.scheduler.algorithm.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.graphstream.graph.Graph;
import org.junit.BeforeClass;
import org.junit.Test;

import com.para11el.scheduler.algorithm.DFSAlgorithm;
import com.para11el.scheduler.algorithm.test.TestGraphManager;
import com.para11el.scheduler.algorithm.Task;

/**
 * Helper methods to test the validity of a solution.
 * 
 * @author Jessica Alcantara, Holly Hagenson
 *
 */
public class SolutionValidity {
	
	public SolutionValidity() {} 
	
	/**
	 * Compares order of tasks
	 * @param taskA start time of taskA
	 * @param taskB start time of taskB
	 * @return Boolean true taskA starts before taskB
	 * 
	 * @author Jessica Alcantara
	 */
	public Boolean isBefore(int taskA, int taskB) {	
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
	 * 
	 * @author Jessica Alcantara
	 */
	public Boolean noTaskOverlap(ArrayList<Task> schedule) {
		TreeMap<Integer,Integer> taskTimes = new TreeMap<Integer,Integer>();
		
		for (Task task : schedule) {
			int startTime = task.getStartTime();
			int finishTime = ((Number) task.getNode().getAttribute("Weight")).intValue()
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
	
	/**
	 * Find tasks scheduled on same processor within a schedule
	 * @param schedule of tasks 
	 * @param processor number to find tasks scheduled on
	 * @return ArrayList<Task> tasks on given processor number
	 * 
	 * @author Holly Hagenson
	 */
	public ArrayList<Task> tasksOnSameProcessor(ArrayList<Task> schedule, int processor){
		ArrayList<Task> sameProcessorTasks = new ArrayList<Task>(); 
		
		for (Task t : schedule){
			if (t.getProcessor() == processor){
				sameProcessorTasks.add(t);
			}
		}
		return sameProcessorTasks; 
	}
	
	/**
	 * Map task node id's with their start times
	 * @param schedule of tasks to map
	 * @return TreeMap of mapped tasks
	 * 
	 * @author Jessica Alcantara
	 */
	public TreeMap<String,Integer> mapTasksWithStartTime(ArrayList<Task> schedule){
		TreeMap<String,Integer> mappedNodes = new TreeMap<String,Integer>();
		
		for (Task t : schedule){
			mappedNodes.put(t.getNode().getId(), t.getStartTime());
		}
		
		return mappedNodes; 
	}
	
	/**
	 * Returns the optimal finish time of a solution schedule.
	 * @param solution to find finish time of
	 * @return int optimal finish time
	 * 
	 * @author Holly Hagenson 
	 */
	public int getOptimalFinishTime(ArrayList<Task> solution){
		int maxFinish = 0;
		
		for (Task t : solution){
			int finishTime = t.getFinishTime();
			
			if (finishTime > maxFinish){
				maxFinish = finishTime; 
			}
		}
		return maxFinish; 
	}

}
