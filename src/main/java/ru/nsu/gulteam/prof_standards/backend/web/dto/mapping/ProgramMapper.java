package ru.nsu.gulteam.prof_standards.backend.web.dto.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import ru.nsu.gulteam.prof_standards.backend.domain.node.BasicEducationProgram;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Course;
import ru.nsu.gulteam.prof_standards.backend.entity.FullBasicEducationProgramInfo;
import ru.nsu.gulteam.prof_standards.backend.entity.FullCourseInfo;
import ru.nsu.gulteam.prof_standards.backend.service.ProgramService;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.BasicEducationProgramDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.CourseDto;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class,
                FacultyMapper.class,
                DepartmentMapper.class,
                FgosMapper.class})
public interface ProgramMapper {
    @Mappings({
            @Mapping(source = "program.createdBy", target = "createdBy"),
            @Mapping(source = "program.fgos", target = "fgos"),
            @Mapping(source = "program.canAddVariableCourse", target = "canAddVariableCourse"),
            @Mapping(source = "program.canUpdate", target = "canUpdate"),
            @Mapping(source = "program.canDelete", target = "canDelete"),
    })
    BasicEducationProgramDto toDto(FullBasicEducationProgramInfo program);
    BasicEducationProgram fromDto(BasicEducationProgramDto programDto);
}
