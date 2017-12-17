package ru.nsu.gulteam.prof_standards.backend.entity;

import ru.nsu.gulteam.prof_standards.backend.domain.node.Course;

import java.util.List;

public class FullCourseInfo {
    private Course course;
    private List<Long> previousCourses;
    private List<Long> nextCourses;

    private boolean implementsTemplate;
    private long templateCourse;

    private List<Integer> developCompetence;
    private List<Long> developSkills;
    private List<Long> developKnowledge;
    private long programId;

    public long getProgramId() {
        return programId;
    }

    public void setProgramId(long programId) {
        this.programId = programId;
    }

    public FullCourseInfo(Course course) {
        this.course = course;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Long> getPreviousCourses() {
        return previousCourses;
    }

    public void setPreviousCourses(List<Long> previousCourses) {
        this.previousCourses = previousCourses;
    }

    public List<Long> getNextCourses() {
        return nextCourses;
    }

    public void setNextCourses(List<Long> nextCourses) {
        this.nextCourses = nextCourses;
    }

    public boolean isImplementsTemplate() {
        return implementsTemplate;
    }

    public void setImplementsTemplate(boolean implementsTemplate) {
        this.implementsTemplate = implementsTemplate;
    }

    public long getTemplateCourse() {
        return templateCourse;
    }

    public void setTemplateCourse(long templateCourse) {
        this.templateCourse = templateCourse;
    }

    public List<Integer> getDevelopCompetence() {
        return developCompetence;
    }

    public void setDevelopCompetence(List<Integer> developCompetence) {
        this.developCompetence = developCompetence;
    }

    public List<Long> getDevelopSkills() {
        return developSkills;
    }

    public void setDevelopSkills(List<Long> developSkills) {
        this.developSkills = developSkills;
    }

    public List<Long> getDevelopKnowledge() {
        return developKnowledge;
    }

    public void setDevelopKnowledge(List<Long> developKnowledge) {
        this.developKnowledge = developKnowledge;
    }
}
