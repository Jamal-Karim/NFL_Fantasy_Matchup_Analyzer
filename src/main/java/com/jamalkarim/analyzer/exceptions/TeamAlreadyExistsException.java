package com.jamalkarim.analyzer.exceptions;

public class TeamAlreadyExistsException extends RuntimeException {
    public TeamAlreadyExistsException(String message) {
        super(message);
    }
}
