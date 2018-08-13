package com.para11el.scheduler.algorithm.test;

import org.graphstream.graph.implementations.AbstractGraph;
import org.graphstream.graph.implementations.SingleNode;

/**
 * 
 * Mock class for graphstream SingleNode that implements Node. 
 * 
 * @author Jessica_Alcantara
 *
 */
public class MockNode extends SingleNode{
	
	private Number _weight;

	protected MockNode(AbstractGraph graph, String id) {
		super(graph, id);
	}
	
	public MockNode(AbstractGraph graph, String id, int weight) {
		super(graph, id);
		_weight = weight;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAttribute(String key) {
		if (key.equals("Weight")) {
			return (T) _weight;
		}
		return null;
	}
}
