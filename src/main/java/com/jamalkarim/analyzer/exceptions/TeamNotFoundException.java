package com.jamalkarim.analyzer.exceptions;

public class TeamNotFoundException extends RuntimeException {
    public TeamNotFoundException(String message) {
        super(message);
    }

    public TeamNotFoundException(long id) {
        super("Team with id " + id + " not found");
    }


}
