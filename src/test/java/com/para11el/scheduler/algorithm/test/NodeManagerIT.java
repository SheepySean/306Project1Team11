package com.para11el.scheduler.algorithm.test;

import static org.junit.Assert.*;

import org.graphstream.graph.Graph;
import org.junit.BeforeClass;
import org.junit.Test;

import com.para11el.scheduler.algorithm.NodeManager;

/**
 * JUnit test class to test the behaviour of the NodeManager class.
 *  
 * @author Holly Hagenson
 *
 */
public class NodeManagerIT {
	private static Graph _testGraph;
	private static TestGraphManager _tgManager; 
	private static NodeManager _nm; 
	
	@BeforeClass
	public static void initialise(){
		_tgManager = new TestGraphManager(); 
		_testGraph = _tgManager.createGraph();
		_nm = new NodeManager(_testGraph); 
	}
	
	/**
	 * Check that the bottom level of each node in the graph is being 
	 * calculated correctly. 
	 * 
	 * @author Holly Hagenson
	 */
	@Test
	public void testGetBottomLevel(){ 
		int bottomLevel1 = _nm.getBottomLevel(_testGraph.getNode("1")); 
		int bottomLevel2 = _nm.getBottomLevel(_testGraph.getNode("2"));
		int bottomLevel3 = _nm.getBottomLevel(_testGraph.getNode("3"));
		int bottomLevel4 = _nm.getBottomLevel(_testGraph.getNode("4"));
		int bottomLevel5 = _nm.getBottomLevel(_testGraph.getNode("5"));
		
		assertEquals(12, bottomLevel1);
		assertEquals(9, bottomLevel2);
		assertEquals(2, bottomLevel3);
		assertEquals(1, bottomLevel4);
		assertEquals(5, bottomLevel5);
	}

}
