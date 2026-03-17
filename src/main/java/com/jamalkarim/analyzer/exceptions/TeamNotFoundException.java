package com.jamalkarim.analyzer.exceptions;

/**
 * Exception thrown when a requested team cannot be found in the database
 */
public class TeamNotFoundException extends RuntimeException {

    /**
     * Default constructor with a generic error message.
     */
    public TeamNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs an exception with a message specifying the ID.
     *
     * @param id The unique identifier of the fantasy team
     */
    public TeamNotFoundException(long id) {
        super("Team with id " + id + " not found");
    }


}
