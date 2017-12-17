package ru.nsu.gulteam.prof_standards.backend.entity;

import ru.nsu.gulteam.prof_standards.backend.domain.node.Department;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Faculty;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Role;
import ru.nsu.gulteam.prof_standards.backend.domain.node.User;

public class FullUserInfo {
    private User user;
    private Role role;
    private Department department;
    private Faculty faculty;

    public FullUserInfo() {
    }

    public FullUserInfo(User user, Role role, Department department, Faculty faculty) {
        this.user = user;
        this.role = role;
        this.department = department;
        this.faculty = faculty;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }
}
