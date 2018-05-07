package ru.nsu.gulteam.prof_standards.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Department;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Faculty;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Role;
import ru.nsu.gulteam.prof_standards.backend.domain.node.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullUserInfo {
    private User user;
    private Role role;
    private Department department;
    private Faculty faculty;

    private boolean canEdit;
    private boolean canChangeRole;
    private boolean canChangeFacultyAndDepartment;
}
