package com.para11el.scheduler.algorithm.test;

import org.graphstream.graph.implementations.AbstractGraph;
import org.graphstream.graph.implementations.SingleNode;

/**
 * 
 * Mock class for GraphStream AbstractElement. 
 * 
 * @author Jessica Alcantara
 *
 */
public class MockNode extends SingleNode{
	
	private Number _weight;

	/**
	 * 
	 * @param graph
	 * @param id
	 * 
	 * @author Jessica Alcantara
	 */
	protected MockNode(AbstractGraph graph, String id) {
		super(graph, id);
	}
	
	/**
	 * 
	 * @param graph
	 * @param id
	 * @param weight
	 * 
	 * @author Jessica Alcantara
	 */
	public MockNode(AbstractGraph graph, String id, int weight) {
		super(graph, id);
		_weight = weight;
	}

	/**
	 * 
	 * @author Jessica Alcantara
	 */
	@Override
	public Object getAttribute(String arg0) {
		if (arg0.equals("Weight")) {
			return _weight;
		}
		return null;
	}
}
