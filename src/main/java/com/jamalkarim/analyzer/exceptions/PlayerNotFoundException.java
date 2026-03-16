package com.jamalkarim.analyzer.exceptions;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(String name, String team) {
        super("Player '" + name + "' not found on team: " + team);
    }

    public PlayerNotFoundException(long id) {
        super("Player with id " + id + " not found");
    }

    public PlayerNotFoundException() {
        super("Player does not exist");
    }

}