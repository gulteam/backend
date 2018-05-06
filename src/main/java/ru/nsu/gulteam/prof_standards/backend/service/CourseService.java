package ru.nsu.gulteam.prof_standards.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.*;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.*;
import ru.nsu.gulteam.prof_standards.backend.domain.type.UserRole;
import ru.nsu.gulteam.prof_standards.backend.entity.FullCourseInfo;
import ru.nsu.gulteam.prof_standards.backend.entity.FullUserInfo;
import ru.nsu.gulteam.prof_standards.backend.exception.IncorrectIdentifierException;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.CourseMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.CourseDto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {
    private CourseRepository courseRepository;
    private CourseMapper courseMapper;
    private KnowledgeRepository knowledgeRepository;
    private SkillsRepository skillsRepository;
    private TemplateCourseRepository templateCourseRepository;
    private BasicEducationProgramRepository programRepository;
    private UserService userService;

    @Autowired
    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper, KnowledgeRepository knowledgeRepository, SkillsRepository skillsRepository, TemplateCourseRepository templateCourseRepository, BasicEducationProgramRepository programRepository, UserService userService) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.knowledgeRepository = knowledgeRepository;
        this.skillsRepository = skillsRepository;
        this.templateCourseRepository = templateCourseRepository;
        this.programRepository = programRepository;
        this.userService = userService;
    }

    public FullCourseInfo getFullCourseInfo(User user, Course course) {
        FullCourseInfo fullCourseInfo = new FullCourseInfo(course);

        fullCourseInfo.setDevelopKnowledge(knowledgeRepository.getDevelopsByCourse(course).stream().map(Knowledge::getId).collect(Collectors.toList()));
        if(fullCourseInfo.getDevelopKnowledge() == null){
            fullCourseInfo.setDevelopKnowledge(Collections.emptyList());
        }

        fullCourseInfo.setDevelopSkills(skillsRepository.getDevelopsByCourse(course).stream().map(Skills::getId).collect(Collectors.toList()));
        if(fullCourseInfo.getDevelopSkills() == null){
            fullCourseInfo.setDevelopSkills(Collections.emptyList());
        }

        TemplateCourse templateCourse = templateCourseRepository.findTemplateOf(course);
        if(templateCourse == null){
            fullCourseInfo.setImplementsTemplate(false);
        }
        else {
            fullCourseInfo.setImplementsTemplate(true);
            fullCourseInfo.setTemplateCourse(templateCourse.getId());
        }

        fullCourseInfo.setProgramId(programRepository.getProgramOf(course).getId());

        fullCourseInfo.setPreviousCourses(courseRepository.getPreviousCourses(course).stream().map(Course::getId).collect(Collectors.toList()));
        if(fullCourseInfo.getPreviousCourses() == null){
            fullCourseInfo.setPreviousCourses(Collections.emptyList());
        }

        fullCourseInfo.setNextCourses(courseRepository.getNextCourses(course).stream().map(Course::getId).collect(Collectors.toList()));
        if(fullCourseInfo.getNextCourses() == null){
            fullCourseInfo.setNextCourses(Collections.emptyList());
        }

        fullCourseInfo.setCreatedBy(userService.getFullUserInfo(courseRepository.getCreator(course)));

        fullCourseInfo.setCanEdit(canEditCourse(user, course));

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

    public FullCourseInfo updateCourse(User user, int courseId, CourseDto courseDto) {
        List<Skills> developSkills = courseDto.getDevelopSkills().stream().map(id -> {
            Skills skills = skillsRepository.findOne((long)id);

            if(skills == null){
                throw new IncorrectIdentifierException("There is no skills with id: " + id);
            }

            return skills;
        }).collect(Collectors.toList());


        List<Knowledge> developKnowledges = courseDto.getDevelopKnowledge().stream().map(id -> {
            Knowledge knowledge = knowledgeRepository.findOne((long)id);

            if(knowledge == null){
                throw new IncorrectIdentifierException("There is no knowledge with id: " + id);
            }

            return knowledge;
        }).collect(Collectors.toList());

        List<Course> previousCourses = courseDto.getPreviousCourses().stream().map(id -> {
            Course course = courseRepository.findOne((long)id);

            if(course == null){
                throw new IncorrectIdentifierException("There is no course with id: " + id);
            }

            return course;
        }).collect(Collectors.toList());

        Course course = courseMapper.fromDto(courseDto);
        Course savedCourse = courseRepository.save(course, courseId);

        courseRepository.deleteAllDevelopRelations(savedCourse);
        developSkills.forEach(skills -> skillsRepository.connectToCourse(skills, savedCourse));
        developKnowledges.forEach(knowledge -> knowledgeRepository.connectToCourse(knowledge, savedCourse));

        courseRepository.removeAllBased(savedCourse);
        previousCourses.forEach(base -> courseRepository.setBasedOn(savedCourse, base));

        return getFullCourseInfo(user, savedCourse);
    }

    public boolean canReadCourse(User user, Course course) {
        return true;
    }

    public boolean canEditCourse(User user, Course course) {
        if(user == null){
            return false;
        }

        FullUserInfo fullUserInfo = userService.getFullUserInfo(user);

        UserRole role = fullUserInfo.getRole().getName();
        Faculty courseFaculty = programRepository.getFacultyOf(programRepository.getProgramOf(course));

        return role.equals(UserRole.ADMINISTRATOR) || role.equals(UserRole.DEAN_MEMBER) && fullUserInfo.getFaculty().equals(courseFaculty);
    }

    public FullCourseInfo getFullCourseInfo(Course course) {
        return getFullCourseInfo(null, course);
    }

    public Course setRating(long courseID, double rating)
    {
        Course course = courseRepository.findOne(courseID);
        course.setRating(rating);
        return courseRepository.save(course);
    }
}
