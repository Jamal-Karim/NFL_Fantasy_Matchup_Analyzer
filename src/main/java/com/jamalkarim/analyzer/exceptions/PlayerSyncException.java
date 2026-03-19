package com.jamalkarim.analyzer.exceptions;

/**
 * Exception thrown when there is a failure in synchronizing player data from an external provider
 * or mapping it to the local persistence layer.
 */
public class PlayerSyncException extends RuntimeException {

    /**
     * Constructs an exception with a specific error message.
     *
     * @param message The detail message
     */
    public PlayerSyncException(String message) {
        super(message);
    }
}
