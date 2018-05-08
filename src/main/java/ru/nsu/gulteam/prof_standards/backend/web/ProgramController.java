package ru.nsu.gulteam.prof_standards.backend.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.gulteam.prof_standards.backend.domain.node.BasicEducationProgram;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Competence;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Fgos;
import ru.nsu.gulteam.prof_standards.backend.domain.node.User;
import ru.nsu.gulteam.prof_standards.backend.entity.AnalyzeResult;
import ru.nsu.gulteam.prof_standards.backend.entity.FullBasicEducationProgramInfo;
import ru.nsu.gulteam.prof_standards.backend.entity.FullCourseInfo;
import ru.nsu.gulteam.prof_standards.backend.entity.FullBlockInfo;
import ru.nsu.gulteam.prof_standards.backend.service.*;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.CompetenceMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.CourseMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.ProgramMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.BlockMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.BasicEducationProgramDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.BlockDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.Message;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1/program")
@AllArgsConstructor
public class ProgramController {
    private final SecurityService securityService;
    private final UserService userService;
    private final ProgramService programService;
    private final CourseMapper courseMapper;
    private final ProgramMapper programMapper;
    private final AnalyzeService analyzeService;
    private final BlockMapper blockMapper;
    private final BlockService blockService;
    private final FgosService fgosService;
    private final CompetenceMapper competenceMapper;

    @RequestMapping(path = "{programId}/addCourse", method = RequestMethod.GET)
    public ResponseEntity<?> addCourse(@PathVariable int programId) {
        User user = userService.getUserEntity(securityService.getUserDetails());

        if (user == null) {
            throw new RuntimeException("Cann't create course without user");
        }

        FullCourseInfo course = programService.addCourseTo(user, programId);
        return ResponseEntity.ok(courseMapper.toDto(course));
    }

    @RequestMapping(path = "{programId}/addTemplateCourse", method = RequestMethod.GET)
    public ResponseEntity<?> addTemplateCourse(@PathVariable int programId) {
        User user = userService.getUserEntity(securityService.getUserDetails());

        if (user == null) {
            throw new RuntimeException("Cann't create template course without user");
        }

        FullBlockInfo course = blockService.addBlockTo(user, programId);
        return ResponseEntity.ok(blockMapper.toDto(course));
    }

    @RequestMapping(path = "{programId}/allCourses", method = RequestMethod.GET)
    public ResponseEntity<?> getAllCourses(@PathVariable int programId) {
        User user = userService.getUserEntity(securityService.getUserDetails());

        List<FullCourseInfo> courses = programService.getAllCourses(user, programId);
        return ResponseEntity.ok(courses.stream().map(courseMapper::toDto).collect(Collectors.toList()));
    }

    @RequestMapping(path = "{programId}/allTemplateCourses", method = RequestMethod.GET)
    public ResponseEntity<?> getAllTemplateCourses(@PathVariable int programId) {
        User user = userService.getUserEntity(securityService.getUserDetails());

        List<FullBlockInfo> blocks = blockService.getAllTemplateCourses(user, programId);
        List<BlockDto> blockDtos = blocks.stream().map(blockMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(blockDtos);
    }

    @RequestMapping(path = "/allPrograms", method = RequestMethod.GET)
    public ResponseEntity<?> allPrograms() {
        User user = userService.getUserEntity(securityService.getUserDetails());

        List<BasicEducationProgram> programs = programService.getAllPrograms();
        List<BasicEducationProgramDto> programDtos = programs.stream().map(program -> programService.getFullProgramInfo(user, program)).map(programMapper::toDto).collect(Collectors.toList());

        return ResponseEntity.ok(programDtos);
    }

    @RequestMapping(path = "{programId}", method = RequestMethod.GET)
    public ResponseEntity<?> get(@PathVariable int programId) {
        User user = userService.getUserEntity(securityService.getUserDetails());

        BasicEducationProgram program = programService.getProgram(programId);
        FullBasicEducationProgramInfo fullInfo = programService.getFullProgramInfo(user, program);

        return ResponseEntity.ok(programMapper.toDto(fullInfo));
    }

    @RequestMapping(path = "{programId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable int programId) {
        programService.deleteProgram(programId);
        return ResponseEntity.ok(new Message("Program successfully deleted"));
    }

    @RequestMapping(path = "{programId}", method = RequestMethod.POST)
    public ResponseEntity<?> update(@PathVariable int programId, @RequestBody BasicEducationProgramDto programDto) {
        User user = userService.getUserEntity(securityService.getUserDetails());

        BasicEducationProgram program = programService.updateProgram(programId, programDto);
        FullBasicEducationProgramInfo fullInfo = programService.getFullProgramInfo(user, program);

        return ResponseEntity.ok(programMapper.toDto(fullInfo));
    }

    @RequestMapping(path = "/addProgram", method = RequestMethod.GET)
    public ResponseEntity<?> add() {
        User user = userService.getUserEntity(securityService.getUserDetails());

        // Todo: debug
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cann't create course without user");
        }

        if (!programService.canCreateProgram(user)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This user have no permissions to create programs");
        }

        BasicEducationProgram program = programService.addProgram(user);
        FullBasicEducationProgramInfo fullInfo = programService.getFullProgramInfo(user, program);

        return ResponseEntity.ok(programMapper.toDto(fullInfo));
    }

    @RequestMapping(path = "{programId}/analyze", method = RequestMethod.GET)
    public ResponseEntity<?> analyze(@PathVariable int programId) {
        AnalyzeResult analyzeResult = analyzeService.analyze(programService.getProgram(programId));
        return ResponseEntity.ok(analyzeResult);
    }

    @RequestMapping(path = "{programId}/allRequiredCompetences", method = RequestMethod.GET)
    public ResponseEntity<?> getAllRequiredCompetences(@PathVariable int programId) {
        User user = userService.getUserEntity(securityService.getUserDetails());

        List<Competence> competences = programService.getAllRequiredCompetences(user, programId);
        return ResponseEntity.ok(competences.stream().map(competenceMapper::toDto).collect(Collectors.toList()));
    }
}
