package ru.nsu.gulteam.prof_standards.backend.web.dto.mapping;

import org.mapstruct.Mapper;
import ru.nsu.gulteam.prof_standards.backend.entity.Trajectory;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.TrajectoryDto;

@Mapper(componentModel = "spring", uses = {CourseMapper.class})
public interface TrajectoryMapper {
    TrajectoryDto toDto(Trajectory trajectory);
}
