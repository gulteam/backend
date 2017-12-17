package ru.nsu.gulteam.prof_standards.backend.web.dto.response;

import ru.nsu.gulteam.prof_standards.backend.domain.node.Course;

import java.util.List;

public class TrajectoryDto {
    private List<CourseDto> courses;

    public TrajectoryDto() {
    }

    public List<CourseDto> getCourses() {

        return courses;
    }

    public void setCourses(List<CourseDto> courses) {
        this.courses = courses;
    }
}
