package com.para11el.scheduler.main;

public enum ParameterConstants {
    PARALLISATION("-p"), VISUALISATION("-v"), OUTPUT("-o");

    private final String _value;

    private ParameterConstants(String value) {
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
