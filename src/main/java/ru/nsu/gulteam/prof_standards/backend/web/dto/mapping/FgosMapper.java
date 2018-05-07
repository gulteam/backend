package ru.nsu.gulteam.prof_standards.backend.web.dto.mapping;

import org.mapstruct.Mapper;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Fgos;
import ru.nsu.gulteam.prof_standards.backend.domain.node.FgosCourseRequirement;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Role;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.FgosDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.RoleDto;

@Mapper(componentModel = "spring",
        uses = {CompetenceMapper.class,
                FgosCourseRequirementMapper.class})
public interface FgosMapper {
    FgosDto toDto(Fgos role);

    Fgos fromDto(FgosDto roleDto);
}
