package fr.sii.scoreboard.web.rest.errors;

public class TeamJoinException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public TeamJoinException() {
        super("An error has occurred! team cannot be joined.", "account", "error");
    }
}
