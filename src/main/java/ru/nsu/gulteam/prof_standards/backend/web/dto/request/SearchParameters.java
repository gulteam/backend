package ru.nsu.gulteam.prof_standards.backend.web.dto.request;

import lombok.Data;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.BasicEducationProgramDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.CourseDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.ProfessionalStandardDto;

import java.util.List;

@Data
public class SearchParameters {
    private BasicEducationProgramDto basicEducationProgram;
    private List<CourseDto> includeCourses;
    private List<CourseDto> excludeCourses;
    private List<ProfessionalStandardDto> includeStandards;
    private List<ProfessionalStandardDto> excludeStandards;
}
