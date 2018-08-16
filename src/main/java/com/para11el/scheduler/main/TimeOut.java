package com.para11el.scheduler.main;

public class TimeOut extends Thread {
	
	private static Long _timeOutMiliseconds;
	
	public TimeOut(int seconds) {
		_timeOutMiliseconds = (long) (seconds*1000);
		
		run();
	}
	
	public void run() {
		try {
			Thread.sleep(_timeOutMiliseconds);
			System.out.println("A timeout has occurred, an optimal schedule was not computed so no output file was generated.");
			System.exit(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			return;
		}
	}
}
