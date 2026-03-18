package com.jamalkarim.analyzer.exceptions;

public class PlayerAlreadyRosteredException extends RuntimeException {
    public PlayerAlreadyRosteredException(String message) {
        super(message);
    }
}
