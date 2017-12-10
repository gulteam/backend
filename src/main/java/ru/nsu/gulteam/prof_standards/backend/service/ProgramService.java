package ru.nsu.gulteam.prof_standards.backend.service;

import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Course;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.BasicEducationProgramRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.CourseRepository;
import ru.nsu.gulteam.prof_standards.backend.entity.FullCourseInfo;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgramService {
    private BasicEducationProgramRepository programRepository;
    private CourseRepository courseRepository;
    private CourseService courseService;

    public ProgramService(BasicEducationProgramRepository programRepository, CourseRepository courseRepository, CourseService courseService) {
        this.programRepository = programRepository;
        this.courseRepository = courseRepository;
        this.courseService = courseService;
    }

    public FullCourseInfo addCourseTo(int programId) {
        Course course = courseRepository.save(new Course());

        // Todo: connect course to program

        return courseService.getFullCourseInfo(course);
    }

    public List<FullCourseInfo> getAllCourses(int programId) {
        List<Course> courses = courseRepository.findAll();

        // Todo: get only courses from one program

        return courses.stream().map(courseService::getFullCourseInfo).collect(Collectors.toList());
    }
}
