package ru.nsu.gulteam.prof_standards.backend.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Course;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Department;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Faculty;

import java.util.List;

@Data
@NoArgsConstructor
public class FullCourseInfo {
    private Course course;
    private List<Long> previousCourses;
    private List<Long> nextCourses;

    private boolean implementsTemplate;
    private long templateCourse;

    private List<Integer> developCompetence;
    private List<Long> developSkills;
    private List<Long> developKnowledge;
    private List<Long> developedBy;
    private long programId;

    private boolean canUpdate;
    private boolean canDelete;
    private boolean canUpdateDevelopersList;
    private FullUserInfo createdBy;

    private Department department;
    private Faculty faculty;

    public FullCourseInfo(Course course) {
        this.course = course;
    }
}
