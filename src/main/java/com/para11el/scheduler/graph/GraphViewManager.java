package com.para11el.scheduler.graph;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Manager class that controls the view of a graph such as its ability to be labelled or not
 *
 * @Author Sean Oldfield
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
     */
    public void labelGraph() {
        for(Node node : _managedGraph.getNodeSet()) {
            node.addAttribute("ui.label", node.getId());
            for (Edge edge : node.getEdgeSet()) {
                edge.addAttribute("ui.label", edge.getAttribute("Weight").toString());
            }
        }
    }

    /**
     * Remove the graphical labels of the graph and only leave the key attributes such as weight behind
     */
    public void unlabelGraph() {
        this.removeExcludedAttributes(_managedGraph.getNodeSet());
        this.removeExcludedAttributes(_managedGraph.getEdgeSet());
    }

    /**
     * Add an attribute to be removed from the graph when being saved. This is an attribute that doesn't
     * need to be included in .dot file. This exists because you may find adding an attribute for CSS control
     * useful in later visualisation.
     * @param attribute Name of the attribute that needs to be excluded
     */
    public void addExcludedAttribute(String attribute) {
        ATTR_EXCLUDES.add(attribute);
    }

    /**
     * Remove excluded attributes from a set of elements from the graph.
     * @param set Set of elements from the graph that need certain attributes removed.
     */
    private void removeExcludedAttributes(Collection<? extends Element> set) {
        for(Element e : set) {
            Object[] attrs = e.getAttributeKeySet().toArray();
            for(int i = 0; i < attrs.length; i++) {
                if(ATTR_EXCLUDES.contains(attrs[i])) {
                    e.removeAttribute((String)attrs[i]);
                }
            }
        }
    }
}
