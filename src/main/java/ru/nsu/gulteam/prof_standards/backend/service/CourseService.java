package ru.nsu.gulteam.prof_standards.backend.service;

import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Course;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Knowledge;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Skills;
import ru.nsu.gulteam.prof_standards.backend.domain.node.TemplateCourse;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.CourseRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.KnowledgeRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.SkillsRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.TemplateCourseRepository;
import ru.nsu.gulteam.prof_standards.backend.entity.FullCourseInfo;
import ru.nsu.gulteam.prof_standards.backend.exception.IncorrectIdentifierException;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.CourseMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.CourseDto;

import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class CourseService {
    private CourseRepository courseRepository;
    private CourseMapper courseMapper;
    private KnowledgeRepository knowledgeRepository;
    private SkillsRepository skillsRepository;
    private TemplateCourseRepository templateCourseRepository;

    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper, KnowledgeRepository knowledgeRepository, SkillsRepository skillsRepository, TemplateCourseRepository templateCourseRepository) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.knowledgeRepository = knowledgeRepository;
        this.skillsRepository = skillsRepository;
        this.templateCourseRepository = templateCourseRepository;
    }

    public FullCourseInfo getFullCourseInfo(Course course) {
        FullCourseInfo fullCourseInfo = new FullCourseInfo(course);

        fullCourseInfo.setDevelopKnowledge(knowledgeRepository.getDevelopsByCourse(course).stream().map(Knowledge::getId).collect(Collectors.toList()));
        fullCourseInfo.setDevelopSkills(skillsRepository.getDevelopsByCourse(course).stream().map(Skills::getId).collect(Collectors.toList()));

        TemplateCourse templateCourse = templateCourseRepository.findTemplateOf(course);
        if(templateCourse == null){
            fullCourseInfo.setImplementsTemplate(false);
        }
        else {
            fullCourseInfo.setImplementsTemplate(true);
            fullCourseInfo.setTemplateCourse(templateCourse.getId());
        }

        return fullCourseInfo;
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
