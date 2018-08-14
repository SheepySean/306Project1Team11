package com.para11el.scheduler.graph;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * Manager class that controls the view of a graph such as its ability to be labelled or not
 *
 * @author Sean Oldfield
 */
public class GraphViewManager {

    private static List<String> ATTR_EXCLUDES = Arrays.asList("x", "y", "z", "xy", "xyz",
            "ui.label", "ui.color", "ui.style", "ui.stylesheet", "ui.hide", "ui.quantity", "ui.antialias",
            "ui.screenshot");
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

    /**
     * Remove the graphical labels of the graph and only leave the key attributes such as weight behind
     * @author Sean Oldfield
     */
    public void unlabelGraph() {
        this.removeExcludedAttributes(_managedGraph.nodes()); // Each node
        this.removeExcludedAttributes(_managedGraph.edges()); // Each edge
    }

    /**
     * Add an attribute to be removed from the graph when being saved. This is an attribute that doesn't
     * need to be included in .dot file. This exists because you may find adding an attribute for CSS control
     * useful in later visualisation.
     * @param attribute Name of the attribute that needs to be excluded
     * @author Sean Oldfield
     */
    public void addExcludedAttribute(String attribute) {
        ATTR_EXCLUDES.add(attribute);
    }

    /**
     * Remove excluded attributes from a set of elements from the graph.
     * @param set Set of elements from the graph that need certain attributes removed.
     * @author Sean Oldfield
     */
    private void removeExcludedAttributes(Stream<? extends Element> set) {
        set.forEach((e) -> {
            Object[] attrs = e.attributeKeys().toArray(); // Each attribute the element has
            for (int i = 0; i < attrs.length; i++) { // For each attribute
                if (ATTR_EXCLUDES.contains(attrs[i])) { // If its an excluded attribute
                    e.removeAttribute((String) attrs[i]); // Remove it
                }
            }
        });
    }
}
