package com.jamalkarim.analyzer.exceptions;

/**
 * Exception thrown when an attempt is made to add a player to a team roster when they are already assigned to another team.
 */
public class PlayerAlreadyRosteredException extends RuntimeException {
    public PlayerAlreadyRosteredException(String message) {
        super(message);
    }
}
