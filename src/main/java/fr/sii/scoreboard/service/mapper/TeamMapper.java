package fr.sii.scoreboard.service.mapper;

import fr.sii.scoreboard.domain.Team;
import fr.sii.scoreboard.service.dto.TeamDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Team} and its DTO {@link TeamDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TeamMapper extends EntityMapper<TeamDTO, Team> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    TeamDTO toDtoName(Team team);
}
