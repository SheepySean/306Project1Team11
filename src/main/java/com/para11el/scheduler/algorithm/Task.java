package com.para11el.scheduler.algorithm;

import org.graphstream.graph.Node;

/**
 * Task class to represent an object Task
 *
 * @author Tina Chen, Rebekah Berriman
 */
public class Task {
	
	private Node _node;
	private int _startTime;
	private int _processor;
	
	/**
	 * Task constructor
	 * @param node Node task in input graph
	 * @param startTime Start time of a scheduled task
	 * @param processor Processor a task is scheduled on
	 * 
	 * @author Tina Chen, Rebekah Berriman
	 */
	public Task(Node node, int startTime, int processor) {
		_node = node;
		_startTime = startTime;
		_processor = processor;
	}

	public Node getNode() {
		return _node;
	}

	public void setNode(Node node) {
		this._node = node;
	}

	public int getStartTime() {
		return _startTime;
	}

	public void setStartTime(int startTime) {
		this._startTime = startTime;
	}

	public int getProcessor() {
		return _processor;
	}

	public void setProcessor(int processor) {
		this._processor = processor;
	}
	
	/**
	 * Returns the weight of a task
	 * @return Weight of task
	 * 
	 * @author Jessica Alcantara
	 */
	public int getWeight() {
		int weight = ((Number)_node.getAttribute("Weight")).intValue();
		return weight;
	}
	
	/**
	 * Returns the finish time of a task
	 * @return Finish time of task
	 * 
	 * @author Jessica Alcantara
	 */
	public int getFinishTime() {
		int weight = ((Number)_node.getAttribute("Weight")).intValue();
		int finishTime = weight + _startTime;
		return finishTime;
	}
	
	/**
	 * Checks whether two Tasks are equal. Tasks are equal if the node has
	 * the same ID and if the start time is equal
	 *  @return boolean if two tasks are equal
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @author Jessica Alcantara
	 */
	@Override
	public boolean equals(Object o){
		// Check instance
		if (!(o instanceof Task)) {
			return false;
		}
		// Check properties are equal
		if (_node.getId().equals(((Task)o).getNode().getId())
				&& _startTime == ((Task)o).getStartTime()) {
			return true;
		} else {
			return false;
		}
	}

	 /**
	  * Returns a hashcode value for the Task and computes it by the
	  * start time and node ID
	  * @return int hashcode value
	  * 
	  * @see java.lang.Object#hashCode()
	  * @author Jessica Alcantara
	  */
	@Override
	public int hashCode() {
		int result = 17;
		result = 31*result + _startTime;
		result = 31*result + _node.getId().hashCode();
		return result;
	}
	
	/**
	 * Returns a string representation of a Task object
	 * @return String representing task object
	 * 
	 * @see java.lang.Object#toString()
	 * @author Tina Chen
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Node:  "); 
		buffer.append(_node);
		buffer.append(", Start Time: ");
		buffer.append(_startTime);
		buffer.append(", Processor: ");
		buffer.append(_processor);
		
		return buffer.toString();
	}
}

