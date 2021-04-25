package fr.sii.scoreboard.service.mapper;

import fr.sii.scoreboard.domain.Challenge;
import fr.sii.scoreboard.service.dto.ChallengeDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Challenge} and its DTO {@link ChallengeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ChallengeMapper extends EntityMapper<ChallengeDTO, Challenge> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ChallengeDTO toDtoName(Challenge challenge);

    @Named("guarded")
    @Mapping(target = "answer", ignore = true)
    ChallengeDTO toGuardedDto(Challenge challenge);
}
