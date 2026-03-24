package com.jamalkarim.analyzer.exceptions;

/**
 * Exception thrown when an attempt is made to create a team that already exists in the system.
 */
public class TeamAlreadyExistsException extends RuntimeException {

    /**
     * Constructs an exception with a specific error message.
     *
     * @param message The detail message
     */
    public TeamAlreadyExistsException(String message) {
        super(message);
    }
}
