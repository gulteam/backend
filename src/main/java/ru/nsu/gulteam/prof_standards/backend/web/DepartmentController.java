package ru.nsu.gulteam.prof_standards.backend.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Department;
import ru.nsu.gulteam.prof_standards.backend.entity.FullCourseInfo;
import ru.nsu.gulteam.prof_standards.backend.service.CourseService;
import ru.nsu.gulteam.prof_standards.backend.service.DepartmentService;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.CourseMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.DepartmentMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.CourseDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.DepartmentDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.Message;

@RestController
@RequestMapping(path = "api/v1/department")
public class DepartmentController {

    private DepartmentService departmentService;
    private DepartmentMapper departmentMapper;

    public DepartmentController(DepartmentService departmentService, DepartmentMapper departmentMapper) {
        this.departmentService = departmentService;
        this.departmentMapper = departmentMapper;
    }

    @RequestMapping(path = "{departmentId}", method = RequestMethod.GET)
    public ResponseEntity<?> get(@PathVariable int departmentId) {
        Department department = departmentService.getDepartment(departmentId);
        return ResponseEntity.ok(departmentMapper.toDto(department));
    }

    @RequestMapping(path = "{departmentId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable int departmentId) {
        departmentService.deleteDepartment(departmentId);
        return ResponseEntity.ok(new Message("Department successfully deleted"));
    }

    @RequestMapping(path = "{departmentId}", method = RequestMethod.POST)
    public ResponseEntity<?> update(@PathVariable int departmentId, @RequestBody DepartmentDto departmentDto) {
        Department department = departmentService.updateDepartment(departmentId, departmentMapper.fromDto(departmentDto));
        return ResponseEntity.ok(departmentMapper.toDto(department));
    }
}
