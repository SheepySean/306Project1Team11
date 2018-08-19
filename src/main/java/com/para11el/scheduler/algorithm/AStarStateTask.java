package com.para11el.scheduler.algorithm;

import java.util.ArrayList;
import java.util.concurrent.RecursiveTask;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

/**
 * Class representing the a task to be computed in parallel. These are
 * states built during the expansion phase of the A* algorithm.
 * 
 * @author Jessica Alcantara, Holly Hagenson
 *
 */
public class AStarStateTask extends RecursiveTask<State> implements Parallelised{
	
	private static final long serialVersionUID = 1L;
	private State _state;
	private Node _node;
	private int _processor;
	private CostFunctionManager _cfm;
	
	public AStarStateTask() {}
	
	/**
	 * Constructor for the A* State Task
	 * @param state Parent state
	 * @param node Node to be added to the new state
	 * @param processor Number of processors to schedule on
	 * @param cfm Cost Function Manager to handle cost function calculations
	 * 
	 * @author Jessica Alcantara, Holly Hagenson
	 */
	public AStarStateTask(State state, Node node, int processor, CostFunctionManager cfm) {
		_state = state;
		_node = node;
		_processor = processor;
		_cfm = cfm;
	}

	/**
	 * Creates a new state from the parent state schedule
	 * 
	 * @author Jessica Alcantara
	 * @see java.util.concurrent.RecursiveTask#compute()
	 */
	@Override
	protected State compute() {
		ArrayList<Task> schedule = new ArrayList<Task>(_state.getSchedule());

		// Schedule the node
		int startTime = getEarliestStartTime(_node,schedule,_processor);
		schedule.add(new Task(_node,startTime,_processor));

		// Create state from new schedule
		State newState = new State(_node,_state,schedule,
				_cfm.calculateCostFunction(_state,_node,schedule));
		
		return newState;
	}


	@Override
	public Graph getGSGraph() {
		return null;
	}
}
