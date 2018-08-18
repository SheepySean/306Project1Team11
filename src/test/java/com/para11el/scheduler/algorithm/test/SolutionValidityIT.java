package com.para11el.scheduler.algorithm.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.graphstream.graph.Graph;
import org.junit.BeforeClass;
import org.junit.Test;

import com.para11el.scheduler.algorithm.AStarAlgorithm;
import com.para11el.scheduler.algorithm.Task;

/**
 * JUnit test class to test behaviour of the SolutionValidity class.
 * 
 * @author Holly Hagenson
 *
 */
public class SolutionValidityIT {
	
	private static Graph _testGraph;
	private AStarAlgorithm _aStar;
	private static SolutionValidity _validity;
	private static TestGraphManager _tgManager;
	private int _processors;
	private ArrayList<Task> _tasks = new ArrayList<Task>();
	
	/**
	 * Create graph to test output.
	 */
	@BeforeClass
	public static void initialise(){
		_validity = new SolutionValidity(); 
		_tgManager = new TestGraphManager(); 
		_testGraph = _tgManager.createGraph(); 
	}
	
	/**
	 * Test that isBefore correctly determines validates the order of tasks
	 * 
	 * @author Holly Hagenson
	 */
	@Test
	public void testIsBefore(){
		_processors = 1; 
		
		_aStar = new AStarAlgorithm(_testGraph, _processors);
		_tasks = _aStar.buildSolution();
		
		assertTrue(_validity.isBefore(_tasks.get(0).getStartTime(), _tasks.get(1).getStartTime()));
		assertFalse(_validity.isBefore(_tasks.get(2).getStartTime(), _tasks.get(0).getStartTime()));
	}
	
	/**
	 * Test that noTaskOverlap correctly determines whether there is overlapping 
	 * of tasks in a schedule. 
	 * 
	 * @author Holly Hagenson
	 */
	@Test
	public void testNoTaskOverlap(){
		_processors = 1;
		
		_aStar = new AStarAlgorithm(_testGraph, _processors);
		_tasks = _aStar.buildSolution();
		
		ArrayList<Task> falseSolution = new ArrayList<Task>();
		falseSolution.add(new Task(_testGraph.getNode("1"), 0, 1));
		falseSolution.add(new Task(_testGraph.getNode("2"), 1, 1));
		
		assertTrue(_validity.noTaskOverlap(_tasks));	
		assertFalse(_validity.noTaskOverlap(falseSolution));
	}
	
	/**
	 * Test that tasksOnSameProcessor correctly determines which tasks
	 * have been scheduled on which processor.
	 * 
	 * @author Holly Hagenson
	 */
	@Test
	public void testTasksOnSameProcessor(){
		_processors = 1;
		
		_aStar = new AStarAlgorithm(_testGraph, _processors);
		_tasks = _aStar.buildSolution();
		
		ArrayList<Task> falseSolution = new ArrayList<Task>();
		falseSolution.add(new Task(_testGraph.getNode("1"), 0, 1));
		falseSolution.add(new Task(_testGraph.getNode("2"), 5, 2));
		
		assertEquals(_validity.tasksOnSameProcessor(_tasks, 1), _tasks);
		assertNotEquals(_validity.tasksOnSameProcessor(falseSolution, 1), falseSolution);
	}
	
	/**
	 * Test that optimalFinishTime correctly calculates the optimal makespan of a
	 * given schedule.
	 * 
	 * @author Holly Hagenson
	 */
	@Test
	public void optimalFinishTime(){
		_processors = 1;
		
		_aStar = new AStarAlgorithm(_testGraph, _processors);
		_tasks = _aStar.buildSolution();

		assertEquals(_validity.getOptimalFinishTime(_tasks), 15);

		_processors = 2;

		_aStar = new AStarAlgorithm(_testGraph, _processors);
		_tasks = _aStar.buildSolution();
		
		assertEquals(_validity.getOptimalFinishTime(_tasks), 12);
		
	}

}
