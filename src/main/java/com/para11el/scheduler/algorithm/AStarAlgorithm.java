package com.para11el.scheduler.algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Stream;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

/**
 * A* Algorithm to find the optimal solution 
 * 
 * @author Jessica Alcantara, Holly Hagenson
 */
public class AStarAlgorithm extends Algorithm{

	/**
	 * Comparator to sort states according to the results of the cost
	 * function.
	 * 
	 * @author Jessica Alcantara
	 */
	private Comparator<State> _stateComparator = new Comparator<State>() {
		@Override
		public int compare(State o1, State o2) {
			return o1.compareTo(o2);
		}
	};

	private Queue<State> _states = new PriorityQueue<State>(_stateComparator);
	private CostFunctionManager _cfm;

	public AStarAlgorithm() {
		super();
	}

	public AStarAlgorithm(Graph graph, int processor) {
		super(graph, processor);
		_cfm = new CostFunctionManager(calculateTotalWeight(graph.nodes()), processor);
	}

	public AStarAlgorithm(Graph graph, int processor, int cores) {
		super(graph, processor, cores);
		_cfm = new CostFunctionManager(calculateTotalWeight(graph.nodes()), processor);
	}

	/**
	 * Initializes the algorithm and build solution
	 * 
	 * @author Jessica Alcantara
	 */
	public ArrayList<Task> buildSolution() {
		// Initialize priority queue with entry node initial states
		_graph.nodes().forEach((node) -> {
			if (node.getInDegree() == 0) {
				ArrayList<Task> schedule = new ArrayList<Task>();
				schedule.add(new Task(node,0,1));
				_states.add(new State(node, null, schedule,
						_cfm.calculateCostFunction(null, node, schedule)));
			}
		});
		
		while (_states.size() > 0) {
			// Pop the most promising state
			State state = _states.poll();

			// Check if solution is complete
			if (state.isComplete(_graph.nodes())) {
				return state.getSchedule();
			}

			// Expand state into children
			expandState(state);
		}
		return null;
	}

	/**
	 * Finds the earliest start time of a task on a processor with given dependencies
	 * 
	 * @param node to put onto processor
	 * @param schedule to find the earliest start time of
	 * @param processor to find the earliest start time of
	 * @return int of the earliest start time
	 * 
	 * @author Holly Hagenson
	 */
	public int getEarliestStartTime(Node node, ArrayList<Task> schedule, int processor) {
		int startTime = 0, parentLatestFinish = 0, processorFinish; 

		// Get the latest finish time of the parents
		for (Node parent : getParents(node)){
			Task task = findNodeTask(parent, schedule); 
			int nodeWeight = task.getWeight();

			if (task.getProcessor() == processor) {
				parentLatestFinish = task.getStartTime() + nodeWeight; 
			} else{
				int edgeWeight = ((Number)task.getNode().getEdgeToward(node).getAttribute("Weight")).intValue();
				parentLatestFinish = task.getStartTime() + nodeWeight + edgeWeight;  
			}
			if (parentLatestFinish > startTime){
				startTime = parentLatestFinish; 
			}
		}
		
		// Get the latest finish time of the current processor
		processorFinish = getProcessorFinishTime(schedule, processor);
		if (processorFinish > startTime) {
			startTime = processorFinish;
		}

		return startTime;
	}

	/**
	 * Expands the state by finding all possible states from the free nodes.
	 * @param state Parent state
	 * 
	 * @author Jessica Alcantara
	 */
	public void expandState(State state) {
		ArrayList<Node> freeNodes = availableNode(state.getSchedule());
		
		for (Node node : freeNodes) {
			// Create states from each possible allocation of the task
			for (int i=1; i<=_processors; i++) {
				ArrayList<Task> schedule = new ArrayList<Task>(state.getSchedule());
				// Schedule the node
				int startTime = getEarliestStartTime(node,schedule,i);
				schedule.add(new Task(node,startTime,i));

				// Create state from new schedule
				State newState = new State(node,state,schedule,
						_cfm.calculateCostFunction(state,node,schedule));
				if (!checkDuplicates(newState, _states)){
					_states.add(newState);
				}
			}
		}
	}

	/**
	 * Calculates the sum of weights of all nodes in the graph
	 * @param nodes Collection of nodes in the graph
	 * @return int total of node weights
	 * 
	 * @author Jessica Alcantara
	 */
	public int calculateTotalWeight(Stream<Node> nodes) {
		int weightTotal = 0;
		Iterator<Node> i = nodes.iterator();
		while (i.hasNext()){
			weightTotal += ((Number)i.next().getAttribute("Weight")).intValue();
		}
		
		return weightTotal;
	}

	/**
	 * Returns the comparator used to order the states in the priority queue
	 * @return comparator of states
	 * 
	 * @author Jessica Alcantara
	 */
	public Comparator<State> getStateComparator() {
		return _stateComparator;
	}
	
	/**
	 * Check for duplicates in states. 
	 * @param newState to be added to states
	 * @param states already queued
	 * @return Boolean whether there are duplicates or not
	 * 
	 * @author Holly Hagenson
	 */
	public Boolean checkDuplicates(State newState, Queue<State> states){
		// For all queued states, check them against the state to be queued
		for (State state : states){
			Boolean duplicate = compareStates(state, newState);
			Boolean makespan = compareMakespan(state, newState); 
			if (duplicate && makespan){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Compare states to see if all tasks have been put on same processors.
	 * @param newState to be added to states
	 * @param stateInQueue already queued state
	 * @return Boolean whether the states are the same or not
	 * 
	 * @author Holly Hagenson
	 */
	public Boolean compareStates(State newState, State stateInQueue){
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
	 * Compare the makespans of states to see if they are equal.
	 * @param newState to be added to states
	 * @param stateInQueue already queued state
	 * @return Boolean whether the makespans are equal or not
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
}
