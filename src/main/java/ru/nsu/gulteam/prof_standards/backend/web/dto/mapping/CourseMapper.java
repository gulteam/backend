package ru.nsu.gulteam.prof_standards.backend.web.dto.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Course;
import ru.nsu.gulteam.prof_standards.backend.entity.FullCourseInfo;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.CourseDto;

@Mapper(componentModel = "spring",
        uses = {RoleMapper.class,
                FacultyMapper.class,
                DepartmentMapper.class})
public interface CourseMapper {
    @Mappings({
            @Mapping(source = "course.id", target = "id"),
            @Mapping(source = "course.name", target = "name"),
            @Mapping(source = "course.amount", target = "amount"),
            @Mapping(source = "course.semester", target = "semester"),
            @Mapping(source = "course.attestationForm", target = "attestationForm"),
            @Mapping(source = "course.createdBy", target = "createdBy"),
            @Mapping(source = "course.department", target = "department"),
            @Mapping(source = "course.faculty", target = "faculty"),
            @Mapping(source = "course.developedBy", target = "developedBy"),
            @Mapping(source = "course.canUpdate", target = "canUpdate"),
            @Mapping(source = "course.canDelete", target = "canDelete"),
            @Mapping(source = "course.canUpdateDevelopersList", target = "canUpdateDevelopersList"),
    })
    CourseDto toDto(FullCourseInfo course);

    Course fromDto(CourseDto courseDto);
}
