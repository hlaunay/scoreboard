package fr.sii.scoreboard.service.mapper;

import fr.sii.scoreboard.domain.*;
import fr.sii.scoreboard.service.dto.TeamDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Team} and its DTO {@link TeamDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TeamMapper extends EntityMapper<TeamDTO, Team> {}
