package ru.nsu.gulteam.prof_standards.backend.web.dto.response;

import ru.nsu.gulteam.prof_standards.backend.domain.type.AttestationForm;

import javax.validation.constraints.NotNull;
import java.util.List;

public class CourseDto {
    @NotNull
    private long id;
    @NotNull
    private int amount;
    @NotNull
    private int semester;
    @NotNull
    private AttestationForm attestationForm;
    @NotNull
    private String name;
    @NotNull
    private List<Integer> previousCourses;
    @NotNull
    private List<Integer> nextCourses;
    @NotNull
    private boolean implementsTemplate;
    private int templateCourse;
    @NotNull
    private List<Integer> developCompetence;
    @NotNull
    private List<Integer> developSkills;
    @NotNull
    private List<Integer> developKnowledge;
    @NotNull
    private int programId;

    public CourseDto(){}

    public CourseDto(long id, int amount, int semester, AttestationForm attestationForm, String name, List<Integer> previousCourses, List<Integer> nextCourses, boolean implementsTemplate, int templateCourse, List<Integer> developCompetence, List<Integer> developSkills, List<Integer> developKnowledge, int programId) {
        this.id = id;
        this.amount = amount;
        this.semester = semester;
        this.attestationForm = attestationForm;
        this.name = name;
        this.previousCourses = previousCourses;
        this.nextCourses = nextCourses;
        this.implementsTemplate = implementsTemplate;
        this.templateCourse = templateCourse;
        this.developCompetence = developCompetence;
        this.developSkills = developSkills;
        this.developKnowledge = developKnowledge;
        this.programId = programId;
    }

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public AttestationForm getAttestationForm() {
        return attestationForm;
    }

    public void setAttestationForm(AttestationForm attestationForm) {
        this.attestationForm = attestationForm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isImplementsTemplate() {
        return implementsTemplate;
    }

    public void setImplementsTemplate(boolean implementsTemplate) {
        this.implementsTemplate = implementsTemplate;
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
