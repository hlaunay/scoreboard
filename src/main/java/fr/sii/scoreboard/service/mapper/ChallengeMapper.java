package fr.sii.scoreboard.service.mapper;

import fr.sii.scoreboard.domain.Challenge;
import fr.sii.scoreboard.service.dto.ChallengeDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Challenge} and its DTO {@link ChallengeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ChallengeMapper extends EntityMapper<ChallengeDTO, Challenge> {
}
