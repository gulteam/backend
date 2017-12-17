package ru.nsu.gulteam.prof_standards.backend.web.dto.mapping;

import org.mapstruct.Mapper;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Department;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Faculty;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.DepartmentDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.FacultyDto;

@Mapper(componentModel = "spring")
public interface FacultyMapper {
    FacultyDto toDto(Faculty faculty);
    Faculty fromDto(FacultyDto facultyDto);
}
