package ru.nsu.gulteam.prof_standards.backend.service;

import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.*;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.*;
import ru.nsu.gulteam.prof_standards.backend.entity.FullCourseInfo;
import ru.nsu.gulteam.prof_standards.backend.exception.IncorrectIdentifierException;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.CourseMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.CourseDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.DepartmentDto;

import java.util.stream.Collectors;

@Service
public class DepartmentService {
    private DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public Department getDepartment(long departmentId) throws IncorrectIdentifierException {
        Department department = departmentRepository.findOne(departmentId);
        if (department == null) {
            throw new IncorrectIdentifierException("There is no department with id: " + departmentId);
        }
        return department;
    }

    public void deleteDepartment(long departmentId) {
        Department department = departmentRepository.findOne(departmentId);
        if (department == null) {
            throw new IncorrectIdentifierException("There is no department with id: " + departmentId);
        }
        deleteDepartment(department);
    }

    public void deleteDepartment(Department department) {
        departmentRepository.delete(department);
    }

    public Department updateDepartment(int departmentId, Department department) {
        return departmentRepository.save(department, departmentId);
    }
}
