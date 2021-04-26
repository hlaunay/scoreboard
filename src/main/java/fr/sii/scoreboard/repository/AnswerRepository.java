package fr.sii.scoreboard.repository;

import fr.sii.scoreboard.domain.Answer;
import fr.sii.scoreboard.domain.Score;
import fr.sii.scoreboard.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the Answer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findAllByTeam(Team team);

    @Query(value = "(select " +
        "t.name as team, " +
        "sum(c2.points) as points, " +
        "min(a.`date`) as first " +
        "from " +
        "answer a " +
        "inner join challenge c2 on " +
        "a.challenge_id = c2 .id " +
        "inner join team t on " +
        "a.team_id = t.id " +
        "group by " +
        "t.name " +
        "order by " +
        "points desc, " +
        "first asc) " +
        "union all " +
        "(select  " +
        "t.name as team, " +
        "0 as points, " +
        "null as first " +
        "from team t  " +
        "left join answer a on a.team_id = t.id  " +
        "where a.team_id is null order by t.name asc)", nativeQuery = true)
    List<Score> findScores();

}
