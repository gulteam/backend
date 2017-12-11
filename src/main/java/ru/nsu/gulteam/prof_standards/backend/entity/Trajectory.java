package ru.nsu.gulteam.prof_standards.backend.entity;

import ru.nsu.gulteam.prof_standards.backend.domain.node.Course;

import java.util.List;

public class Trajectory {
    private List<Course> courses;

    public Trajectory(List<Course> courses) {
        this.courses = courses;
    }

    public List<Course> getCourses() {
        return courses;
    }
}
