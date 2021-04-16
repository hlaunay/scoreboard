package fr.sii.scoreboard.service;

import fr.sii.scoreboard.domain.Team;
import fr.sii.scoreboard.repository.TeamRepository;
import fr.sii.scoreboard.service.dto.TeamCreateDTO;
import fr.sii.scoreboard.service.dto.TeamDTO;
import fr.sii.scoreboard.service.mapper.TeamMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Team}.
 */
@Service
@Transactional
public class TeamService {

    private final Logger log = LoggerFactory.getLogger(TeamService.class);

    private final TeamRepository teamRepository;

    private final TeamMapper teamMapper;

    private final PasswordEncoder passwordEncoder;

    public TeamService(TeamRepository teamRepository, TeamMapper teamMapper, PasswordEncoder passwordEncoder) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Save a team.
     *
     * @param teamDTO the entity to save.
     * @return the persisted entity.
     */
    public Team save(TeamCreateDTO teamDTO) {
        log.debug("Request to save Team : {}", teamDTO);
        Team team = new Team();
        team.setName(teamDTO.getName());
        team.password(passwordEncoder.encode(teamDTO.getPassword()));
        team = teamRepository.save(team);
        return team;
    }

    /**
     * Get all the teams.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TeamDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Teams");
        return teamRepository.findAll(pageable).map(teamMapper::toDto);
    }

    /**
     * Get one team by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TeamDTO> findOne(Long id) {
        log.debug("Request to get Team : {}", id);
        return teamRepository.findById(id).map(teamMapper::toDto);
    }

    /**
     * Delete the team by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Team : {}", id);
        teamRepository.deleteById(id);
    }
}
