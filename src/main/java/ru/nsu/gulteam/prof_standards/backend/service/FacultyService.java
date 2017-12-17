package ru.nsu.gulteam.prof_standards.backend.service;

import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.BasicEducationProgram;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Course;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Department;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Faculty;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.BasicEducationProgramRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.CourseRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.DepartmentRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.FacultyRepository;
import ru.nsu.gulteam.prof_standards.backend.entity.FullCourseInfo;
import ru.nsu.gulteam.prof_standards.backend.exception.IncorrectIdentifierException;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.FacultyMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.ProgramMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.BasicEducationProgramDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private FacultyRepository facultyRepository;
    private DepartmentRepository departmentRepository;
    private DepartmentService departmentService;
    private FacultyMapper facultyMapper;

    public FacultyService(FacultyRepository facultyRepository, DepartmentRepository departmentRepository, DepartmentService departmentService, FacultyMapper facultyMapper) {
        this.facultyRepository = facultyRepository;
        this.departmentRepository = departmentRepository;
        this.departmentService = departmentService;
        this.facultyMapper = facultyMapper;
    }

    public Department addDepartmentTo(long facultyId) {
        Faculty faculty = facultyRepository.findOne(facultyId);
        if (faculty == null) {
            throw new IncorrectIdentifierException("There is no faculty with id: " + facultyId);
        }

        Department department = departmentRepository.save(new Department());
        departmentRepository.connectToFaculty(department, faculty);

        return department;
    }

    public List<Department> getAllDepartments(long facultyId) {
        Faculty faculty = facultyRepository.findOne(facultyId);
        if (faculty == null) {
            throw new IncorrectIdentifierException("There is no faculty with id: " + facultyId);
        }

        return getAllDepartments(faculty);
    }

    public List<Department> getAllDepartments(Faculty faculty) {
        List<Department> departments = departmentRepository.findAllFromFaculty(faculty);
        return departments;
    }

    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    public Faculty getFaculty(long facultyId) {
        Faculty faculty = facultyRepository.findOne(facultyId);
        if (faculty == null) {
            throw new IncorrectIdentifierException("There is no faculty with id: " + facultyId);
        }
        return faculty;
    }

    public void deleteFaculty(long facultyId) {
        Faculty faculty = facultyRepository.findOne(facultyId);
        if (faculty == null) {
            throw new IncorrectIdentifierException("There is no faculty with id: " + facultyId);
        }
        getAllDepartments(faculty).forEach(departmentService::deleteDepartment);
        facultyRepository.deleteConnections(faculty);
        facultyRepository.delete(faculty);
    }

    public Faculty addFaculty() {
        return facultyRepository.save(new Faculty());
    }

    public Faculty updateFaculty(int facultyId, Faculty faculty) {
        Faculty savedFaculty = facultyRepository.save(faculty, facultyId);
        return savedFaculty;
    }
}
