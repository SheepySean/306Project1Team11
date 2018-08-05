package com.para11el.scheduler.graph;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.stream.file.FileSinkDOT;

/**
 * Custom Dot Writer that extends GraphStream's FileSinkDOT
 *
 * @author Sean Oldfield
 */
public class CustomFileSinkDot extends FileSinkDOT {
    protected PrintWriter _out;
    protected String _graphName;
    protected boolean _digraph;


    /**
     * @see FileSinkDOT
     * @param digraph True if graph is a digraph
     * @param graphName Name of the outputted graph
     */
    public CustomFileSinkDot(boolean digraph, String graphName) {
        _graphName = graphName;
        _digraph = digraph;
    }


    /**
     * @see FileSinkDOT#exportGraph(Graph)
     */
    protected void exportGraph(Graph graph) {
        String graphId = graph.getId();
        long timeId = 0L;
        Iterator i$ = graph.getAttributeKeySet().iterator();

        while(i$.hasNext()) {
            String key = (String)i$.next();
            graphAttributeAdded(graphId, timeId++, key, graph.getAttribute(key));
        }

        i$ = graph.iterator();

        String fromNodeId;
        while(i$.hasNext()) {
            Node node = (Node)i$.next();
            fromNodeId = node.getId();
            _out.printf("\t%s %s;%n", fromNodeId, outputAttributes(node));
        }

        String attr;
        for(i$ = graph.getEachEdge().iterator(); i$.hasNext(); _out.printf(" %s;%n", attr)) {
            Edge edge = (Edge)i$.next();
            fromNodeId = edge.getNode0().getId();
            String toNodeId = edge.getNode1().getId();
            attr = outputAttributes(edge);
            if (_digraph) {
                _out.printf("\t%s -> %s", fromNodeId, toNodeId);
                if (!edge.isDirected()) {
                    _out.printf(" -> %s", fromNodeId);
                }
            } else {
                _out.printf("\t%s -- %s", fromNodeId, toNodeId);
            }
        }
    }

    /**
     * @see FileSinkDOT#outputHeader()
     */
    protected void outputHeader() throws IOException {
        _out = (PrintWriter)output;
        _out.printf("%s {%n", _digraph ? "digraph \"" + _graphName +  "\"" : "graph \"" + _graphName +  "\"");
    }

    /**
     * @see FileSinkDOT#outputEndOfFile()
     */
    protected void outputEndOfFile() throws IOException {
        _out.printf("}%n");
    }

    /**
     * @see FileSinkDOT#outputAttributes(Element)
     */
    protected String outputAttributes(Element e) {
        if (e.getAttributeCount() == 0) {
            return "";
        } else {
            StringBuilder buffer = new StringBuilder("[");
            boolean first = true;

            // Convert the key atrributes into an ArrayList as opposed to an Iterator
            ArrayList<String> keys = new ArrayList<>();
            Iterator<String> i$ = e.getEachAttributeKey().iterator();
            while (i$.hasNext()) {
                keys.add(i$.next());
            }
            Collections.reverse(keys); // Reverse the list for correct output formatting
            int count = 0;
            for(String key : keys) {
                if(count > 0) {
                    first = false;
                }
                boolean quote = true;
                Object value = e.getAttribute(key);
                if (value instanceof Number) {
                    quote = false;
                    value = ((Number) value).intValue(); // Write a number as an int
                }
                count++;
                buffer.append(String.format("%s%s=%s%s%s", first ? "" : ",", key, quote ? "" : "", value, quote ? "" : ""));
            }

            return buffer.append(']').toString();
        }
    }

}
