package com.para11el.scheduler.main.test;

import static org.junit.Assert.*;
import com.para11el.scheduler.graph.GraphConstants;
import com.para11el.scheduler.graph.GraphFileManager;
import com.para11el.scheduler.graph.GraphViewManager;
import com.para11el.scheduler.main.Scheduler;
import org.graphstream.graph.Graph;
import org.junit.*;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.internal.CheckExitCalled;

import java.awt.*;

/**
 * Simple JUnit test to test the behaviour of the Scheduler.
 *
 * @author Sean Oldfield
 */
public class GraphResourceIT {
	private static GraphFileManager _fileManager;
	private GraphViewManager _viewManager;
	
	@Rule
	public final ExpectedSystemExit exit = ExpectedSystemExit.none();
	
	@BeforeClass
	public static void createManagers() {
	    _fileManager = new GraphFileManager();

	}
	
	/**
	 * Ensures the graph is able to load
	 */
	@Test
    public void testGraphLoad() {
	    try {
            Graph g = _fileManager.readGraphFile(GraphConstants.GRAPH_DIRECTORY.getValue() +
                    "/" + GraphConstants.SAMPLE_INPUT_FILE.getValue() +
                    GraphConstants.FILE_EXT.getValue(), "Test");
            if(g == null) {
                fail();
            }
            assertNotEquals(0, g.getNodeCount());
        } catch (Exception e) {
	        e.printStackTrace();
	        fail();
        }
    }

	/**
	 * Ensures the graph is able to write to the output file
	 */
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

    /**
     * Ensures that the labels for visualisation are properly removed
     */
    @Test
    public void testViewLabelUnlabel() {
	    try {
            Graph g = _fileManager.readGraphFile(GraphConstants.GRAPH_DIRECTORY.getValue() +
                    "/" + GraphConstants.SAMPLE_INPUT_FILE.getValue() +
                    GraphConstants.FILE_EXT.getValue(), "Test");
            _viewManager = new GraphViewManager(g);
            _viewManager.labelGraph();
            assertTrue(g.getNode(0).hasAttribute("ui.label"));
        } catch (Exception e) {
	        e.printStackTrace();
	        fail();
        }
    }

    /**
     * Ensure the program runs as expected
     */
    @Test
    public void testSchedulerRun() {
    	try {
	        String[] someArgs = {"example.dot", "4"};
	        Scheduler.main(someArgs);
	        exit.expectSystemExitWithStatus(1);
        } catch (HeadlessException e) {
        } catch (CheckExitCalled e) {
        } catch (Exception e) {
        	fail();
        }
    }
}

