package ru.nsu.gulteam.prof_standards.backend.web.dto.mapping;

import org.mapstruct.Mapper;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Knowledge;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Skills;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.KnowledgeDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.SkillsDto;

@Mapper(componentModel = "spring")
public interface SkillsMapper {
    SkillsDto toDto(Skills skills);
    Skills fromDto(SkillsDto skillsDto);
}
