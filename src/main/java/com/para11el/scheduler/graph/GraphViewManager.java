package com.para11el.scheduler.graph;

import org.graphstream.graph.Graph;
import java.util.Arrays;
import java.util.List;

/**
 * Manager class that controls the view of a graph such as its ability to be labelled
 *
 * @author Sean Oldfield
 */
public class GraphViewManager {

    public static List<String> ATTR_EXCLUDES = Arrays.asList("x", "y", "z", "xy", "xyz",
            "ui.label", "ui.color", "ui.style", "ui.stylesheet", "ui.hide", "ui.quantity", "ui.antialias",
            "ui.screenshot", "ui.class");
    private Graph _managedGraph;

    /**
     * Constructor for GraphViewManager
     * @param graph The graph to be managed
     * 
     * @author Sean Oldfield
     */
    public GraphViewManager(Graph graph) {
        _managedGraph = graph;
    }

    /**
     * Label the graph with its attributes such as weight and node name for visual display
     * 
     * @author Sean Oldfield
     */
    public void labelGraph() {
        _managedGraph.nodes().forEach((node) -> {
            node.setAttribute("ui.label", node.getId()); // Name each node
            node.edges().forEach((edge) -> {
                edge.setAttribute("ui.label", ((Number)edge.getAttribute("Weight")).intValue()); // Add each edges weight
            });
        });
    }

}
