package ru.nsu.gulteam.prof_standards.backend.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.*;
import ru.nsu.gulteam.prof_standards.backend.domain.node.TemplateCourse;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.BasicEducationProgramRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.CourseRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.BlockRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.TemplateCourseRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.type.UserRole;
import ru.nsu.gulteam.prof_standards.backend.entity.FullBasicEducationProgramInfo;
import ru.nsu.gulteam.prof_standards.backend.entity.FullCourseInfo;
import ru.nsu.gulteam.prof_standards.backend.entity.FullUserInfo;
import ru.nsu.gulteam.prof_standards.backend.exception.IncorrectIdentifierException;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.ProgramMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.BasicEducationProgramDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProgramService {
    private BasicEducationProgramRepository programRepository;
    private CourseRepository courseRepository;
    private CourseService courseService;
    private ProgramMapper programMapper;
    private FacultyService facultyService;
    private UserService userService;
    private BlockRepository blockRepository;
    private TrajectoryService trajectoryService;


    public FullCourseInfo addCourseTo(User creator, long programId) {
        BasicEducationProgram program = programRepository.findOne(programId);

        if (program == null) {
            throw new IncorrectIdentifierException("There is no program with id: " + programId);
        }

        return addCourseTo(creator, program);
    }

    public FullCourseInfo addCourseTo(User creator, BasicEducationProgram program) {
        Course course = courseRepository.save(new Course());
        courseRepository.connectToProgram(course, program);
        courseRepository.connectToCreator(course, creator);

        return courseService.getFullCourseInfo(creator, course);
    }

    public List<FullCourseInfo> getAllCourses(User user, long programId) {
        BasicEducationProgram program = programRepository.findOne(programId);
        if (program == null) {
            throw new IncorrectIdentifierException("There is no program with id: " + programId);
        }

        return getAllCourses(user, program);
    }

    public List<FullCourseInfo> getAllCourses(User user, BasicEducationProgram program) {
        List<Course> courses = courseRepository.findAllFromProgram(program);
        return courses.stream().map(course -> courseService.getFullCourseInfo(user, course)).collect(Collectors.toList());
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

    public BasicEducationProgram addProgram(User creator) {
        BasicEducationProgram program = programRepository.save(new BasicEducationProgram());

        programRepository.connectToCreator(program, creator);
        programRepository.connectToFaculty(program, userService.getFullUserInfo(creator).getFaculty());
        trajectoryService.updateTrajectories(program);
        return program;
    }

    public BasicEducationProgram updateProgram(int programId, BasicEducationProgramDto programDto) {
        BasicEducationProgram program = programMapper.fromDto(programDto);
        programRepository.clearBasedOn(program);
        BasicEducationProgram savedProgram = programRepository.save(program, programId);
        trajectoryService.updateTrajectories(program); // TODO batalin: async ?
        return savedProgram;
    }

    public boolean canCreateProgram(User user) {
        FullUserInfo fullUserInfo = userService.getFullUserInfo(user);

        UserRole role = fullUserInfo.getRole().getName();

        return role.equals(UserRole.DEAN_MEMBER) && fullUserInfo.getFaculty() != null;
    }

    public boolean canReadProgram(User user, BasicEducationProgram program) {
        return true;
    }

    public boolean canEditProgram(User user, BasicEducationProgram program) {
        if (user == null) {
            return false;
        }

        FullUserInfo fullUserInfo = userService.getFullUserInfo(user);

        UserRole role = fullUserInfo.getRole().getName();

        return role.equals(UserRole.ADMINISTRATOR) || role.equals(UserRole.DEAN_MEMBER) && fullUserInfo.getFaculty().equals(programRepository.getFacultyOf(program));
    }

    public FullBasicEducationProgramInfo getFullProgramInfo(User user, BasicEducationProgram program) {
        FullBasicEducationProgramInfo fullInfo = new FullBasicEducationProgramInfo((int) (long) program.getId(),
                program.getName(),
                canEditProgram(user, program),
                programRepository.getFacultyOf(program),
                programRepository.getCreator(program),
                program.getFgos());

        return fullInfo;
    }

    public List<Competence> getAllRequiredCompetences(User user, long programId) {
        return programRepository.findRequiredCompetences(programRepository.findOne(programId));
    }

    public List<Block> getAllTemplateCourses(User user, BasicEducationProgram basicEducationProgram) {
        return blockRepository.findAllFromProgram(basicEducationProgram);
    }
}
