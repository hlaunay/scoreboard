package fr.sii.scoreboard.service;

import fr.sii.scoreboard.domain.Challenge;
import fr.sii.scoreboard.domain.Challenge_;
import fr.sii.scoreboard.repository.ChallengeRepository;
import fr.sii.scoreboard.service.criteria.ChallengeCriteria;
import fr.sii.scoreboard.service.dto.ChallengeDTO;
import fr.sii.scoreboard.service.mapper.ChallengeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

import java.util.List;

/**
 * Service for executing complex queries for {@link Challenge} entities in the database.
 * The main input is a {@link ChallengeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ChallengeDTO} or a {@link Page} of {@link ChallengeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChallengeQueryService extends QueryService<Challenge> {

    private final Logger log = LoggerFactory.getLogger(ChallengeQueryService.class);

    private final ChallengeRepository challengeRepository;

    private final ChallengeMapper challengeMapper;

    public ChallengeQueryService(ChallengeRepository challengeRepository, ChallengeMapper challengeMapper) {
        this.challengeRepository = challengeRepository;
        this.challengeMapper = challengeMapper;
    }

    /**
     * Return a {@link List} of {@link ChallengeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ChallengeDTO> findByCriteria(ChallengeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Challenge> specification = createSpecification(criteria);
        return challengeMapper.toDto(challengeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ChallengeDTO} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ChallengeDTO> findByCriteria(ChallengeCriteria criteria, Pageable page, Boolean isAdmin) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Challenge> specification = createSpecification(criteria);
        if (isAdmin) {
            return challengeRepository.findAll(specification, page).map(challengeMapper::toDto);
        }

        return challengeRepository.findAll(specification, page).map(challengeMapper::toGuardedDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ChallengeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Challenge> specification = createSpecification(criteria);
        return challengeRepository.count(specification);
    }

    /**
     * Function to convert {@link ChallengeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Challenge> createSpecification(ChallengeCriteria criteria) {
        Specification<Challenge> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Challenge_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Challenge_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Challenge_.description));
            }
            if (criteria.getPoints() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPoints(), Challenge_.points));
            }
            if (criteria.getAnswer() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAnswer(), Challenge_.answer));
            }
        }
        return specification;
    }
}
