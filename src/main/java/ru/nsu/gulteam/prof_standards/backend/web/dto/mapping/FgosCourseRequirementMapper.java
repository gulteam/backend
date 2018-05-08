package ru.nsu.gulteam.prof_standards.backend.web.dto.mapping;

import org.mapstruct.Mapper;
import ru.nsu.gulteam.prof_standards.backend.domain.node.FgosCourseRequirement;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Role;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.CompetenceDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.FgosCourseRequirementDto;

@Mapper(componentModel = "spring")
public interface FgosCourseRequirementMapper {
    FgosCourseRequirementDto toDto(FgosCourseRequirement fgosCourseRequirement);

    FgosCourseRequirement fromDto(FgosCourseRequirementDto fgosCourseRequirementDto);
}
