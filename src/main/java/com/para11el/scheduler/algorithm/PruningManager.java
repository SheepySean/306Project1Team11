package com.para11el.scheduler.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

/**
 * Manager class to perform pruning checks.
 * 
 * @author Jessica Alcantara, Holly Hagenson
 *
 */
public class PruningManager {
	
	private int _makespan;
	
	public PruningManager(){}

	/**
	 * Check for duplicates between states
	 * @param newState State to be added to list of states
	 * @param states States already in queue
	 * @return Boolean whether there are duplicates or not
	 * 
	 * @author Holly Hagenson, Jessica Alcantara
	 */
	public Boolean doPrune(State newState, Queue<State> states) {
		ArrayList<Task> newSchedule = newState.getSchedule();
		_makespan = getScheduleFinishTime(newSchedule);
		
		// Check new state against all states in queue
		for (State state : states){
			ArrayList<Task> queuedSchedule = state.getSchedule();
			// Check if same number of tasks
			if (sameNumberOfTasks(newSchedule, queuedSchedule)) {
				// Check if makespans are the same
				if (compareMakespan(queuedSchedule)) {
					// Check if allocation is the same
					if (compareOrder(newSchedule,queuedSchedule)) {
						// Check if order is the same
						if (compareAllocation(newSchedule,queuedSchedule)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Compares the partial schedules if they have the same order of tasks.
	 * @param newSchedule Partial schedule to be added to queue
	 * @param queuedSchedule Partial schedule already in queue
	 * @return Boolean true if same order
	 * 
	 * @author Jessica Alcantara
	 */
	public Boolean compareOrder(ArrayList<Task> newSchedule, ArrayList<Task> queuedSchedule) {
		for (Task task : newSchedule) {
			if (!queuedSchedule.contains(task)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Compares the partial schedules if they have the same number of tasks.
	 * @param newSchedule Partial schedule to be added to queue
	 * @param queuedSchedule Partial schedule already in queue
	 * @return Boolean true if same number
	 * 
	 * @author Jessica Alcantara
	 */
	public Boolean sameNumberOfTasks(ArrayList<Task> newSchedule, ArrayList<Task> queuedSchedule) {
		if (newSchedule.size() == queuedSchedule.size()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Compare schedules to see if all the same tasks have been put on the same processors 
	 * regardless of the processor number
	 * @param newSchedule Partial schedule to be added to list of states
	 * @param queuedSchedule Partial schedule already in queue
	 * @return Boolean true if same allocation
	 * 
	 * @author Jessica Alcantara
	 */
	public Boolean compareAllocation(ArrayList<Task> newSchedule, ArrayList<Task> queuedSchedule){
		HashMap<Integer,ArrayList<Task>> allocations = new HashMap<Integer,ArrayList<Task>>();
		
		// Map each task to a processor in the new schedule
		for (Task task : newSchedule) {
			if (allocations.containsKey(task.getProcessor())) {
				ArrayList<Task> tasks = allocations.get(task.getProcessor());
				tasks.add(task);
				allocations.put(task.getProcessor(), tasks);
			} else {
				ArrayList<Task> tasks = new ArrayList<Task>();
				tasks.add(task);
				allocations.put(task.getProcessor(), tasks);
			}
		}
		
		for (ArrayList<Task> newTasks : allocations.values()) {
			int processor = 0;
			for (Task queuedTask : queuedSchedule) {
				// Check if equal tasks are contained in the same processor
				if (newTasks.contains(queuedTask)) {
					if (queuedTask.getProcessor() != processor && processor != 0) {
						return false;
					}
					processor = queuedTask.getProcessor();
				}
			}
		}
		
		return true; 
	}
	
	/**
	 * Compare the makespans of schedules to see if they are equal
	 * @param queuedSchedule Partial schedule already in queue
	 * @return Boolean true if makespans are equal
	 * 
	 * @author Holly Hagenson
	 */
	public Boolean compareMakespan(ArrayList<Task> queuedSchedule){
		int queuedMakespan = getScheduleFinishTime(queuedSchedule); 
		
		if (_makespan == queuedMakespan){
			return true;
		}
		return false;		
	}
	
	/**
	 * Return the latest finish time of a schedule
	 * @param schedule ArrayList of scheduled tasks
	 * @return int of the latest finish time
	 * 
	 * @author Jessica Alcantara
	 */
	public int getScheduleFinishTime(ArrayList<Task> schedule) {
		int finishTime = 0;
		int taskFinish;
		
		for (Task task : schedule)  {
			taskFinish = task.getFinishTime();
			if (taskFinish > finishTime) {
				finishTime = taskFinish;
			}
		}
		return finishTime;
	}
	
	/**
	 * Sets the makespan
	 * @param makespan Makespan of the current state
	 * 
	 * @author Jessica Alcantara
	 */
	public void setMakespan(int makespan) {
		_makespan = makespan;
	}
}
