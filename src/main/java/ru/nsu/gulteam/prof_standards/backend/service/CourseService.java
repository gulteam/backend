package ru.nsu.gulteam.prof_standards.backend.service;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final KnowledgeRepository knowledgeRepository;
    private final SkillsRepository skillsRepository;
    private final BlockRepository blockRepository;
    private final BasicEducationProgramRepository programRepository;
    private final UserService userService;
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final CompetenceRepository competenceRepository;

    public FullCourseInfo getFullCourseInfo(User user, Course course) {
        FullCourseInfo fullCourseInfo = new FullCourseInfo(course);

        fullCourseInfo.setDevelopKnowledge(knowledgeRepository.getDevelopsByCourse(course).stream().map(Knowledge::getId).collect(Collectors.toList()));
        if (fullCourseInfo.getDevelopKnowledge() == null) {
            fullCourseInfo.setDevelopKnowledge(Collections.emptyList());
        }

        fullCourseInfo.setDevelopSkills(skillsRepository.getDevelopsByCourse(course).stream().map(Skills::getId).collect(Collectors.toList()));
        if (fullCourseInfo.getDevelopSkills() == null) {
            fullCourseInfo.setDevelopSkills(Collections.emptyList());
        }

        Block block = blockRepository.findBlockOf(course);
        if (block == null) {
            fullCourseInfo.setImplementsTemplate(false);
        } else {
            fullCourseInfo.setImplementsTemplate(true);
            fullCourseInfo.setTemplateCourse(block.getId());
        }

        fullCourseInfo.setProgramId(programRepository.getProgramOf(course).getId());

        fullCourseInfo.setPreviousCourses(courseRepository.getPreviousCourses(course).stream().map(Course::getId).collect(Collectors.toList()));
        if (fullCourseInfo.getPreviousCourses() == null) {
            fullCourseInfo.setPreviousCourses(Collections.emptyList());
        }

        fullCourseInfo.setNextCourses(courseRepository.getNextCourses(course).stream().map(Course::getId).collect(Collectors.toList()));
        if (fullCourseInfo.getNextCourses() == null) {
            fullCourseInfo.setNextCourses(Collections.emptyList());
        }

        fullCourseInfo.setCreatedBy(userService.getFullUserInfo(courseRepository.getCreator(course)));

        fullCourseInfo.setCanEdit(canEditCourse(user, course));
        fullCourseInfo.setCanEditDevelopersList(canEditDevelopersList(user, course));

        fullCourseInfo.setFaculty(facultyRepository.getFaculty(course));

        if (fullCourseInfo.getFaculty() != null) {
            fullCourseInfo.setDepartment(departmentRepository.getDepartment(course));
        }

        List<User> developers = userRepository.getDevelopersOf(course);
        List<Long> developersIds = developers.stream().map(User::getId).collect(Collectors.toList());
        fullCourseInfo.setDevelopedBy(developersIds);

        fullCourseInfo.setDevelopCompetence(courseRepository.getDevelopCompetences(course).stream().map(c -> (int) (long) c.getId()).collect(Collectors.toList()));

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
            Skills skills = skillsRepository.findOne((long) id);

            if (skills == null) {
                throw new IncorrectIdentifierException("There is no skills with id: " + id);
            }

            return skills;
        }).collect(Collectors.toList());


        List<Knowledge> developKnowledges = courseDto.getDevelopKnowledge().stream().map(id -> {
            Knowledge knowledge = knowledgeRepository.findOne((long) id);

            if (knowledge == null) {
                throw new IncorrectIdentifierException("There is no knowledge with id: " + id);
            }

            return knowledge;
        }).collect(Collectors.toList());

        List<Course> previousCourses = courseDto.getPreviousCourses().stream().map(id -> {
            Course course = courseRepository.findOne((long) id);

            if (course == null) {
                throw new IncorrectIdentifierException("There is no course with id: " + id);
            }

            return course;
        }).collect(Collectors.toList());

        List<Competence> developCompetence = courseDto.getDevelopCompetence().stream().map(id -> {
            Competence competence = competenceRepository.findOne((long) id);

            if (competence == null) {
                throw new IncorrectIdentifierException("There is no competence with id: " + id);
            }

            return competence;
        }).collect(Collectors.toList());

        Course course = courseMapper.fromDto(courseDto);
        Course savedCourse = courseRepository.save(course, courseId);

        courseRepository.deleteAllDevelopRelations(savedCourse);
        developSkills.forEach(skills -> skillsRepository.connectToCourse(skills, savedCourse));
        developKnowledges.forEach(knowledge -> knowledgeRepository.connectToCourse(knowledge, savedCourse));

        courseRepository.removeAllBased(savedCourse);
        previousCourses.forEach(base -> courseRepository.setBasedOn(savedCourse, base));

        developCompetence.forEach(competence -> competenceRepository.connectToCourse(competence, savedCourse));

        Faculty faculty = null;
        if (courseDto.getFaculty() != null &&
                (faculty = facultyRepository.findOne(courseDto.getFaculty().getId())) == null) {
            throw new IncorrectIdentifierException("There is no faculty with id: " + courseDto.getFaculty().getId());
        }

        Department department = null;
        if (courseDto.getDepartment() != null &&
                (department = departmentRepository.findOne(courseDto.getDepartment().getId())) == null) {
            throw new IncorrectIdentifierException("There is no department with id: " + courseDto.getDepartment().getId());
        }

        facultyRepository.disconnectFromFaculty(course);
        if (faculty != null) {
            facultyRepository.connectToCourse(faculty, course);
        }

        departmentRepository.disconnectFromDepartment(course);
        if (department != null) {
            departmentRepository.connectToCourse(department, course);
        }

        courseRepository.deleteAllDevelopsBy(course);
        courseDto.getDevelopedBy().forEach(id -> courseRepository.connectToDeveloper(course, userRepository.findOne((long) (int) id)));

        return getFullCourseInfo(user, savedCourse);
    }

    public boolean canReadCourse(User user, Course course) {
        return true;
    }

    public boolean canEditDevelopersList(User user, Course course) {
        if (user == null) {
            return false;
        }

        FullUserInfo fullUserInfo = userService.getFullUserInfo(user);

        UserRole role = fullUserInfo.getRole().getName();
        Faculty courseFaculty = programRepository.getFacultyOf(programRepository.getProgramOf(course));


        FullCourseInfo fullCourseInfo = getFullCourseInfo(course);
        Faculty responsibleFaculty = fullCourseInfo.getFaculty();
        Department responcibleDepartment = fullCourseInfo.getDepartment();

        return role.equals(UserRole.ADMINISTRATOR)
                || (role.equals(UserRole.DEAN_MEMBER) && fullUserInfo.getFaculty().equals(courseFaculty))
                || role.equals(UserRole.DEAN_MEMBER) && fullUserInfo.getFaculty().equals(responsibleFaculty)
                || role.equals(UserRole.DEPARTMENT_MEMBER) && fullUserInfo.getDepartment().equals(responcibleDepartment);
    }

    public boolean canEditCourse(User user, Course course) {
        if (user == null) {
            return false;
        }

        FullUserInfo fullUserInfo = userService.getFullUserInfo(user);

        UserRole role = fullUserInfo.getRole().getName();
        Faculty courseFaculty = programRepository.getFacultyOf(programRepository.getProgramOf(course));

        FullCourseInfo fullCourseInfo = getFullCourseInfo(course);
        Faculty responsibleFaculty = fullCourseInfo.getFaculty();
        Department responcibleDepartment = fullCourseInfo.getDepartment();

        return fullCourseInfo.getDevelopedBy().contains(user.getId()) ||
                role.equals(UserRole.ADMINISTRATOR) ||
                (role.equals(UserRole.DEAN_MEMBER) && fullUserInfo.getFaculty().equals(courseFaculty)) ||
                (role.equals(UserRole.DEAN_MEMBER) && fullUserInfo.getFaculty().equals(responsibleFaculty)) ||
                (role.equals(UserRole.DEPARTMENT_MEMBER) && fullUserInfo.getDepartment().equals(responcibleDepartment));
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
