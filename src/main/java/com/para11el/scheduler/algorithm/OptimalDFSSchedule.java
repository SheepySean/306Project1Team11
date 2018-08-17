package com.para11el.scheduler.algorithm;

import java.util.ArrayList;

public class OptimalDFSSchedule {
	
	private static OptimalDFSSchedule instance;
	private int scheduleTime;
	private ArrayList<Task> optimalSchedule;
    
    //private constructor to avoid client applications to use constructor
    private OptimalDFSSchedule(){}

    public static synchronized OptimalDFSSchedule getInstance(){
        if (instance ==  null) {
        	instance = new OptimalDFSSchedule();
        } 
        return instance;
    }
    
    public void initialise(int time) {
    	scheduleTime = time;
    }
    
    public synchronized void setOptimal(ArrayList<Task> schedule, int time) {
    	if (scheduleTime >= time) {
    		optimalSchedule = schedule;
    		scheduleTime = time;
    	}
    }
    
    public ArrayList<Task> getOptimalSchedule() {
    	System.out.println("Schedule time: " + scheduleTime);
    	return optimalSchedule;
    }
    
    public int getOptimalTime() {
    	return scheduleTime;	
    }

}
