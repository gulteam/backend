package ru.nsu.gulteam.prof_standards.backend.web.dto.mapping;

import org.mapstruct.Mapper;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Competence;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Role;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.CompetenceDto;

@Mapper(componentModel = "spring")
public interface CompetenceMapper {
    CompetenceDto toDto(Competence role);

    Competence fromDto(CompetenceDto roleDto);
}
