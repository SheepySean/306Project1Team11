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

	public Node get_node() {
		return _node;
	}

	public void set_node(Node _node) {
		this._node = _node;
	}

	public int get_startTime() {
		return _startTime;
	}

	public void set_startTime(int _startTime) {
		this._startTime = _startTime;
	}

	public int get_processor() {
		return _processor;
	}

	public void set_processor(int _processor) {
		this._processor = _processor;
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

