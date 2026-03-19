package com.jamalkarim.analyzer.exceptions;

/**
 * Exception thrown when a requested player or team matchup cannot be found in the database.
 */
public class MatchupNotFoundException extends RuntimeException {

    /**
     * Constructs an exception with a specific message.
     *
     * @param message The detail message
     */
    public MatchupNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs an exception specifying the ID of the missing matchup.
     *
     * @param id The unique identifier of the matchup
     */
    public MatchupNotFoundException(long id) {
        super("Matchup with id " + id + " not found");
    }
}
