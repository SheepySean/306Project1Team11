package com.para11el.scheduler.main;

import com.para11el.scheduler.ui.ViewerPaneController;

/**
 * TimeOut extends the thread class and stops the program
 * if the timeout occurs before an optimal schedule has been found.
 * 
 * @author Rebekah Berriman
 *
 */
public class TimeOut extends Thread {
	
	private static Long _timeOutMiliseconds;
	
	/**
	 * Takes the timeout period specified by the user 
	 * @param seconds after which the program exits
	 * 
	 * @author Rebekah Berriman
	 */
	public TimeOut(int seconds) {
		_timeOutMiliseconds = (long) (seconds*1000);
		
		run();
	}
	
	/**
	 * Takes the timeout period specified by the user.
	 * If a timeout occurs, stop the program from running, otherwise
	 * the thread is interrupted when the optimal schedule is found and the
	 * program terminates.
	 * 
	 * @author Rebekah Berriman
	 */
	public void run() {
		try {
			Thread.sleep(_timeOutMiliseconds);
			System.out.println("A timeout has occurred, an optimal schedule was not computed so no output file was generated.");
			
			ViewerPaneController.getInstance();
			ViewerPaneController.toggleTimer(false);
			
			ViewerPaneController.getInstance();
			ViewerPaneController.setTimeout(true);;
			
			
			
			
			
			//System.exit(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			return;
		}
	}
}
