package ru.nsu.gulteam.prof_standards.backend.entity;

import ru.nsu.gulteam.prof_standards.backend.domain.node.Course;

import java.util.List;

public class FullCourseInfo {
    private Course course;
    private List<Integer> previousCourses;
    private List<Integer> nextCourses;

    private boolean isImplements;
    private int templateCourse;

    private List<Integer> developCompetence;
    private List<Integer> developSkills;
    private List<Integer> developKnowledge;

    public FullCourseInfo(Course course, List<Integer> previousCourses, List<Integer> nextCourses, boolean isImplements, int templateCourse, List<Integer> developCompetence, List<Integer> developSkills, List<Integer> developKnowledge) {
        this.course = course;
        this.previousCourses = previousCourses;
        this.nextCourses = nextCourses;
        this.isImplements = isImplements;
        this.templateCourse = templateCourse;
        this.developCompetence = developCompetence;
        this.developSkills = developSkills;
        this.developKnowledge = developKnowledge;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Integer> getPreviousCourses() {
        return previousCourses;
    }

    public void setPreviousCourses(List<Integer> previousCourses) {
        this.previousCourses = previousCourses;
    }

    public List<Integer> getNextCourses() {
        return nextCourses;
    }

    public void setNextCourses(List<Integer> nextCourses) {
        this.nextCourses = nextCourses;
    }

    public boolean isImplements() {
        return isImplements;
    }

    public void setImplements(boolean anImplements) {
        isImplements = anImplements;
    }

    public int getTemplateCourse() {
        return templateCourse;
    }

    public void setTemplateCourse(int templateCourse) {
        this.templateCourse = templateCourse;
    }

    public List<Integer> getDevelopCompetence() {
        return developCompetence;
    }

    public void setDevelopCompetence(List<Integer> developCompetence) {
        this.developCompetence = developCompetence;
    }

    public List<Integer> getDevelopSkills() {
        return developSkills;
    }

    public void setDevelopSkills(List<Integer> developSkills) {
        this.developSkills = developSkills;
    }

    public List<Integer> getDevelopKnowledge() {
        return developKnowledge;
    }

    public void setDevelopKnowledge(List<Integer> developKnowledge) {
        this.developKnowledge = developKnowledge;
    }
}
