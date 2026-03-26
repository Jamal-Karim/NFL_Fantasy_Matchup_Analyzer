package com.jamalkarim.analyzer.cucumber.utils;

/**
 * Thrown when a test variable cannot be resolved or saved during 
 * Cucumber scenario execution.
 */
public class TestVariableException extends RuntimeException {
    public TestVariableException(String message) {
        super(message);
    }
}
