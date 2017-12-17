package ru.nsu.gulteam.prof_standards.backend.web.dto.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.nsu.gulteam.prof_standards.backend.domain.node.ProfessionalStandard;
import ru.nsu.gulteam.prof_standards.backend.entity.FullSearchRequest;
import ru.nsu.gulteam.prof_standards.backend.web.dto.request.SearchParameters;

import java.util.stream.Collectors;

@Configuration
public class SearchParameterMapper {
    private CourseMapper courseMapper;
    private ProgramMapper programMapper;
    private ProfessionalStandardMapper professionalStandardMapper;

    @Autowired
    public SearchParameterMapper(CourseMapper courseMapper, ProgramMapper programMapper, ProfessionalStandardMapper professionalStandardMapper) {
        this.courseMapper = courseMapper;
        this.programMapper = programMapper;
        this.professionalStandardMapper = professionalStandardMapper;
    }

    public SearchParameterMapper() {
    }

    @Bean
    public SearchParameterMapper createSearchParameterMapper(CourseMapper courseMapper, ProgramMapper programMapper, ProfessionalStandardMapper professionalStandardMapper){
        return new SearchParameterMapper(courseMapper, programMapper, professionalStandardMapper);
    }

    public FullSearchRequest fromDto(SearchParameters searchParameters){
        return new FullSearchRequest(programMapper.fromDto(searchParameters.getBasicEducationProgram()),
                searchParameters.getIncludeCourses().stream().map(courseMapper::fromDto).collect(Collectors.toList()),
                searchParameters.getExcludeCourses().stream().map(courseMapper::fromDto).collect(Collectors.toList()),
                searchParameters.getIncludeStandards().stream().map(professionalStandardMapper::fromDto).collect(Collectors.toList()),
                searchParameters.getExcludeStandards().stream().map(professionalStandardMapper::fromDto).collect(Collectors.toList())
                );
    }
}
