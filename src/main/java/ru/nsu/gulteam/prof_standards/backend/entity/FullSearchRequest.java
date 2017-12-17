package ru.nsu.gulteam.prof_standards.backend.entity;

import ru.nsu.gulteam.prof_standards.backend.domain.node.BasicEducationProgram;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Course;
import ru.nsu.gulteam.prof_standards.backend.domain.node.ProfessionalStandard;

import java.util.List;

public class FullSearchRequest {
    private BasicEducationProgram basicEducationProgram;
    private List<Course> includeCourses;
    private List<Course> excludeCourses;
    private List<ProfessionalStandard> includeStandards;
    private List<ProfessionalStandard> excludeStandards;

    public FullSearchRequest(BasicEducationProgram basicEducationProgram, List<Course> includeCourses, List<Course> excludeCourses, List<ProfessionalStandard> includeStandards, List<ProfessionalStandard> excludeStandards) {
        this.basicEducationProgram = basicEducationProgram;
        this.includeCourses = includeCourses;
        this.excludeCourses = excludeCourses;
        this.includeStandards = includeStandards;
        this.excludeStandards = excludeStandards;
    }

    public FullSearchRequest() {
    }

    public BasicEducationProgram getBasicEducationProgram() {
        return basicEducationProgram;
    }

    public void setBasicEducationProgram(BasicEducationProgram basicEducationProgram) {
        this.basicEducationProgram = basicEducationProgram;
    }

    public List<Course> getIncludeCourses() {
        return includeCourses;
    }

    public void setIncludeCourses(List<Course> includeCourses) {
        this.includeCourses = includeCourses;
    }

    public List<Course> getExcludeCourses() {
        return excludeCourses;
    }

    public void setExcludeCourses(List<Course> excludeCourses) {
        this.excludeCourses = excludeCourses;
    }

    public List<ProfessionalStandard> getIncludeStandards() {
        return includeStandards;
    }

    public void setIncludeStandards(List<ProfessionalStandard> includeStandards) {
        this.includeStandards = includeStandards;
    }

    public List<ProfessionalStandard> getExcludeStandards() {
        return excludeStandards;
    }

    public void setExcludeStandards(List<ProfessionalStandard> excludeStandards) {
        this.excludeStandards = excludeStandards;
    }
}
