package fr.sii.scoreboard.web.rest.errors;

public class TeamAlreadyExistsException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public TeamAlreadyExistsException() {
        super("Team name already used!", "team", "teamexists");
    }
}
