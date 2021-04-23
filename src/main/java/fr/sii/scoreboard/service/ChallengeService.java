package fr.sii.scoreboard.service;

import fr.sii.scoreboard.domain.Challenge;
import fr.sii.scoreboard.repository.ChallengeRepository;
import fr.sii.scoreboard.service.dto.ChallengeDTO;
import fr.sii.scoreboard.service.mapper.ChallengeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Challenge}.
 */
@Service
@Transactional
public class ChallengeService {

    private final Logger log = LoggerFactory.getLogger(ChallengeService.class);

    private final ChallengeRepository challengeRepository;

    private final ChallengeMapper challengeMapper;

    public ChallengeService(ChallengeRepository challengeRepository, ChallengeMapper challengeMapper) {
        this.challengeRepository = challengeRepository;
        this.challengeMapper = challengeMapper;
    }

    /**
     * Save a challenge.
     *
     * @param challengeDTO the entity to save.
     * @return the persisted entity.
     */
    public ChallengeDTO save(ChallengeDTO challengeDTO) {
        log.debug("Request to save Challenge : {}", challengeDTO);
        Challenge challenge = challengeMapper.toEntity(challengeDTO);
        challenge = challengeRepository.save(challenge);
        return challengeMapper.toDto(challenge);
    }

    /**
     * Partially update a challenge.
     *
     * @param challengeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ChallengeDTO> partialUpdate(ChallengeDTO challengeDTO) {
        log.debug("Request to partially update Challenge : {}", challengeDTO);

        return challengeRepository
            .findById(challengeDTO.getId())
            .map(
                existingChallenge -> {
                    challengeMapper.partialUpdate(existingChallenge, challengeDTO);
                    return existingChallenge;
                }
            )
            .map(challengeRepository::save)
            .map(challengeMapper::toDto);
    }

    /**
     * Get all the challenges.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ChallengeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Challenges");
        return challengeRepository.findAll(pageable).map(challengeMapper::toDto);
    }

    /**
     * Get one challenge by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ChallengeDTO> findOne(Long id) {
        log.debug("Request to get Challenge : {}", id);
        return challengeRepository.findById(id).map(challengeMapper::toDto);
    }

    /**
     * Delete the challenge by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Challenge : {}", id);
        challengeRepository.deleteById(id);
    }
}
