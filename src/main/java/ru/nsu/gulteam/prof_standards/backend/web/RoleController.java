package ru.nsu.gulteam.prof_standards.backend.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Role;
import ru.nsu.gulteam.prof_standards.backend.service.RoleService;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.RoleMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1")
public class RoleController {
    private RoleMapper roleMapper;
    private RoleService roleService;

    @Autowired
    public RoleController(RoleMapper roleMapper, RoleService roleService) {
        this.roleMapper = roleMapper;
        this.roleService = roleService;
    }

    @RequestMapping(path = "/allRoles", method = RequestMethod.GET)
    public ResponseEntity<?> allRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles.stream().map(roleMapper::toDto).collect(Collectors.toList()));
    }
}
