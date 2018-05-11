package ru.nsu.gulteam.prof_standards.backend.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Department;
import ru.nsu.gulteam.prof_standards.backend.domain.node.User;
import ru.nsu.gulteam.prof_standards.backend.domain.type.UserRole;
import ru.nsu.gulteam.prof_standards.backend.service.DepartmentService;
import ru.nsu.gulteam.prof_standards.backend.service.SecurityService;
import ru.nsu.gulteam.prof_standards.backend.service.UserService;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.DepartmentMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.DepartmentDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.Message;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/department")
public class DepartmentController {
    private final UserService userService;
    private final SecurityService securityService;
    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    @RequestMapping(path = "{departmentId}", method = RequestMethod.GET)
    public ResponseEntity<?> get(@PathVariable int departmentId) {
        User user = userService.getUserEntity(securityService.getUserDetails());



        Department department = departmentService.getDepartment(departmentId);
        return ResponseEntity.ok(departmentMapper.toDto(department));
    }

    @RequestMapping(path = "{departmentId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable int departmentId) {
        User user = userService.getUserEntity(securityService.getUserDetails());

        if (!userService.getUserRole(user).getName().equals(UserRole.ADMINISTRATOR)) {
            return ResponseEntity.badRequest().body("You have no permissions to delete department.");
        }

        departmentService.deleteDepartment(departmentId);
        return ResponseEntity.ok(new Message("Department successfully deleted"));
    }

    @RequestMapping(path = "{departmentId}", method = RequestMethod.POST)
    public ResponseEntity<?> update(@PathVariable int departmentId, @RequestBody DepartmentDto departmentDto) {
        User user = userService.getUserEntity(securityService.getUserDetails());

        if (!userService.getUserRole(user).getName().equals(UserRole.ADMINISTRATOR)) {
            return ResponseEntity.badRequest().body("You have no permissions to update department.");
        }

        Department department = departmentService.updateDepartment(departmentId, departmentMapper.fromDto(departmentDto));
        return ResponseEntity.ok(departmentMapper.toDto(department));
    }
}
