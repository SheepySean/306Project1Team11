package com.para11el.scheduler.algorithm;

import org.graphstream.graph.Node;

import java.util.ArrayList;

/**
 * Abstract interface to represent algorithms the can be parallelised
 *
 * @author Sean Oldfield
 *
 */
public interface Parallelised extends Traversable {
    /**
     * Finds the earliest start time of a task on a processor with given dependencies
     * @param node Node to be scheduled
     * @param schedule ArrayList of scheduled tasks
     * @param processor Processor to schedule the node on
     * @return int of the earliest start time
     *
     * @author Rebekah Berriman
     */
    default int getEarliestStartTime(Node node, ArrayList<Task> schedule, int processor) {
        int processorFinishTime;
        int nodeStartTime = 0;
        int parentLatestFinish = 0;

        // Get the latest finish time of the parents
        for (Node parent : getParents(node)){
            Task task = findNodeTask(parent, schedule);
            int nodeWeight = task.getWeight();

            if (task.getProcessor() == processor) {
                parentLatestFinish = task.getStartTime() + nodeWeight;
            } else{
                int edgeWeight = ((Number)task.getNode().getEdgeToward(node)
                        .getAttribute("Weight")).intValue();
                parentLatestFinish = task.getStartTime() + nodeWeight + edgeWeight;
            }
            if (parentLatestFinish > nodeStartTime){
                nodeStartTime = parentLatestFinish;
            }
        }

        // Get the latest finish time of the current processor
        processorFinishTime = getProcessorFinishTime(schedule, processor);
        if (processorFinishTime > nodeStartTime) {
            nodeStartTime = processorFinishTime;
        }
        return nodeStartTime;
    }


    /**
     * Returns the task corresponding to a node
     * @param node From input graph
     * @param schedule ArrayList of scheduled tasks
     * @return Task object node
     *
     * @author Sean Oldfield
     */
    default Task findNodeTask(Node node, ArrayList<Task> schedule) {
        for (Task task : schedule) {
            if (task.getNode().equals(node)) {
                return task;
            }
        }
        return null;
    }

    /**
     * Returns an int of the finishTime of the last task on the processor
     * @param schedule ArrayList of the scheduled tasks
     * @param processor To find latest finish time of
     * @return int of the finishTime
     *
     * @author Rebekah Berriman
     */
    default int getProcessorFinishTime(ArrayList<Task> schedule, int processor) {
        int processorFinishTime = 0;
        int taskFinishTime;

        for (Task task : schedule) {
            if (task.getProcessor() == processor) {
                taskFinishTime = task.getFinishTime();
                if (processorFinishTime < taskFinishTime) {
                    processorFinishTime = taskFinishTime;
                }
            }
        }
        return processorFinishTime;
    }

}
