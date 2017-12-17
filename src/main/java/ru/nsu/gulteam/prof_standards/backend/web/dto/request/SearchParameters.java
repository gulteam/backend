package ru.nsu.gulteam.prof_standards.backend.web.dto.request;

import ru.nsu.gulteam.prof_standards.backend.web.dto.response.BasicEducationProgramDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.CourseDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.ProfessionalStandardDto;

import java.util.List;

public class SearchParameters {
    private BasicEducationProgramDto basicEducationProgram;
    private List<CourseDto> includeCourses;
    private List<CourseDto> excludeCourses;
    private List<ProfessionalStandardDto> includeStandards;
    private List<ProfessionalStandardDto> excludeStandards;

    public SearchParameters() {
    }

    public SearchParameters(BasicEducationProgramDto basicEducationProgram, List<CourseDto> includeCourses, List<CourseDto> excludeCourses, List<ProfessionalStandardDto> includeStandards, List<ProfessionalStandardDto> excludeStandards) {
        this.basicEducationProgram = basicEducationProgram;
        this.includeCourses = includeCourses;
        this.excludeCourses = excludeCourses;
        this.includeStandards = includeStandards;
        this.excludeStandards = excludeStandards;
    }

    public BasicEducationProgramDto getBasicEducationProgram() {
        return basicEducationProgram;
    }

    public void setBasicEducationProgram(BasicEducationProgramDto basicEducationProgram) {
        this.basicEducationProgram = basicEducationProgram;
    }

    public List<CourseDto> getIncludeCourses() {
        return includeCourses;
    }

    public void setIncludeCourses(List<CourseDto> includeCourses) {
        this.includeCourses = includeCourses;
    }

    public List<CourseDto> getExcludeCourses() {
        return excludeCourses;
    }

    public void setExcludeCourses(List<CourseDto> excludeCourses) {
        this.excludeCourses = excludeCourses;
    }

    public List<ProfessionalStandardDto> getIncludeStandards() {
        return includeStandards;
    }

    public void setIncludeStandards(List<ProfessionalStandardDto> includeStandards) {
        this.includeStandards = includeStandards;
    }

    public List<ProfessionalStandardDto> getExcludeStandards() {
        return excludeStandards;
    }

    public void setExcludeStandards(List<ProfessionalStandardDto> excludeStandards) {
        this.excludeStandards = excludeStandards;
    }
}
