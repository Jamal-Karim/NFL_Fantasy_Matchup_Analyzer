package com.jamalkarim.analyzer.exceptions;

/**
 * Exception thrown when a requested player cannot be found in the database
 * or from an external data provider.
 */
public class PlayerNotFoundException extends RuntimeException {

    /**
     * Constructs an exception with a message specifying name and team.
     *
     * @param name The name of the player
     * @param team The team they play for
     */
    public PlayerNotFoundException(String name, String team) {
        super("Player '" + name + "' not found on team: " + team);
    }

    /**
     * Constructs an exception with a message specifying the ID.
     *
     * @param id The unique identifier of the player
     */
    public PlayerNotFoundException(long id) {
        super("Player with id " + id + " not found");
    }

    /**
     * Default constructor with a generic error message.
     */
    public PlayerNotFoundException() {
        super("Player does not exist");
    }

}