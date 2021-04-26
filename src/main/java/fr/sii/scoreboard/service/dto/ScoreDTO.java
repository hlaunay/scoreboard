package fr.sii.scoreboard.service.dto;

import java.time.Instant;

public class ScoreDTO {

    private Integer position;

    private String team;

    private Integer points;

    private Instant firstAnswer;

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Instant getFirstAnswer() {
        return firstAnswer;
    }

    public void setFirstAnswer(Instant firstAnswer) {
        this.firstAnswer = firstAnswer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScoreDTO scoreDTO = (ScoreDTO) o;

        if (position != null ? !position.equals(scoreDTO.position) : scoreDTO.position != null) return false;
        if (team != null ? !team.equals(scoreDTO.team) : scoreDTO.team != null) return false;
        if (points != null ? !points.equals(scoreDTO.points) : scoreDTO.points != null) return false;
        return firstAnswer != null ? firstAnswer.equals(scoreDTO.firstAnswer) : scoreDTO.firstAnswer == null;
    }

    @Override
    public int hashCode() {
        int result = position != null ? position.hashCode() : 0;
        result = 31 * result + (team != null ? team.hashCode() : 0);
        result = 31 * result + (points != null ? points.hashCode() : 0);
        result = 31 * result + (firstAnswer != null ? firstAnswer.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ScoreDTO{" +
            "position=" + position +
            ", team='" + team + '\'' +
            ", points=" + points +
            ", firstAnswer=" + firstAnswer +
            '}';
    }
}
