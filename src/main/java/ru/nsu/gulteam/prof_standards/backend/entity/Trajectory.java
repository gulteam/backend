package ru.nsu.gulteam.prof_standards.backend.entity;

import ru.nsu.gulteam.prof_standards.backend.domain.node.Course;

import java.util.List;

public class Trajectory {
    private List<FullCourseInfo> courses;

    public Trajectory(List<FullCourseInfo> courses) {
        this.courses = courses;
    }

    public List<FullCourseInfo> getCourses() {
        return courses;
    }
}
