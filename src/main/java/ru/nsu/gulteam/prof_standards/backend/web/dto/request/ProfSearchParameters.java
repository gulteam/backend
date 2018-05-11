package ru.nsu.gulteam.prof_standards.backend.web.dto.request;

import lombok.Data;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.KnowledgeDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.SkillsDto;

import java.util.List;

@Data
public class ProfSearchParameters {
    private List<SkillsDto> includeSkills;
    private List<KnowledgeDto> includeKnowledges;
}
