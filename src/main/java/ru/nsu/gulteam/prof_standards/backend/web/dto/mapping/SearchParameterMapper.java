package ru.nsu.gulteam.prof_standards.backend.web.dto.mapping;

import org.mapstruct.Mapper;
import ru.nsu.gulteam.prof_standards.backend.entity.FullSearchRequest;
import ru.nsu.gulteam.prof_standards.backend.web.dto.request.SearchParameters;

@Mapper(componentModel = "spring",
        uses = {CourseMapper.class,
                ProgramMapper.class,
                ProfessionalStandardMapper.class})
public interface SearchParameterMapper {
    FullSearchRequest fromDto(SearchParameters searchParameters);
}
