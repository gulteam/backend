package ru.nsu.gulteam.prof_standards.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.BasicEducationProgram;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Course;
import ru.nsu.gulteam.prof_standards.backend.domain.node.User;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.BasicEducationProgramRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.CourseRepository;
import ru.nsu.gulteam.prof_standards.backend.entity.FullCourseInfo;
import ru.nsu.gulteam.prof_standards.backend.exception.IncorrectIdentifierException;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.ProgramMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.BasicEducationProgramDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgramService {
    private BasicEducationProgramRepository programRepository;
    private CourseRepository courseRepository;
    private CourseService courseService;
    private ProgramMapper programMapper;

    @Autowired
    public ProgramService(BasicEducationProgramRepository programRepository, CourseRepository courseRepository, CourseService courseService, ProgramMapper programMapper) {
        this.programRepository = programRepository;
        this.courseRepository = courseRepository;
        this.courseService = courseService;
        this.programMapper = programMapper;
    }

    public FullCourseInfo addCourseTo(User creator, long programId) {
        BasicEducationProgram program = programRepository.findOne(programId);

        if (program == null) {
            throw new IncorrectIdentifierException("There is no program with id: " + programId);
        }

        Course course = courseRepository.save(new Course());
        courseRepository.connectToProgram(course, program);
        courseRepository.connectToCreator(course, creator);

        return courseService.getFullCourseInfo(course);
    }

    public List<FullCourseInfo> getAllCourses(long programId) {
        BasicEducationProgram program = programRepository.findOne(programId);
        if (program == null) {
            throw new IncorrectIdentifierException("There is no program with id: " + programId);
        }

        return getAllCourses(program);
    }

    public List<FullCourseInfo> getAllCourses(BasicEducationProgram program) {
        List<Course> courses = courseRepository.findAllFromProgram(program);
        return courses.stream().map(courseService::getFullCourseInfo).collect(Collectors.toList());
    }

    public List<BasicEducationProgram> getAllPrograms() {
        return programRepository.findAll();
    }

    public BasicEducationProgram getProgram(long programId) {
        BasicEducationProgram program = programRepository.findOne(programId);
        if (program == null) {
            throw new IncorrectIdentifierException("There is no program with id: " + programId);
        }
        return program;
    }

    public void deleteProgram(long programId) {
        BasicEducationProgram program = programRepository.findOne(programId);
        if (program == null) {
            throw new IncorrectIdentifierException("There is no program with id: " + programId);
        }
        programRepository.deleteConnections(program);
        programRepository.delete(program);
    }

    public BasicEducationProgram addProgram() {
        return programRepository.save(new BasicEducationProgram());
    }

    public BasicEducationProgram updateProgram(int programId, BasicEducationProgramDto programDto) {
        BasicEducationProgram program = programMapper.fromDto(programDto);
        BasicEducationProgram savedProgram = programRepository.save(program, programId);
        return savedProgram;
    }
}
