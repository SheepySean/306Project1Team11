package com.para11el.scheduler.processor;

/**
 * Class representing an edge of a graph
 *
 * @author Jessica Alcantara, Holly Hagenson
 */
public class Edge {
	
	private String _source;
	private String _target;
	private int _cost;
	
	public Edge(String source, String target, int cost) {
		_source = source;
		_target = target;
		_cost = cost;
	}
	
	public String getSource() {
		return _source;
	}
	
	public String getTarget() {
		return _target;
	}
	
	public int getCost() {
		return _cost;
	}

}
