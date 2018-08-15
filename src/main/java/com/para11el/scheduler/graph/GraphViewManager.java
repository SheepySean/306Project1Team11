package com.para11el.scheduler.graph;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
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
        this.removeExcludedAttributes(_managedGraph.nodes(), false); // Each node
        this.removeExcludedAttributes(_managedGraph.edges(), false); // Each edge
        this.removeExcludedAttributes(_managedGraph.attributeKeys(), true); // The graph itself
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
     * @param graph True if the stream of elements relates to attribute keys coming from a graph
     * @author Sean Oldfield
     */
    private void removeExcludedAttributes(Stream<? extends Object> set, boolean graph) {
        if(graph) {
            this.removeAttributesFromElement(set.toArray(), _managedGraph);
        } else {
            set.forEach((e) -> {
                Element element = (Element) e;
                this.removeAttributesFromElement(element.attributeKeys().toArray(), element);
            });
        }
    }

    /**
     * Remove attributes from an element if they match those to be excluded
     * @param attributes Array of attributes keys for the element
     * @param element The element to remove the attributes from
     * @author Sean Oldfield
     */
    private void removeAttributesFromElement(Object[] attributes, Element element) {
        for (int i = 0; i < attributes.length; i++) { // For each attribute
            if (ATTR_EXCLUDES.contains(attributes[i])) { // If its an excluded attribute
                element.removeAttribute((String) attributes[i]); // Remove it
            }
        }
        return;
    }

}
