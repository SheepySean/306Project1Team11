package algorithm;

import org.graphstream.graph.Node;

/**
 * Task class to represent an object Task with parameters:
 * @param _node = a task in the input graph
 * @param _startTime = the start time of the task in scheduled processor
 * @param _processor = the scheduled processor
 * 
 * @author Tina Chen, Rebekah Berriman
 * 
 */

public class Task {
	
	private Node _node;
	private int _startTime;
	private int _processor;
	
	public Task(Node n, int t, int p) {
		_node = n;
		_startTime = t;
		_processor = p;
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
}
