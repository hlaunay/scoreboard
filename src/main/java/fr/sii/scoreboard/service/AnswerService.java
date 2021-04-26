package fr.sii.scoreboard.service;

import fr.sii.scoreboard.domain.Answer;
import fr.sii.scoreboard.domain.Score;
import fr.sii.scoreboard.domain.User;
import fr.sii.scoreboard.repository.AnswerRepository;
import fr.sii.scoreboard.repository.ChallengeRepository;
import fr.sii.scoreboard.service.dto.AnswerDTO;
import fr.sii.scoreboard.service.dto.AnswerSubmitDTO;
import fr.sii.scoreboard.service.dto.ScoreDTO;
import fr.sii.scoreboard.service.mapper.AnswerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Answer}.
 */
@Service
@Transactional
public class AnswerService {

    private final Logger log = LoggerFactory.getLogger(AnswerService.class);

    private final AnswerRepository answerRepository;

    private final ChallengeRepository challengeRepository;

    private final AnswerMapper answerMapper;

    public AnswerService(AnswerRepository answerRepository, ChallengeRepository challengeRepository, AnswerMapper answerMapper) {
        this.answerRepository = answerRepository;
        this.challengeRepository = challengeRepository;
        this.answerMapper = answerMapper;
    }

    /**
     * Save a answer.
     *
     * @param answerDTO the entity to save.
     * @return the persisted entity.
     */
    public AnswerDTO save(AnswerDTO answerDTO) {
        log.debug("Request to save Answer : {}", answerDTO);
        Answer answer = answerMapper.toEntity(answerDTO);
        answer = answerRepository.save(answer);
        return answerMapper.toDto(answer);
    }

    /**
     * Partially update a answer.
     *
     * @param answerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AnswerDTO> partialUpdate(AnswerDTO answerDTO) {
        log.debug("Request to partially update Answer : {}", answerDTO);

        return answerRepository
            .findById(answerDTO.getId())
            .map(
                existingAnswer -> {
                    answerMapper.partialUpdate(existingAnswer, answerDTO);
                    return existingAnswer;
                }
            )
            .map(answerRepository::save)
            .map(answerMapper::toDto);
    }

    /**
     * Get all the answers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AnswerDTO> findAll() {
        log.debug("Request to get all Answers");
        return answerRepository.findAll().stream().map(answerMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one answer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AnswerDTO> findOne(Long id) {
        log.debug("Request to get Answer : {}", id);
        return answerRepository.findById(id).map(answerMapper::toDto);
    }

    /**
     * Delete the answer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Answer : {}", id);
        answerRepository.deleteById(id);
    }

    public List<AnswerDTO> findAllForAccount(User user) {
        log.debug("Request to get all Answers for account : {}", user);
        return answerRepository.findAllByTeam(user.getTeam()).stream().map(answerMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional
    public Optional<AnswerDTO> submitAnswer(User user, AnswerSubmitDTO answerSubmitDTO) {
        log.debug("Request to submit Answers for challenge {} with account : {}", answerSubmitDTO.getChallengeId(), user);
        return challengeRepository.findById(answerSubmitDTO.getChallengeId())
            .filter(challenge -> challenge.getAnswer().equals(answerSubmitDTO.getAnswer()))
            .map(challenge -> {
                Answer answer = new Answer();
                answer.setDate(Instant.now());
                answer.setChallenge(challenge);
                answer.setTeam(user.getTeam());

                answerRepository.saveAndFlush(answer);

                return answerMapper.toDto(answer);
            });

    }

    public List<ScoreDTO> findScores() {
        log.debug("Request to get all Scores");
        List<Score> scores = answerRepository.findScores();
        List<ScoreDTO> results = new ArrayList<>();

        for (int i = 0; i < scores.size(); i++) {
            Score score = scores.get(i);
            ScoreDTO dto = new ScoreDTO();
            dto.setPosition(i + 1);
            dto.setTeam(score.getTeam());
            dto.setPoints(score.getPoints());
            dto.setFirstAnswer(score.getFirst());

            results.add(dto);
        }

        return results;
    }
}
