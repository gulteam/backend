package ru.nsu.gulteam.prof_standards.backend.web.dto.mapping;

import org.mapstruct.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.nsu.gulteam.prof_standards.backend.domain.node.BasicEducationProgram;
import ru.nsu.gulteam.prof_standards.backend.entity.Trajectory;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.BasicEducationProgramDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.TrajectoryDto;

import java.util.stream.Collectors;

@Configuration
public class TrajectoryMapper {
    private CourseMapper courseMapper;

    @Bean
    public TrajectoryMapper createTrajectoryMapper(CourseMapper courseMapper){
        return new TrajectoryMapper(courseMapper);
    }

    public TrajectoryMapper(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }

    public TrajectoryDto toDto(Trajectory trajectory){
        TrajectoryDto trajectoryDto = new TrajectoryDto();
        trajectoryDto.setCourses(trajectory.getCourses().stream().map(course->courseMapper.toDto(course)).collect(Collectors.toList()));
        return trajectoryDto;
    }
}
