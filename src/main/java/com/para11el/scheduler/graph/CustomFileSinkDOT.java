package com.para11el.scheduler.graph;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.stream.file.FileSinkBase;
import org.graphstream.stream.file.FileSinkDOT;

import javax.sound.midi.SysexMessage;

/**
 * Custom Dot Writer that extends GraphStream's FileSinkDOT
 *
 * @author Sean Oldfield
 */
public class CustomFileSinkDOT extends FileSinkDOT {
    protected PrintWriter _out;
    protected String _graphName;
    protected boolean _digraph;

    /**
     * @see FileSinkDOT
     * @param digraph True if graph is a digraph
     * @param graphName Name of the outputted graph
     */
    public CustomFileSinkDOT(boolean digraph, String graphName) {
        _graphName = graphName;
        _digraph = digraph;
    }


    public void setDirected(boolean digraph) {
        this._digraph = digraph;
    }

    public boolean isDirected() {
        return this._digraph;
    }

    // Changed
    protected void exportGraph(Graph graph) {
        String graphId = graph.getId();
        AtomicLong timeId = new AtomicLong(0L);
        graph.attributeKeys().forEach((key) -> {
            this.graphAttributeAdded(graphId, timeId.getAndIncrement(), key, graph.getAttribute(key));
        });
        Iterator var5 = graph.iterator();

        while(var5.hasNext()) {
            Node node = (Node)var5.next();
            String nodeId = node.getId();
            this._out.printf("\t%s %s;%n", nodeId, this.outputAttributes(node));
        }

        graph.edges().forEach((edge) -> {
            String fromNodeId = edge.getNode0().getId();
            String toNodeId = edge.getNode1().getId();
            String attr = this.outputAttributes(edge);
            if (this._digraph) {
                this._out.printf("\t%s -> %s", fromNodeId, toNodeId);
                if (!edge.isDirected()) {
                    this._out.printf(" -> %s", fromNodeId);
                }
            } else {
                this._out.printf("\t%s -- %s", fromNodeId, toNodeId);
            }

            this._out.printf(" %s;%n", attr);
        });
    }
    // Changed
    protected void outputHeader() throws IOException {
        this._out = (PrintWriter)this.output;
        _out.printf("%s {%n", _digraph ? "digraph \"" + _graphName +  "\"" : "graph \"" + _graphName +  "\"");

    }

    protected void outputEndOfFile() throws IOException {
        this._out.printf("}%n");
    }

    public void edgeAttributeAdded(String graphId, long timeId, String edgeId, String attribute, Object value) {
    }

    public void edgeAttributeChanged(String graphId, long timeId, String edgeId, String attribute, Object oldValue, Object newValue) {
    }

    public void edgeAttributeRemoved(String graphId, long timeId, String edgeId, String attribute) {
    }

    public void graphAttributeAdded(String graphId, long timeId, String attribute, Object value) {
        this._out.printf("\tgraph [ %s ];%n", this.outputAttribute(attribute, value, true));
    }

    public void graphAttributeChanged(String graphId, long timeId, String attribute, Object oldValue, Object newValue) {
        this._out.printf("\tgraph [ %s ];%n", this.outputAttribute(attribute, newValue, true));
    }

    public void graphAttributeRemoved(String graphId, long timeId, String attribute) {
    }

    public void nodeAttributeAdded(String graphId, long timeId, String nodeId, String attribute, Object value) {
        this._out.printf("\t\"%s\" [ %s ];%n", nodeId, this.outputAttribute(attribute, value, true));
    }

    public void nodeAttributeChanged(String graphId, long timeId, String nodeId, String attribute, Object oldValue, Object newValue) {
        this._out.printf("\t\"%s\" [ %s ];%n", nodeId, this.outputAttribute(attribute, newValue, true));
    }

    public void nodeAttributeRemoved(String graphId, long timeId, String nodeId, String attribute) {
    }

    public void edgeAdded(String graphId, long timeId, String edgeId, String fromNodeId, String toNodeId, boolean directed) {
        if (this._digraph) {
            this._out.printf("\t%s -> %s", fromNodeId, toNodeId);
            if (!directed) {
                this._out.printf(" -> %s", fromNodeId);
            }

            this._out.printf(";%n");
        } else {
            this._out.printf("\t%s -- %s;%n", fromNodeId, toNodeId);
        }

    }

    public void edgeRemoved(String graphId, long timeId, String edgeId) {
    }

    public void graphCleared(String graphId, long timeId) {
    }

    public void nodeAdded(String graphId, long timeId, String nodeId) {
        this.out.printf("\t\"%s\";%n", nodeId);
    }

    public void nodeRemoved(String graphId, long timeId, String nodeId) {
    }

    public void stepBegins(String graphId, long timeId, double step) {
    }

    protected String outputAttribute(String key, Object value, boolean first) {
        return String.format("%s%s=%s", key, value);
    }

    protected String outputAttributes(Element e) {
        if (e.getAttributeCount() == 0) {
            return "";
        } else {
            // Convert the key atrributes into an ArrayList as opposed to an Iterator
            ArrayList<String> keys = new ArrayList<>();

            e.attributeKeys().sorted(Comparator.reverseOrder()).forEach((key) -> {
                keys.add(key);
            });
            StringBuilder buffer = new StringBuilder("[");
            AtomicBoolean first = new AtomicBoolean(true);
            int count = 0;
            for(String key : keys) {
                if(!GraphViewManager.ATTR_EXCLUDES.contains(key)) {
                    if (count > 0) {
                        first.set(false);
                    }
                    Object value = e.getAttribute(key);
                    if (value instanceof Number) {
                        value = ((Number) value).intValue(); // Write a number as an int
                    }
                    count++;
                    buffer.append(String.format("%s%s=%s", first.get() ? "" : ",", key, value));
                }
            }
            return buffer.append(']').toString();
        }
    }

}

