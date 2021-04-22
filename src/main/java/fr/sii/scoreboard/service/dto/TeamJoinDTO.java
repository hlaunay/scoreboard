package fr.sii.scoreboard.service.dto;

import fr.sii.scoreboard.domain.Team;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link Team} entity.
 */
public class TeamJoinDTO implements Serializable {

    @NotNull
    @Size(min = 3, max = 50)
    private String name;

    @NotNull
    @Size(min = 4, max = 50)
    private String password;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TeamJoinDTO)) {
            return false;
        }

        TeamJoinDTO teamDTO = (TeamJoinDTO) o;
        if (this.name == null) {
            return false;
        }
        return Objects.equals(this.name, teamDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TeamDTO{" +
            ", name='" + getName() + "'" +
            "}";
    }
}
