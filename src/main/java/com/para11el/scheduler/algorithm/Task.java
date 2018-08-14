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
	 * 
	 * @param node a task in input graph
	 * @param startTime the start time of a scheduled task
	 * @param processor the processor a task is scheduled on
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
	 * Returns the weight of a task.
	 * @return weight of task
	 * 
	 * @author Jessica Alcantara
	 */
	public int getWeight() {
		int weight = ((Number)_node.getAttribute("Weight")).intValue();
		return weight;
	}
	
	/**
	 * Returns the finish time of a task.
	 * @return finishTime of task
	 * 
	 * @author Jessica Alcantara
	 */
	public int getFinishTime() {
		int weight = ((Number)_node.getAttribute("Weight")).intValue();
		int finishTime = weight + _startTime;
		return finishTime;
	}
	
	/**
	 * Returns a string representation of a Task object
	 * 
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

