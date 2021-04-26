package fr.sii.scoreboard.domain;

import java.time.Instant;

public interface Score {
    String getTeam();

    Integer getPoints();

    Instant getFirst();
}
