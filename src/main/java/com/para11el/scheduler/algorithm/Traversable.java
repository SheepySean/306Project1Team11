package com.para11el.scheduler.algorithm;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import java.util.ArrayList;

/**
 * Abstract interface to represent graphs that can be traversed for
 * graph traversal algorithms
 *
 * @author Sean Oldfield
 *
 */
public interface Traversable {
    /**
     * Returns a list of parent nodes of a node
     * @param node Node to find parents of
     *
     * @author Tina Chen
     */
    default ArrayList<Node> getParents(Node node) {
        ArrayList<Node> parents = new ArrayList<Node>();
        node.enteringEdges().forEach((edge) -> {
            parents.add(edge.getSourceNode());
        });

        return parents;
    }

    /**
     * Find the available nodes that can be scheduled given what nodes have already
     * been scheduled
     * @param scheduledTasks
     * @return ArrayList of available nodes
     *
     * @author Rebekah Berriman, Tina Chen
     */
    default ArrayList<Node> getAvailableNodes(ArrayList<Task> scheduledTasks) {
        ArrayList<Node> scheduledNodes =  new ArrayList<Node>();
        ArrayList<Node> available = new ArrayList<Node>();

        // Get list of scheduled nodes
        for (Task task : scheduledTasks) {
            scheduledNodes.add(task.getNode());
        }

        getGSGraph().nodes().forEach((node) -> {
            // Find nodes that have not been scheduled
            if (!scheduledNodes.contains(node)) {
                ArrayList<Node> parents = getParents(node);

                // Add nodes with no parents to available nodes
                if (parents.size() == 0) {
                    available.add(node);
                    // Check all the parents of the node have been scheduled
                } else {
                    boolean availableNode = true;
                    for (Node parentNode : parents) {
                        // Node is not available if parent has not been scheduled
                        if (!scheduledNodes.contains(parentNode)) {
                            availableNode = false;
                        }
                    }
                    if (availableNode) {
                        available.add(node);
                    }
                }
            }
        });

        return available;
    }

    Graph getGSGraph();
}
