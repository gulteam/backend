package ru.nsu.gulteam.prof_standards.backend.service;

import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Course;
import ru.nsu.gulteam.prof_standards.backend.entity.FullCourseInfo;

import java.util.Collections;

@Service
public class CourseService {
    public FullCourseInfo getFullCourseInfo(Course course){
        return new FullCourseInfo(course,
                Collections.emptyList(),
                Collections.emptyList(),
                false,
                0,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList());
    }
}
