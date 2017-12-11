package ru.nsu.gulteam.prof_standards.backend.service;

import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Course;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.CourseRepository;
import ru.nsu.gulteam.prof_standards.backend.entity.FullCourseInfo;
import ru.nsu.gulteam.prof_standards.backend.exception.IncorrectIdentifierException;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.CourseMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.CourseDto;

import java.util.Collections;

@Service
public class CourseService {
    private CourseRepository courseRepository;
    private CourseMapper courseMapper;

    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    public FullCourseInfo getFullCourseInfo(Course course) {
        return new FullCourseInfo(course,
                Collections.emptyList(),
                Collections.emptyList(),
                false,
                0,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList());
    }

    public Course getCourse(long courseId) throws IncorrectIdentifierException {
        Course course = courseRepository.findOne(courseId);
        if (course == null) {
            throw new IncorrectIdentifierException("There is no course with id: " + courseId);
        }
        return course;
    }

    public void deleteCourse(long courseId) {
        Course course = courseRepository.findOne(courseId);
        if (course == null) {
            throw new IncorrectIdentifierException("There is no course with id: " + courseId);
        }
        courseRepository.delete(course);
    }

    public FullCourseInfo updateCourse(int courseId, CourseDto courseDto) {
        Course course = courseMapper.fromDto(courseDto);
        Course savedCourse = courseRepository.save(course, courseId);
        return getFullCourseInfo(savedCourse);
    }
}
