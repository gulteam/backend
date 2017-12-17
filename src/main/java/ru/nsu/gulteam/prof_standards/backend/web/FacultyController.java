package ru.nsu.gulteam.prof_standards.backend.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Department;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Faculty;
import ru.nsu.gulteam.prof_standards.backend.service.FacultyService;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.DepartmentMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.FacultyMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.FacultyDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.Message;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1/faculty")
public class FacultyController {
    private FacultyService facultyService;
    private DepartmentMapper departmentMapper;
    private FacultyMapper facultyMapper;

    public FacultyController(FacultyService facultyService, DepartmentMapper departmentMapper, FacultyMapper facultyMapper) {
        this.facultyService = facultyService;
        this.departmentMapper = departmentMapper;
        this.facultyMapper = facultyMapper;
    }

    @RequestMapping(path = "{facultyId}/addDepartment", method = RequestMethod.GET)
    public ResponseEntity<?> addDepartment(@PathVariable int facultyId) {
        Department department = facultyService.addDepartmentTo(facultyId);
        return ResponseEntity.ok(departmentMapper.toDto(department));
    }

    @RequestMapping(path = "{facultyId}/allDepartments", method = RequestMethod.GET)
    public ResponseEntity<?> getAllDepartments(@PathVariable int facultyId) {
        List<Department> departments = facultyService.getAllDepartments(facultyId);
        return ResponseEntity.ok(departments.stream().map(departmentMapper::toDto).collect(Collectors.toList()));
    }

    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public ResponseEntity<?> allFaculties() {
        List<Faculty> faculties = facultyService.getAllFaculties();
        return ResponseEntity.ok(faculties.stream().map(facultyMapper::toDto).collect(Collectors.toList()));
    }

    @RequestMapping(path = "{facultyId}", method = RequestMethod.GET)
    public ResponseEntity<?> get(@PathVariable int facultyId) {
        Faculty faculty = facultyService.getFaculty(facultyId);
        return ResponseEntity.ok(facultyMapper.toDto(faculty));
    }

    @RequestMapping(path = "{facultyId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable int facultyId) {
        facultyService.deleteFaculty(facultyId);
        return ResponseEntity.ok(new Message("Faculty successfully deleted"));
    }

    @RequestMapping(path = "{facultyId}", method = RequestMethod.POST)
    public ResponseEntity<?> update(@PathVariable int facultyId, @RequestBody FacultyDto facultyDto) {
        Faculty faculty = facultyService.updateFaculty(facultyId, facultyMapper.fromDto(facultyDto));
        return ResponseEntity.ok(facultyMapper.toDto(faculty));
    }

    @RequestMapping(path = "/addFaculty", method = RequestMethod.GET)
    public ResponseEntity<?> add() {
        Faculty faculty = facultyService.addFaculty();
        return ResponseEntity.ok(facultyMapper.toDto(faculty));
    }
}
