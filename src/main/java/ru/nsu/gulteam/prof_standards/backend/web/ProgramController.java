package ru.nsu.gulteam.prof_standards.backend.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.gulteam.prof_standards.backend.domain.node.BasicEducationProgram;
import ru.nsu.gulteam.prof_standards.backend.domain.node.User;
import ru.nsu.gulteam.prof_standards.backend.entity.AnalyzeResult;
import ru.nsu.gulteam.prof_standards.backend.entity.FullCourseInfo;
import ru.nsu.gulteam.prof_standards.backend.service.AnalyzeService;
import ru.nsu.gulteam.prof_standards.backend.service.ProgramService;
import ru.nsu.gulteam.prof_standards.backend.service.SecurityService;
import ru.nsu.gulteam.prof_standards.backend.service.UserService;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.CourseMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.ProgramMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.BasicEducationProgramDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.Message;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1/program")
public class ProgramController {
    private ProgramService programService;
    private CourseMapper courseMapper;
    private ProgramMapper programMapper;
    private AnalyzeService analyzeService;
    private final SecurityService securityService;
    private final UserService userService;

    @Autowired
    public ProgramController(ProgramService programService, CourseMapper courseMapper, ProgramMapper programMapper, AnalyzeService analyzeService, SecurityService securityService, UserService userService) {
        this.programService = programService;
        this.courseMapper = courseMapper;
        this.programMapper = programMapper;
        this.analyzeService = analyzeService;
        this.securityService = securityService;
        this.userService = userService;
    }

    @RequestMapping(path = "{programId}/addCourse", method = RequestMethod.GET)
    public ResponseEntity<?> addCourse(@PathVariable int programId) {
        User user = userService.getUserEntity(securityService.getUserDetails());

        // Todo: debug
        if(user == null){
            throw new RuntimeException("Cann't create course without user");
        }

        FullCourseInfo course = programService.addCourseTo(user, programId);
        return ResponseEntity.ok(courseMapper.toDto(course));
    }

    @RequestMapping(path = "{programId}/allCourses", method = RequestMethod.GET)
    public ResponseEntity<?> getAllCourses(@PathVariable int programId) {
        List<FullCourseInfo> courses = programService.getAllCourses(programId);
        return ResponseEntity.ok(courses.stream().map(courseMapper::toDto).collect(Collectors.toList()));
    }

    @RequestMapping(path = "/allPrograms", method = RequestMethod.GET)
    public ResponseEntity<?> allPrograms() {
        List<BasicEducationProgram> programs = programService.getAllPrograms();
        return ResponseEntity.ok(programs.stream().map(programMapper::toDto).collect(Collectors.toList()));
    }

    @RequestMapping(path = "{programId}", method = RequestMethod.GET)
    public ResponseEntity<?> get(@PathVariable int programId) {
        BasicEducationProgram program = programService.getProgram(programId);
        return ResponseEntity.ok(programMapper.toDto(program));
    }

    @RequestMapping(path = "{programId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable int programId) {
        programService.deleteProgram(programId);
        return ResponseEntity.ok(new Message("Program successfully deleted"));
    }

    @RequestMapping(path = "{programId}", method = RequestMethod.POST)
    public ResponseEntity<?> update(@PathVariable int programId, @RequestBody BasicEducationProgramDto programDto) {
        BasicEducationProgram program = programService.updateProgram(programId, programDto);
        return ResponseEntity.ok(programMapper.toDto(program));
    }

    @RequestMapping(path = "/addProgram", method = RequestMethod.GET)
    public ResponseEntity<?> add() {
        BasicEducationProgram program = programService.addProgram();
        return ResponseEntity.ok(programMapper.toDto(program));
    }

    @RequestMapping(path = "{programId}/analyze", method = RequestMethod.GET)
    public ResponseEntity<?> analyze(@PathVariable int programId) {
        AnalyzeResult analyzeResult = analyzeService.analyze(programService.getProgram(programId));
        return ResponseEntity.ok(analyzeResult);
    }
}
