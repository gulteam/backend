package ru.nsu.gulteam.prof_standards.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.BasicEducationProgram;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Course;
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
    private TrajectoryService trajectoryService;

    @Autowired
    public ProgramService(BasicEducationProgramRepository programRepository, CourseRepository courseRepository, CourseService courseService, ProgramMapper programMapper, TrajectoryService trajectoryService) {
        this.programRepository = programRepository;
        this.courseRepository = courseRepository;
        this.courseService = courseService;
        this.programMapper = programMapper;
        this.trajectoryService = trajectoryService;
    }

    public FullCourseInfo addCourseTo(long programId) {
        BasicEducationProgram program = programRepository.findOne(programId);

        if (program == null) {
            throw new IncorrectIdentifierException("There is no program with id: " + programId);
        }

        Course course = courseRepository.save(new Course());
        courseRepository.connectToProgram(course, program);

        return courseService.getFullCourseInfo(course);
    }

    public List<FullCourseInfo> getAllCourses(long programId) {
        BasicEducationProgram program = programRepository.findOne(programId);
        if (program == null) {
            throw new IncorrectIdentifierException("There is no program with id: " + programId);
        }

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
        BasicEducationProgram program = programRepository.save(new BasicEducationProgram());
        trajectoryService.updateTrajectories(program);
        return program;
    }

    public BasicEducationProgram updateProgram(int programId, BasicEducationProgramDto programDto) {
        BasicEducationProgram program = programMapper.fromDto(programDto);
        BasicEducationProgram savedProgram = programRepository.save(program, programId);
        trajectoryService.updateTrajectories(program); // TODO batalin: async ?
        return savedProgram;
    }
}
