package com.para11el.scheduler.algorithm;

import java.util.ArrayList;

public class DFSSchedule {
	
	private int _minimumTime;
	private ArrayList<Task> _solutionList;
	
	public DFSSchedule() {
		
		if (_solutionList == null) {
			_solutionList = new ArrayList<Task>();
		}
	}
	
	public void setSolution(ArrayList<Task> task) {
		_solutionList.clear();
		_solutionList = task;
	}
	
	public ArrayList<Task> getSolution() {
		return _solutionList;
	}
	
	public void setMinimumTime(int time) {
		_minimumTime = time;
	}

}
