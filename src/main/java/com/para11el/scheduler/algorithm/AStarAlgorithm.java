package com.para11el.scheduler.algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

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
	
	private Task _lastTask; 
	
	public AStarAlgorithm() {
		super();
	}
	
	public AStarAlgorithm(Graph graph, int processor) {
		super(graph, processor);
	}
	
	public AStarAlgorithm(Graph graph, int processor, int cost) {
		super(graph, processor, cost);
	}
	
	/**
	 * Initializes the algorithm and build solution
	 * 
	 * @author Jessica Alcantara, Holly Hagenson
	 */
	public ArrayList<Task> buildSolution() {
		// Initialize priority queue with entry node initial states
		for (Node node : _graph.getNodeSet()) {
			if (node.getInDegree() == 0) {
				_states.add(new State(node, null,
						calculateCostFunction(null, node), 
						((Number) node.getAttribute("Weight")).intValue()));
			}
		}
		
		while (_states.size() > 0) {
			// Add the most promising state to the solution
			State state = getFirstFreeTask();
			_solution.add(scheduleTask(state));
			
			// Check if solution is complete
			if (isCompleteSolution()) {
				return _solution;
			}
			
			// Expand state into children
			pushChildren(state);
		}
		return _solution;
	}
	
	/**
	 * Schedules the task on the processor that gives the optimal solution
	 * @param state
	 * @return
	 */
	public Task scheduleTask(State state) {
		int startTime = 0;
		int processor = 1;
		
		// TODO: schedule task on processor so it is optimal and valid from state
		
		return new Task(state.getNode(), startTime, processor);
	}
	
	/**
	 * Finds the earliest start time of a task on a processor with given dependencies
	 * 
	 * @param processor to find the earliest start time of
	 * @return int of the earliest start time
	 */
	public int getEarliestStartTime(State state, int processor) {
		int startTime = 0, parentLatestFinish = 0, processorFinish = 0; 

		// Get the latest finish time of the parents
		if (getParents(state.getNode()).size() != 0){
			for (Node parent : getParents(state.getNode())){
				Task task = findNode(parent, _solution); 
				int nodeWeight = ((Number)task.getNode().getAttribute("Weight")).intValue();
				
				if (task.getProcessor() == processor) {
					parentLatestFinish = task.getStartTime() + nodeWeight; 
				} else{
					int edgeWeight = ((Number)task.getNode().getEdgeToward(state.getNode()).getAttribute("Weight")).intValue();
					parentLatestFinish = task.getStartTime() + nodeWeight + edgeWeight;  
				}
				if (parentLatestFinish > startTime){
					startTime = parentLatestFinish; 
				}
			}
		}
		
		for (Task task : _solution) {
			if (task.getProcessor() == processor) {
				int nodeWeight = ((Number) task.getNode().getAttribute("Weight")).intValue();
				processorFinish = task.getStartTime() + nodeWeight;
				if (processorFinish > startTime) {
					startTime = processorFinish; 
				}
			}	
		}
		
		return startTime;
	}
	
	/**
	 * Finds the state with the lowest cost whose parent nodes have
	 * already been scheduled.
	 * @return state available state with the lowest cost
	 * 
	 * @author Jessica Alcantara
	 */
	public State getFirstFreeTask() {
		ArrayList<State> notAvailable = new ArrayList<State>();
		Boolean foundFreeTask = false;
		Boolean isFreeTask = true;
		State state = _states.poll();
		
		while (!foundFreeTask) {
			// Check if parent nodes have been scheduled
			for (Edge e : state.getNode().getEachEnteringEdge()) {
				if (!solutionContainsNode(e.getSourceNode())) {
					isFreeTask = false;
					break;
				}
			}
			
			if (isFreeTask) {
				foundFreeTask = true;
			} else {
				notAvailable.add(state);
				state = _states.poll();
			}
		}
		
		// Add the not available tasks back to the priority queue
		for (State s : notAvailable) {
			_states.add(s);
		}
		
		return state;
	}
	
	/**
	 * Checks whether the node has been scheduled in the solution
	 * @param node Node representing a task
	 * @return boolean true if solution contains the node
	 * 
	 * @author Jessica Alcantara
	 */
	public boolean solutionContainsNode(Node node) {
		for (Task task : _solution) {
			if (task.getNode().equals(node)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks that if the partial solution is a complete solution where all input
	 * tasks are scheduled.
	 * @return boolean true if solution is complete
	 * 
	 * @author Jessica Alcantara
	 */
	public Boolean isCompleteSolution() {
		if (_solution.size() == _graph.getNodeCount()) {
			return true;
		} else {
			return false;
		}
	}

	
    /**
     * Retrieves the children of a node and adds the state to the priority queue.
     * @param node Parent node
     * 
     * @author Jessica Alcantara
     */
	public void pushChildren(State state) {
		for (Edge edge : state.getNode().getLeavingEdgeSet()) {
			Node child = edge.getTargetNode();
			int currentScheduleLength = state.getScheduleLength() + 
					((Number) child.getAttribute("Weight")).intValue();
			_states.add(new State(child, state.getNode(),
					calculateCostFunction(state, child),
					currentScheduleLength));
		}
	}
	
    /**
     * Calculates the cost function f(s) by the following equation:
     * 		f(s) = max{f(s-1), Cpe(S), Bt(s)}
     * @param parentState Parent state of the new node
     * @param newNode New node to calculate the cost function of 
     * @return int representing the cost
     * 
     * @author Jessica Alcantara
     */
	public int calculateCostFunction(State parentState, Node newNode) {
		int parentCost;
		int boundedTime = calculateBoundedTime();
		int criticalPathEstimate = calculateCriticalPathEstimate(_lastTask);
		
		// Check if parent node exists
		if (parentState == null) {
			parentCost = 0;
		} else {
			parentCost = parentState.getCost();
		}
		
		int maxCost = Math.max(parentCost, boundedTime);
		maxCost = Math.max(maxCost, criticalPathEstimate);
		return maxCost;
	}
	
	/**
	 * Calculates the bounded time based on:
	 * 		Bt(S) = idle(S) + sum(weights(all nodes))
	 * 
	 * @return int of the bounded time
	 * 
	 * @author Jessica Alcantara
	 */
	public int calculateBoundedTime() {
		int idleTime = calculateIdleTime();
		int weightTotal = 0;
		
		// Calculate the sum of all node weights
		for (Node node : _graph.getNodeSet()) {
			weightTotal += ((Number)node.getAttribute("Weight")).intValue();
		}
		
		int boundedTime = (idleTime + weightTotal)/_processors;
		return boundedTime;
	}
	
	/**
	 * Calculates the sum of all idle times on each processor.
	 * @return int of the idle time
	 * 
	 * @author Jessica Alcantara
	 */
	public int calculateIdleTime() {
		int idleTime = 0;
		int finishTime = 0;
		int time;
		for (int i=1; i<= _processors; i++) {
			for (Task task : _solution) {
				// Sum the idle time on each processor
				if (i == task.getProcessor()) {
					time = task.getStartTime() - finishTime;
					// Check if the processor is idle
					if (time > 0) {
						idleTime += time;
					}
					finishTime = task.getStartTime() + 
							((Number)task.getNode().getAttribute("Weight")).intValue();
				}
			}
		}
		return idleTime;
	}
	
	/**
	 * Calculates the critical path estimate based on:
	 * 		Cpe(S) = startTime(nlast) + bottomLevel(nlast)
	 * 
	 * @param node Nlast, node with latest finish time in partial schedule
	 * @return int of critical path estimate
	 * 
	 * @author Holly Hagenson
	 */
	public int calculateCriticalPathEstimate(Task task) {
		CostFunctionManager cfm = new CostFunctionManager();
		int bottomLevel = cfm.bottomLevel(task.getNode(), _graph);
		int startTime = task.getStartTime(); 
		
		return startTime + bottomLevel;
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
}
