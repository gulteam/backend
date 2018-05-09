package ru.nsu.gulteam.prof_standards.backend.web.dto.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Fgos;
import ru.nsu.gulteam.prof_standards.backend.entity.FullFgosInfo;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.FgosDto;

@Mapper(componentModel = "spring",
        uses = {CompetenceMapper.class,
                FgosCourseRequirementMapper.class,
                ProfessionalStandardMapper.class,
                UserMapper.class})
public interface FgosMapper {
    @Mappings({
            @Mapping(source = "info.fgos.id", target = "id"),
            @Mapping(source = "info.fgos.code", target = "code"),
            @Mapping(source = "info.fgos.name", target = "name"),
            @Mapping(source = "info.fgos.requireCompetence", target = "requireCompetence"),
            @Mapping(source = "info.fgos.disciplineVolumeFrom", target = "disciplineVolumeFrom"),
            @Mapping(source = "info.fgos.practiceVolumeFrom", target = "practiceVolumeFrom"),
            @Mapping(source = "info.fgos.attestationVolumeFrom", target = "attestationVolumeFrom"),
            @Mapping(source = "info.fgos.summaryVolume", target = "summaryVolume"),
            @Mapping(source = "info.fgos.requireCourses", target = "requireCourses"),
            @Mapping(source = "info.fgos.professionalStandards", target = "professionalStandards"),
            @Mapping(source = "info.canUpdate", target = "canUpdate"),
            @Mapping(source = "info.canDelete", target = "canDelete"),
            @Mapping(source = "info.fullCreator", target = "createdBy"),
    })
    FgosDto toDto(FullFgosInfo info);

    Fgos fromDto(FgosDto fgosDto);
}
