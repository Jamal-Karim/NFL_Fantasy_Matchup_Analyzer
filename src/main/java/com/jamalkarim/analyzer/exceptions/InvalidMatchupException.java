package com.jamalkarim.analyzer.exceptions;

/**
 * Thrown when a matchup analysis is requested between incompatible or identical entities.
 */
public class InvalidMatchupException extends RuntimeException {
    public InvalidMatchupException(String message) {
        super(message);
    }
}
