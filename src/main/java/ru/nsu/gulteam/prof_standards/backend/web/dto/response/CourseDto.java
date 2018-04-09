package ru.nsu.gulteam.prof_standards.backend.web.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nsu.gulteam.prof_standards.backend.domain.type.AttestationForm;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
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
    @NotNull
    private UserDto createdBy;
}
