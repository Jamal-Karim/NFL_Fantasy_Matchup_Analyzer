package com.jamalkarim.analyzer.exceptions;

/**
 * Exception thrown when an attempt is made to add a player to a team roster when they are already assigned to another team.
 */
public class PlayerAlreadyRosteredException extends RuntimeException {

    /**
     * Constructs an exception with a specific error message.
     *
     * @param message The detail message
     */
    public PlayerAlreadyRosteredException(String message) {
        super(message);
    }
}
