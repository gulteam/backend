package ru.nsu.gulteam.prof_standards.backend.web.dto.mapping;

import org.mapstruct.Mapper;
import ru.nsu.gulteam.prof_standards.backend.entity.ProfSearchRequest;
import ru.nsu.gulteam.prof_standards.backend.web.dto.request.ProfSearchParameters;

@Mapper(componentModel = "spring",
        uses = {SkillsMapper.class,
                KnowledgeMapper.class})
public interface ProfSearchParametersMapper {
    ProfSearchRequest fromDto(ProfSearchParameters profSearchParameters);
}
