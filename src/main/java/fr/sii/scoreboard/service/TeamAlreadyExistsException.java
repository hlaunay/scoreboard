package fr.sii.scoreboard.service;

public class TeamAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TeamAlreadyExistsException() {
        super("Team already exists!");
    }
}
