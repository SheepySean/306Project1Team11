package com.para11el.scheduler.main.test;

import static org.junit.Assert.*;
import com.para11el.scheduler.graph.GraphConstants;
import com.para11el.scheduler.graph.GraphFileManager;
import com.para11el.scheduler.graph.GraphViewManager;
import com.para11el.scheduler.main.Scheduler;
import org.graphstream.graph.Graph;
import org.junit.*;

import java.util.Arrays;

/**
 * Simple JUnit test to test the behaviour of the Scheduler.
 *
 * @Author Sean Oldfield
 */
public class GraphResourceIT {
	private static GraphFileManager _fileManager;
	private GraphViewManager _viewManager;
	
	@BeforeClass
	public static void createManagers() {
	    _fileManager = new GraphFileManager();

	}
	
	@Test
    public void testGraphLoad() {
	    try {
            Graph g = _fileManager.readGraphFile(GraphConstants.GRAPH_DIRECTORY.getValue() +
                    "/" + GraphConstants.SAMPLE_INPUT_FILE.getValue() +
                    GraphConstants.FILE_EXT.getValue(), "Test");
            if(!(g instanceof Graph)) {
                fail();
            }
            assertNotEquals(0, g.getNodeCount());
        } catch (Exception e) {
	        e.printStackTrace();
	        fail();
        }
    }

    @Test
    public void testGraphWrite() {
        try {
            Graph g = _fileManager.readGraphFile(GraphConstants.GRAPH_DIRECTORY.getValue() +
                    "/" + GraphConstants.SAMPLE_INPUT_FILE.getValue() +
                    GraphConstants.FILE_EXT.getValue(), "Test");
            _fileManager.writeGraphFile(GraphConstants.OUTPUT_PREFIX.getValue()
                            + GraphConstants.SAMPLE_INPUT_FILE.getValue() + GraphConstants.FILE_EXT.getValue(),
                    g, true);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testViewLabelUnlabel() {
	    try {
            Graph g = _fileManager.readGraphFile(GraphConstants.GRAPH_DIRECTORY.getValue() +
                    "/" + GraphConstants.SAMPLE_INPUT_FILE.getValue() +
                    GraphConstants.FILE_EXT.getValue(), "Test");
            _viewManager = new GraphViewManager(g);
            _viewManager.labelGraph();
            assertTrue(g.getNode(0).hasAttribute("ui.label"));
            _viewManager.unlabelGraph();
            assertFalse(g.getNode(0).hasAttribute("ui.label"));
        } catch (Exception e) {
	        e.printStackTrace();
	        fail();
        }
    }

    @Test
    public void testSchedulerRun() {
	    try {
	        String[] someArgs = {"example.dot", "4"};
            Scheduler.main(someArgs);
        } catch (Exception e) {
	        e.printStackTrace();
	        fail();
        }
    }

}