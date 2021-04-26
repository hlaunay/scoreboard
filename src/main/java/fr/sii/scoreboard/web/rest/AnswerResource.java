package fr.sii.scoreboard.web.rest;

import fr.sii.scoreboard.domain.User;
import fr.sii.scoreboard.repository.AnswerRepository;
import fr.sii.scoreboard.security.AuthoritiesConstants;
import fr.sii.scoreboard.service.AnswerService;
import fr.sii.scoreboard.service.UserService;
import fr.sii.scoreboard.service.dto.AnswerDTO;
import fr.sii.scoreboard.service.dto.AnswerSubmitDTO;
import fr.sii.scoreboard.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link fr.sii.scoreboard.domain.Answer}.
 */
@RestController
@RequestMapping("/api")
public class AnswerResource {

    private final Logger log = LoggerFactory.getLogger(AnswerResource.class);

    private static final String ENTITY_NAME = "answer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;

    private final AnswerService answerService;

    private final AnswerRepository answerRepository;

    public AnswerResource(UserService userService, AnswerService answerService, AnswerRepository answerRepository) {
        this.userService = userService;
        this.answerService = answerService;
        this.answerRepository = answerRepository;
    }

    /**
     * {@code POST  /answers} : Create a new answer.
     *
     * @param answerDTO the answerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new answerDTO, or with status {@code 400 (Bad Request)} if the answer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/answers")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<AnswerDTO> createAnswer(@Valid @RequestBody AnswerDTO answerDTO) throws URISyntaxException {
        log.debug("REST request to save Answer : {}", answerDTO);
        if (answerDTO.getId() != null) {
            throw new BadRequestAlertException("A new answer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnswerDTO result = answerService.save(answerDTO);
        return ResponseEntity
            .created(new URI("/api/answers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /answers/:id} : Updates an existing answer.
     *
     * @param id the id of the answerDTO to save.
     * @param answerDTO the answerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated answerDTO,
     * or with status {@code 400 (Bad Request)} if the answerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the answerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/answers/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<AnswerDTO> updateAnswer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AnswerDTO answerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Answer : {}, {}", id, answerDTO);
        if (answerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, answerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!answerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AnswerDTO result = answerService.save(answerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, answerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /answers/:id} : Partial updates given fields of an existing answer, field will ignore if it is null
     *
     * @param id the id of the answerDTO to save.
     * @param answerDTO the answerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated answerDTO,
     * or with status {@code 400 (Bad Request)} if the answerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the answerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the answerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/answers/{id}", consumes = "application/merge-patch+json")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<AnswerDTO> partialUpdateAnswer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AnswerDTO answerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Answer partially : {}, {}", id, answerDTO);
        if (answerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, answerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!answerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AnswerDTO> result = answerService.partialUpdate(answerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, answerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /answers} : get all the answers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of answers in body.
     */
    @GetMapping("/answers")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public List<AnswerDTO> getAllAnswers() {
        log.debug("REST request to get all Answers");
        return answerService.findAll();
    }

    /**
     * {@code GET  /answers/:id} : get the "id" answer.
     *
     * @param id the id of the answerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the answerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/answers/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<AnswerDTO> getAnswer(@PathVariable Long id) {
        log.debug("REST request to get Answer : {}", id);
        Optional<AnswerDTO> answerDTO = answerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(answerDTO);
    }

    /**
     * {@code DELETE  /answers/:id} : delete the "id" answer.
     *
     * @param id the id of the answerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/answers/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long id) {
        log.debug("REST request to delete Answer : {}", id);
        answerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/account/answers")
    public List<AnswerDTO> getAllAccountAnswers() {
        log.debug("REST request to get all Answers for account");
        User user = userService.getUserWithAuthorities().orElseThrow(() -> new RuntimeException("User could not be found"));
        return answerService.findAllForAccount(user);
    }

    @PostMapping("/account/answers")
    public ResponseEntity<AnswerDTO> submitAnswer(@Valid @RequestBody AnswerSubmitDTO answerSubmitDTO) {
        log.debug("REST request to submit Answer for account");
        User user = userService.getUserWithAuthorities().orElseThrow(() -> new RuntimeException("User could not be found"));
        Optional<AnswerDTO> answerDTO = answerService.submitAnswer(user, answerSubmitDTO);
        return ResponseUtil.wrapOrNotFound(answerDTO);
    }
}
