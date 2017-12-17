package ru.nsu.gulteam.prof_standards.backend.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.gulteam.prof_standards.backend.domain.node.User;
import ru.nsu.gulteam.prof_standards.backend.entity.Trajectory;
import ru.nsu.gulteam.prof_standards.backend.service.SearchService;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.SearchParameterMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.TrajectoryMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.request.SearchParameters;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1")
public class SearchController {
    private SearchService searchService;
    private TrajectoryMapper trajectoryMapper;
    private SearchParameterMapper searchParameterMapper;

    @Autowired
    public SearchController(SearchService searchService, TrajectoryMapper trajectoryMapper, SearchParameterMapper searchParameterMapper) {
        this.searchService = searchService;
        this.trajectoryMapper = trajectoryMapper;
        this.searchParameterMapper = searchParameterMapper;
    }

    @RequestMapping(path = "search", method = RequestMethod.POST)
    public ResponseEntity<?> search(@RequestBody SearchParameters searchParameters) {
        List<Trajectory> searchResult = searchService.search(searchParameterMapper.fromDto(searchParameters));
        return ResponseEntity.ok(searchResult.stream().map(trajectoryMapper::toDto).collect(Collectors.toList()));
    }
}
