package com.para11el.scheduler.main;


/**
 * Enum for representing constants used for command line inputs
 *
 * @author Sean Oldfield
 */
public enum ParameterConstants {
    PARALLISATION("-p"), VISUALISATION("-v"), OUTPUT("-o"), REQUIRED_PARAMS("2");

    private final String _value;

    ParameterConstants(String value) {
        _value = value;
    }

    /**
     * Return a corresponding string representing a value for a different parameter constant
     * @return String
     */
    public String getValue() {
        return _value;
    }

}
