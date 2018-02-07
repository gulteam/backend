package ru.nsu.gulteam.prof_standards.backend.entity;

import lombok.Data;
import ru.nsu.gulteam.prof_standards.backend.domain.node.BasicEducationProgram;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Course;
import ru.nsu.gulteam.prof_standards.backend.domain.node.ProfessionalStandard;

import java.util.List;

@Data
public class FullSearchRequest {
    private BasicEducationProgram basicEducationProgram;
    private List<Course> includeCourses;
    private List<Course> excludeCourses;
    private List<ProfessionalStandard> includeStandards;
    private List<ProfessionalStandard> excludeStandards;
}
