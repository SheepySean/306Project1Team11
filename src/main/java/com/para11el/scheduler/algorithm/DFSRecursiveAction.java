package com.para11el.scheduler.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.logging.Logger;

public class DFSRecursiveAction extends RecursiveAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String workload = "";
    private static final int THRESHOLD = 2;
    private ArrayList<Task> schedule = null;
    private int nodeNumber = 6;
	
	private static Logger logger = Logger.getAnonymousLogger();
		 
	public DFSRecursiveAction(String workload) {
		this.workload = workload;
	}
	
/*	public DFSRecursiveAction(ArrayList<Task> schedule) {
		this.schedule = schedule;
	}*/
	
	protected void compute() {
        if (workload.length() > THRESHOLD) {
            ForkJoinTask.invokeAll(createSubtasks());
        } else {
           processing(workload);
        }
        
        if (schedule.size() < nodeNumber) {
        	ForkJoinTask.invokeAll(createSubtasks());
        } else {
        	processing(workload);
        }
    }
	
/*	private List<DFSRecursiveAction> buildSolution() {
		List<DFSRecursiveAction> tasks = new ArrayList<>();
		
		
		
		
		return tasks;
	}*/
 
    private List<DFSRecursiveAction> createSubtasks() {
        List<DFSRecursiveAction> subtasks = new ArrayList<>();
 
        String partOne = workload.substring(0, workload.length() / 2);
        String partTwo = workload.substring(workload.length() / 2, workload.length());
 
        subtasks.add(new DFSRecursiveAction(partOne));
        subtasks.add(new DFSRecursiveAction(partTwo));
 
        return subtasks;
    }
 
    private void processing(String work) {
        String result = work.toUpperCase();
        logger.info("This result - (" + result + ") - was processed by "
          + Thread.currentThread().getName());
    }

}
