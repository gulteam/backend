package ru.nsu.gulteam.prof_standards.backend.entity;

import lombok.Data;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Knowledge;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Skills;

import java.util.List;

@Data
public class ProfSearchRequest {
    private List<Skills> includeSkills;
    private List<Knowledge> includeKnowledges;
}
