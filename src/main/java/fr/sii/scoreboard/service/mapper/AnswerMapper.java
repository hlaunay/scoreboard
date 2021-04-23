package fr.sii.scoreboard.service.mapper;

import fr.sii.scoreboard.domain.Answer;
import fr.sii.scoreboard.service.dto.AnswerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Answer} and its DTO {@link AnswerDTO}.
 */
@Mapper(componentModel = "spring", uses = { ChallengeMapper.class, TeamMapper.class })
public interface AnswerMapper extends EntityMapper<AnswerDTO, Answer> {
    @Mapping(target = "challenge", source = "challenge", qualifiedByName = "name")
    @Mapping(target = "team", source = "team", qualifiedByName = "name")
    AnswerDTO toDto(Answer s);
}
