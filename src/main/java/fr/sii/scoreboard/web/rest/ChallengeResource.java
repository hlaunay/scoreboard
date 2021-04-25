package fr.sii.scoreboard.web.rest;

import fr.sii.scoreboard.repository.ChallengeRepository;
import fr.sii.scoreboard.security.AuthoritiesConstants;
import fr.sii.scoreboard.security.SecurityUtils;
import fr.sii.scoreboard.service.ChallengeQueryService;
import fr.sii.scoreboard.service.ChallengeService;
import fr.sii.scoreboard.service.criteria.ChallengeCriteria;
import fr.sii.scoreboard.service.dto.ChallengeDTO;
import fr.sii.scoreboard.web.rest.errors.BadRequestAlertException;
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
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link fr.sii.scoreboard.domain.Challenge}.
 */
@RestController
@RequestMapping("/api")
public class ChallengeResource {

    private final Logger log = LoggerFactory.getLogger(ChallengeResource.class);

    private static final String ENTITY_NAME = "challenge";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChallengeService challengeService;

    private final ChallengeRepository challengeRepository;

    private final ChallengeQueryService challengeQueryService;

    public ChallengeResource(
        ChallengeService challengeService,
        ChallengeRepository challengeRepository,
        ChallengeQueryService challengeQueryService
    ) {
        this.challengeService = challengeService;
        this.challengeRepository = challengeRepository;
        this.challengeQueryService = challengeQueryService;
    }

    /**
     * {@code POST  /challenges} : Create a new challenge.
     *
     * @param challengeDTO the challengeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new challengeDTO, or with status {@code 400 (Bad Request)} if the challenge has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/challenges")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<ChallengeDTO> createChallenge(@Valid @RequestBody ChallengeDTO challengeDTO) throws URISyntaxException {
        log.debug("REST request to save Challenge : {}", challengeDTO);
        if (challengeDTO.getId() != null) {
            throw new BadRequestAlertException("A new challenge cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChallengeDTO result = challengeService.save(challengeDTO);
        return ResponseEntity
            .created(new URI("/api/challenges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /challenges/:id} : Updates an existing challenge.
     *
     * @param id           the id of the challengeDTO to save.
     * @param challengeDTO the challengeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated challengeDTO,
     * or with status {@code 400 (Bad Request)} if the challengeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the challengeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/challenges/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<ChallengeDTO> updateChallenge(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ChallengeDTO challengeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Challenge : {}, {}", id, challengeDTO);
        if (challengeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, challengeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!challengeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ChallengeDTO result = challengeService.save(challengeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, challengeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /challenges/:id} : Partial updates given fields of an existing challenge, field will ignore if it is null
     *
     * @param id the id of the challengeDTO to save.
     * @param challengeDTO the challengeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated challengeDTO,
     * or with status {@code 400 (Bad Request)} if the challengeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the challengeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the challengeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/challenges/{id}", consumes = "application/merge-patch+json")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<ChallengeDTO> partialUpdateChallenge(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ChallengeDTO challengeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Challenge partially : {}, {}", id, challengeDTO);
        if (challengeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, challengeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!challengeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ChallengeDTO> result = challengeService.partialUpdate(challengeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, challengeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /challenges} : get all the challenges.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of challenges in body.
     */
    @GetMapping("/challenges")
    public ResponseEntity<List<ChallengeDTO>> getAllChallenges(ChallengeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Challenges by criteria: {}", criteria);
        Page<ChallengeDTO> page = challengeQueryService.findByCriteria(criteria, pageable, SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /challenges/count} : count all the challenges.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/challenges/count")
    public ResponseEntity<Long> countChallenges(ChallengeCriteria criteria) {
        log.debug("REST request to count Challenges by criteria: {}", criteria);
        return ResponseEntity.ok().body(challengeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /challenges/:id} : get the "id" challenge.
     *
     * @param id the id of the challengeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the challengeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/challenges/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<ChallengeDTO> getChallenge(@PathVariable Long id) {
        log.debug("REST request to get Challenge : {}", id);
        Optional<ChallengeDTO> challengeDTO = challengeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(challengeDTO);
    }

    /**
     * {@code DELETE  /challenges/:id} : delete the "id" challenge.
     *
     * @param id the id of the challengeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/challenges/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteChallenge(@PathVariable Long id) {
        log.debug("REST request to delete Challenge : {}", id);
        challengeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
