package com.jamalkarim.analyzer.exceptions;

/**
 * Exception thrown when a fantasy team's roster violates construction rules,
 * such as exceeding position limits or having insufficient players for analysis.
 */
public class InvalidRosterException extends RuntimeException {

    /**
     * Constructs an exception with a specific error message.
     *
     * @param message The detail message
     */
    public InvalidRosterException(String message) {
        super(message);
    }
}
