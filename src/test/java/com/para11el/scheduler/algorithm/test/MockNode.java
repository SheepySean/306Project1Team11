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
	 * Constructor for MockNode
	 * @param graph Graph that node is from
	 * @param id Node Id
	 * 
	 * @author Jessica Alcantara
	 */
	protected MockNode(AbstractGraph graph, String id) {
		super(graph, id);
	}
	
	/**
	 * Constructor for MockNode
	 * @param graph Graph that node is from
	 * @param id Node Id
	 * @param weight Node Weight
	 * 
	 * @author Jessica Alcantara
	 */
	public MockNode(AbstractGraph graph, String id, int weight) {
		super(graph, id);
		_weight = weight;
	}

	/**
	 * Returns the weight of the node without referring to the graph
	 * @param attribute Attribute to be returned
	 * @return object Value of attribute
	 * 
	 * @author Jessica Alcantara
	 */
	@Override
	public Object getAttribute(String attribute) {
		if (attribute.equals("Weight")) {
			return _weight;
		}
		return null;
	}
}
