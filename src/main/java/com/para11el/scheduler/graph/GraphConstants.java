package com.para11el.scheduler.graph;

public enum GraphConstants {
    GRAPH_DIRECTORY("example_graphs"), SAMPLE_INPUT_FILE("Nodes_7_OutTree"), FILE_EXT(".dot"),
    OUTPUT_PREFIX("out_");

    private String _value;

    private GraphConstants(String value) {
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
