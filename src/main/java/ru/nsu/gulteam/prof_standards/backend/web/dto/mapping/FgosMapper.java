package ru.nsu.gulteam.prof_standards.backend.web.dto.mapping;

import org.mapstruct.Mapper;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Fgos;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.FgosDto;

@Mapper(componentModel = "spring",
        uses = {CompetenceMapper.class,
                FgosCourseRequirementMapper.class,
                ProfessionalStandardMapper.class})
public interface FgosMapper {
    FgosDto toDto(Fgos role);

    Fgos fromDto(FgosDto roleDto);
}
