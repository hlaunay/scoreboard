package fr.sii.scoreboard.web.rest;

import fr.sii.scoreboard.domain.Team;
import fr.sii.scoreboard.domain.User;
import fr.sii.scoreboard.security.AuthoritiesConstants;
import fr.sii.scoreboard.service.AnswerService;
import fr.sii.scoreboard.service.TeamQueryService;
import fr.sii.scoreboard.service.TeamService;
import fr.sii.scoreboard.service.UserService;
import fr.sii.scoreboard.service.criteria.TeamCriteria;
import fr.sii.scoreboard.service.dto.ScoreDTO;
import fr.sii.scoreboard.service.dto.TeamDTO;
import fr.sii.scoreboard.service.dto.TeamJoinDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link fr.sii.scoreboard.domain.Team}.
 */
@RestController
@RequestMapping("/api")
public class TeamResource {

    private final Logger log = LoggerFactory.getLogger(TeamResource.class);

    private static final String ENTITY_NAME = "team";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;

    private final AnswerService answerService;

    private final TeamService teamService;

    private final TeamQueryService teamQueryService;

    public TeamResource(UserService userService, AnswerService answerService, TeamService teamService, TeamQueryService teamQueryService) {
        this.userService = userService;
        this.answerService = answerService;
        this.teamService = teamService;
        this.teamQueryService = teamQueryService;
    }

    /**
     * {@code GET  /teams} : get all the teams.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of teams in body.
     */
    @GetMapping("/teams")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\") or  hasAuthority(\"" + AuthoritiesConstants.NO_TEAM + "\")")
    public ResponseEntity<List<TeamDTO>> getAllTeams(TeamCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Teams by criteria: {}", criteria);
        Page<TeamDTO> page = teamQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /teams/count} : count all the teams.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/teams/count")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\") or  hasAuthority(\"" + AuthoritiesConstants.NO_TEAM + "\")")
    public ResponseEntity<Long> countTeams(TeamCriteria criteria) {
        log.debug("REST request to count Teams by criteria: {}", criteria);
        return ResponseEntity.ok().body(teamQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /teams/:id} : get the "id" team.
     *
     * @param id the id of the teamDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the teamDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/teams/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<TeamDTO> getTeam(@PathVariable Long id) {
        log.debug("REST request to get Team : {}", id);
        Optional<TeamDTO> teamDTO = teamService.findOne(id);
        return ResponseUtil.wrapOrNotFound(teamDTO);
    }

    /**
     * {@code DELETE  /teams/:id} : delete the "id" team.
     *
     * @param id the id of the teamDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/teams/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        log.debug("REST request to delete Team : {}", id);
        teamService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }


    @PostMapping(path = "/account/team/create")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.NO_TEAM + "\")")
    public ResponseEntity<Void> createTeam(@Valid @RequestBody TeamJoinDTO teamDTO) throws URISyntaxException {
        log.debug("REST request to save Team : {}", teamDTO);
        User user = userService.getUserWithAuthorities().orElseThrow(() -> new RuntimeException("User could not be found"));
        Team team = teamService.save(teamDTO);
        userService.joinTeam(user, team);

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createAlert(applicationName, "A user is updated with identifier " + user.getLogin(), user.getLogin()))
            .build();
    }

    @PutMapping(path = "/account/team/join")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.NO_TEAM + "\")")
    public ResponseEntity<Void> joinTeam(@Valid @RequestBody TeamJoinDTO teamDTO) throws URISyntaxException {
        log.debug("REST request to join Team : {}", teamDTO);
        User user = userService.getUserWithAuthorities().orElseThrow(() -> new RuntimeException("User could not be found"));
        userService.joinTeam(user, teamDTO);

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createAlert(applicationName, "A user is updated with identifier " + user.getLogin(), user.getLogin()))
            .build();
    }

    @GetMapping(path = "/teams/ranking")
    public List<ScoreDTO> getScores() {
        log.debug("REST request to get Scores");
        return answerService.findScores();
    }
}
