package com.para11el.scheduler.algorithm;

import java.util.ArrayList;
import java.util.Queue;

/**
 * Manager class to perform pruning checks.
 * 
 * @author Jessica Alcantara, Holly Hagenson
 *
 */
public class PruningManager {
	
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
		// Check new state against all states in queue
		for (State state : states){
			Boolean duplicate = compareStatesProcessors(state, newState);
			Boolean makespan = compareMakespan(state, newState); 
			if (duplicate && makespan){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Compare states to see if all the same tasks have been put on the same processors in 
	 * the same order regardless of the processor number
	 * @param newState State to be added to list of states
	 * @param stateInQueue State already in queue
	 * @return Boolean duplicate 
	 * 
	 * @author Holly Hagenson
	 */
	public Boolean compareStatesProcessors(State newState, State stateInQueue){
		//TODO: Check order and dont care about processor (Use hash code which 
		// tell if task is the same -> task is the same if same node id and start time)
		// compare hashcodes by task.hashCode() == task.hashCode()
		ArrayList<Task> newSchedule = newState.getSchedule();
		ArrayList<Task> scheduleInQueue = stateInQueue.getSchedule(); 
		
		// For all tasks in both schedule, compare task allocation
		for (Task newTask : newSchedule){
			for (Task queuedTask : scheduleInQueue){
				if (newTask.getNode().getId().equals(queuedTask.getNode().getId())
						&& newTask.getProcessor() != queuedTask.getProcessor()){
					return false; 
				}
			}
		}
		return true; 
	}
	
	/**
	 * Compare the makespans of states to see if they are equal
	 * @param newState State to be added to list of states
	 * @param stateInQueue State already in queue
	 * @return Boolean whether the makespans are equal
	 * 
	 * @author Holly Hagenson
	 */
	public Boolean compareMakespan(State newState, State stateInQueue){
		ArrayList<Task> newSchedule = newState.getSchedule();
		ArrayList<Task> scheduleInQueue = stateInQueue.getSchedule(); 
		
		int newMakespan = getScheduleFinishTime(newSchedule);
		int queuedMakespan = getScheduleFinishTime(scheduleInQueue); 
		
		if (newMakespan == queuedMakespan){
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
}
