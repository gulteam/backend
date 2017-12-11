package ru.nsu.gulteam.prof_standards.backend.web.dto.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.nsu.gulteam.prof_standards.backend.domain.node.BasicEducationProgram;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Course;
import ru.nsu.gulteam.prof_standards.backend.entity.FullCourseInfo;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.BasicEducationProgramDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.CourseDto;

@Mapper(componentModel = "spring")
public interface ProgramMapper {
    BasicEducationProgramDto toDto(BasicEducationProgram program);
    BasicEducationProgram fromDto(BasicEducationProgramDto programDto);
}
