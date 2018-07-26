package com.para11el.scheduler.graph;

/**
 * Enum for representing constants used for graph file I/O
 *
 * @Author Sean Oldfield
 */
public enum GraphConstants {
    GRAPH_DIRECTORY("example_graphs"), SAMPLE_INPUT_FILE("Nodes_7_OutTree"), FILE_EXT(".dot"),
    OUTPUT_PREFIX("out_");

    private String _value;

    GraphConstants(String value) {
        _value = value;
    }

    /**
     * Return a corresponding string representing a value for a different Graph constant
     * @return String
     */
    public String getValue() {
        return _value;
    }
}
