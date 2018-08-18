package com.para11el.scheduler.algorithm;

import java.util.ArrayList;

/**
 * Singleton class that stores the Optimal Schedule and the finish time
 * of that schedule, to ensure that the threads do not compromise the
 * stored optimal schedule.
 * 
 * @author Rebekah Berriman
 *
 */
public class OptimalSchedule {
	
	/**
	 * Private variables to ensure that only one instance of this class is created
	 * and that only the class can set the variables
	 */
	private static OptimalSchedule instance;
	private int scheduleTime;
	private ArrayList<Task> optimalSchedule;
    
    /**
     * Private constructor to avoid client applications to use constructor - to ensure singleton
     */
    private OptimalSchedule(){}

    /**
     * Ensures that only one instance of this class is created,
     * to ensure that the optimal schedule stored is the correct one.
     * @return instance of the singleton 
     * 
     * @author Rebekah Berriman
     */
    public static synchronized OptimalSchedule getInstance(){
        if (instance ==  null) {
        	instance = new OptimalSchedule();
        } 
        return instance;
    }
    
    /**
     * Set the initial minimum time as the time of all nodes on one processor
     * @param time of a dependency preserved schedule
     * 
     * @author Rebekah Berriman
     */
    public void initialise(int time) {
    	scheduleTime = time;
    }
    
    /**
     * Synchronised to ensure thread safety, if the finish time of this schedule
     * is less than or equal to the stored schedule time, set the current schedule
     * to the input schedule
     * @param schedule that is valid
     * @param finishTime of the passed in schedule 
     * 
     * @author Rebekah Berriman
     */
    public synchronized void setOptimal(ArrayList<Task> schedule, int finishTime) {
    	if (scheduleTime >= finishTime) {
    		optimalSchedule = schedule;
    		scheduleTime = finishTime;
    	}
    }
    
    /**
     * Allow the optimal schedule to be returned.
     * @return the optimal schedule 
     * 
     * @author Rebekah Berriman
     */
    public ArrayList<Task> getOptimalSchedule() {
    	return optimalSchedule;
    }
    
    /**
     * Allow the optimal schedules finish time to be returned
     * @return the finish time of the optimal schedule
     * 
     * @author Rebekah Berriman
     */
    public int getOptimalTime() {
    	return scheduleTime;	
    }

}
