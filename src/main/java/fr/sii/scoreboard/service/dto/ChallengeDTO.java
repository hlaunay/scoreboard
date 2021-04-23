package fr.sii.scoreboard.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.sii.scoreboard.domain.Challenge} entity.
 */
public class ChallengeDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 4, max = 100)
    private String name;

    private String description;

    @NotNull
    private Integer points;

    @NotNull
    private String answer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChallengeDTO)) {
            return false;
        }

        ChallengeDTO challengeDTO = (ChallengeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, challengeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChallengeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", points=" + getPoints() +
            ", answer='" + getAnswer() + "'" +
            "}";
    }
}
