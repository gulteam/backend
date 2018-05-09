package ru.nsu.gulteam.prof_standards.backend.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.*;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.BasicEducationProgramRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.BlockRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.CourseRepository;
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


    public FullCourseInfo addCourseTo(User creator, BasicEducationProgram program) {
        Course course = courseRepository.save(new Course());
        courseRepository.connectToProgram(course, program);
        courseRepository.connectToCreator(course, creator);

        return courseService.getFullCourseInfo(creator, course);
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

    public void deleteProgram(BasicEducationProgram program) {
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

    public BasicEducationProgram updateProgram(BasicEducationProgram oldProgram, BasicEducationProgramDto programDto) {
        BasicEducationProgram program = programMapper.fromDto(programDto);
        programRepository.clearBasedOn(program);

        int programId = (int)(long)oldProgram.getId();
        BasicEducationProgram savedProgram = programRepository.save(program, programId);
        try {
            trajectoryService.updateTrajectories(program); // TODO batalin: async ?
        }
        catch (Exception ignore){
            // Todo asm: может просто тогда хранить список ошибок и их по запросу выдавать? Тип как в линухах, после каждой операции чекать errno.
        }
        return savedProgram;
    }

    public FullBasicEducationProgramInfo getFullProgramInfo(User user, BasicEducationProgram program) {
        return new FullBasicEducationProgramInfo((int) (long) program.getId(),
                program.getName(),
                programRepository.getFacultyOf(program),
                programRepository.getCreator(program),
                program.getFgos(),

                canUpdateProgram(user, program),
                canDeleteProgram(user, program),
                canAddVariableCourse(user, program));
    }

    public List<Competence> getAllRequiredCompetences(User user, BasicEducationProgram program) {
        return programRepository.findRequiredCompetences(program);
    }

    public List<Block> getAllTemplateCourses(User user, BasicEducationProgram basicEducationProgram) {
        return blockRepository.findAllFromProgram(basicEducationProgram);
    }

    // CRUD Permissions //--------------------------------------------------------------------------------------------//

    public boolean canReadProgramsList(User user) {
        return true;
    }

    public boolean canReadProgram(User user, BasicEducationProgram program) {
        return true;
    }

    public boolean canCreateProgram(User user) {
        if (user == null) {
            return false;
        }

        FullUserInfo fullUserInfo = userService.getFullUserInfo(user);

        UserRole role = fullUserInfo.getRole().getName();

        return role.equals(UserRole.ADMINISTRATOR) ||
                (role.equals(UserRole.DEAN_MEMBER) && fullUserInfo.getFaculty() != null);
    }

    public boolean canUpdateProgram(User user, BasicEducationProgram program) {
        if (user == null) {
            return false;
        }

        FullUserInfo fullUserInfo = userService.getFullUserInfo(user);

        UserRole role = fullUserInfo.getRole().getName();

        return role.equals(UserRole.ADMINISTRATOR) ||
                (role.equals(UserRole.DEAN_MEMBER) && fullUserInfo.getFaculty().equals(programRepository.getFacultyOf(program)));
    }

    public boolean canDeleteProgram(User user, BasicEducationProgram program) {
        if (user == null) {
            return false;
        }

        FullUserInfo fullUserInfo = userService.getFullUserInfo(user);

        UserRole role = fullUserInfo.getRole().getName();

        return role.equals(UserRole.ADMINISTRATOR) ||
                (role.equals(UserRole.DEAN_MEMBER) && fullUserInfo.getFaculty().equals(programRepository.getFacultyOf(program)));
    }

    public boolean canAddVariableCourse(User user, BasicEducationProgram program) {
        if (user == null) {
            return false;
        }

        FullUserInfo fullUserInfo = userService.getFullUserInfo(user);

        UserRole role = fullUserInfo.getRole().getName();

        return role.equals(UserRole.ADMINISTRATOR) ||
                (role.equals(UserRole.DEAN_MEMBER) && fullUserInfo.getFaculty().equals(programRepository.getFacultyOf(program))) ||
                        (role.equals(UserRole.DEPARTMENT_MEMBER) && fullUserInfo.getFaculty().equals(programRepository.getFacultyOf(program)));
    }
}
