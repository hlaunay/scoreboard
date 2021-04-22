package fr.sii.scoreboard.service;

public class TeamJoinException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public TeamJoinException() {
        super("Team does not exist!");
    }
}
