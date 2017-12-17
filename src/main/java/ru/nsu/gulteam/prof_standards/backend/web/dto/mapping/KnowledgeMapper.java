package ru.nsu.gulteam.prof_standards.backend.web.dto.mapping;

import org.mapstruct.Mapper;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Knowledge;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.KnowledgeDto;

@Mapper(componentModel = "spring")
public interface KnowledgeMapper {
    KnowledgeDto toDto(Knowledge knowledge);
    Knowledge fromDto(KnowledgeDto knowledgeDto);
}
