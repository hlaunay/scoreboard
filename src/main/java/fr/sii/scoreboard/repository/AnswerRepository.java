package fr.sii.scoreboard.repository;

import fr.sii.scoreboard.domain.Answer;
import fr.sii.scoreboard.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the Answer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findAllByTeam(Team team);

}
