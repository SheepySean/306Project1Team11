package com.para11el.scheduler.graph;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.graphicGraph.GraphicGraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

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
     */
    public GraphViewManager(Graph graph) {
        _managedGraph = graph;
    }

    /**
     * Label the graph with its attributes such as weight and node name for visual display
     * @author Sean Oldfield
     */
    public void labelGraph() {
        _managedGraph.nodes().forEach((node) -> {
            node.setAttribute("ui.label", node.getId()); // Name each node
            node.edges().forEach((edge) -> {
                edge.setAttribute("ui.label", edge.getAttribute("Weight").toString()); // Add each edges weight
            });
        });
    }

}
