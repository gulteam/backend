package ru.nsu.gulteam.prof_standards.backend.web.dto.mapping;

import org.mapstruct.Mapper;
import ru.nsu.gulteam.prof_standards.backend.domain.node.BasicEducationProgram;
import ru.nsu.gulteam.prof_standards.backend.domain.node.ProfessionalStandard;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.BasicEducationProgramDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.ProfessionalStandardDto;

@Mapper(componentModel = "spring")
public interface ProfessionalStandardMapper {
    ProfessionalStandardDto toDto(ProfessionalStandard professionalStandard);
    ProfessionalStandard fromDto(ProfessionalStandardDto professionalStandardDto);
}
