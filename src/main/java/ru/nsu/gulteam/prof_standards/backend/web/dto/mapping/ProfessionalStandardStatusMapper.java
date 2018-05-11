package ru.nsu.gulteam.prof_standards.backend.web.dto.mapping;

import org.mapstruct.Mapper;
import ru.nsu.gulteam.prof_standards.backend.entity.ProfessionalStandardStatus;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.ProfessionalStandardStatusDto;

@Mapper(componentModel = "spring")
public interface ProfessionalStandardStatusMapper {
    ProfessionalStandardStatusDto toDto(ProfessionalStandardStatus professionalStandardStatus);
}
