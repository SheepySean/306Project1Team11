package com.para11el.scheduler.algorithm.test;

import org.graphstream.graph.implementations.AbstractGraph;
import org.graphstream.graph.implementations.SingleNode;

/**
 * 
 * Mock class for graphstream AbstractElement. 
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

	@Override
	public Object getAttribute(String arg0) {
		if (arg0.equals("Weight")) {
			return _weight;
		}
		return null;
	}
}
