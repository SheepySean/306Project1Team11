package com.para11el.scheduler.graph;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class GraphViewManager {

    private static List<String> ATTR_EXCLUDES = Arrays.asList("x", "y", "z", "xy", "xyz",
            "ui.label", "ui.color", "ui.style", "ui.stylesheet", "ui.hide", "ui.quantity", "ui.antialias",
            "ui.screenshot");
    private Graph _managedGraph;

    public GraphViewManager(Graph graph) {
        _managedGraph = graph;
    }

    public void labelGraph() {
        for(Node node : _managedGraph.getNodeSet()) {
            node.addAttribute("ui.label", node.getId());
            for (Edge edge : node.getEdgeSet()) {
                edge.addAttribute("ui.label", edge.getAttribute("Weight").toString());
            }
        }
    }

    public void unlabelGraph() {
        this.removeGraphStreamAttributes(_managedGraph.getNodeSet());
        this.removeGraphStreamAttributes(_managedGraph.getEdgeSet());
    }

    public void addExcludedAttribute(String attribute) {
        ATTR_EXCLUDES.add(attribute);
    }

    private void removeGraphStreamAttributes(Collection<? extends Element> set) {
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
