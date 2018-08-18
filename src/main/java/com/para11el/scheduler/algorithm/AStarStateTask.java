package com.para11el.scheduler.algorithm;

import java.util.ArrayList;
import java.util.concurrent.RecursiveTask;

import org.graphstream.graph.Node;

public class AStarStateTask extends RecursiveTask<State>{
	
	private static final long serialVersionUID = 1L;
	private State _state;
	private Node _node;
	private int _processor;
	private CostFunctionManager _cfm;
	
	public AStarStateTask(State state, Node node, int processor
			, CostFunctionManager cfm) {
		_state = state;
		_node = node;
		_processor = processor;
		_cfm = cfm;
	}

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
	
	public int getEarliestStartTime(Node node, ArrayList<Task> schedule, int processor) {
		int processorFinishTime; 
		int nodeStartTime = 0;
		int parentLatestFinish = 0;

		// Get the latest finish time of the parents
		for (Node parent : getParents(node)){
			Task task = findNodeTask(parent, schedule); 
			int nodeWeight = task.getWeight();

			if (task.getProcessor() == processor) {
				parentLatestFinish = task.getStartTime() + nodeWeight; 
			} else{
				int edgeWeight = ((Number)task.getNode().getEdgeToward(node)
						.getAttribute("Weight")).intValue();
				parentLatestFinish = task.getStartTime() + nodeWeight + edgeWeight;  
			}
			if (parentLatestFinish > nodeStartTime){
				nodeStartTime = parentLatestFinish; 
			}
		}
		
		// Get the latest finish time of the current processor
		processorFinishTime = getProcessorFinishTime(schedule, processor);
		if (processorFinishTime > nodeStartTime) {
			nodeStartTime = processorFinishTime;
		}

		return nodeStartTime;
	}
	
	/**
	 * Returns a list of parent nodes of a node
	 * @param node Node to find parents of
	 * 
	 * @author Tina Chen 
	 */
	public ArrayList<Node> getParents(Node node) {
		ArrayList<Node> parents = new ArrayList<Node>();
		node.enteringEdges().forEach((edge) -> {
			parents.add(edge.getSourceNode());
		});
		
		return parents;
	}
	
	/**
	 * Returns the task corresponding to a node
	 * @param node From input graph
	 * @param schedule ArrayList of scheduled tasks
	 * @return Task object node
	 * 
	 * @author Sean Oldfield
	 */
	public Task findNodeTask(Node node, ArrayList<Task> schedule) {
		for (Task task : schedule) {
			if (task.getNode().equals(node)) {
				return task;
			}
		}
		return null;
	}
	
	public int getProcessorFinishTime(ArrayList<Task> schedule, int processor) {
		int processorFinishTime = 0;
		int taskFinishTime;

		for (Task task : schedule) {
			if (task.getProcessor() == processor) {
				taskFinishTime = task.getFinishTime();
				if (processorFinishTime < taskFinishTime) {
					processorFinishTime = taskFinishTime;
				}
			}	
		}
		return processorFinishTime;
	}

}
