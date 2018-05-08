package ru.nsu.gulteam.prof_standards.backend.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Department;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Faculty;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Fgos;
import ru.nsu.gulteam.prof_standards.backend.service.FacultyService;
import ru.nsu.gulteam.prof_standards.backend.service.FgosService;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.DepartmentMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.FacultyMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.FgosMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.FacultyDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.FgosDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.Message;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1/fgos")
@RequiredArgsConstructor
public class FgosController {
    private final FgosService fgosService;
    private final FgosMapper fgosMapper;

    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public ResponseEntity<?> allFgoses() {
        List<Fgos> fgoses = fgosService.getAll();
        List<FgosDto> fgosDtos = fgoses.stream().map(fgosMapper::toDto).collect(Collectors.toList());

        return ResponseEntity.ok(fgosDtos);
    }

    @RequestMapping(path = "{fgosId}", method = RequestMethod.GET)
    public ResponseEntity<?> get(@PathVariable int fgosId) {
        Fgos fgos = fgosService.get(fgosId);
        return ResponseEntity.ok(fgosMapper.toDto(fgos));
    }

    @RequestMapping(path = "{fgosId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable int fgosId) {
        fgosService.delete(fgosId);
        return ResponseEntity.ok(new Message("Faculty successfully deleted"));
    }

    @RequestMapping(path = "{fgosId}", method = RequestMethod.POST)
    public ResponseEntity<?> update(@PathVariable int fgosId, @RequestBody FgosDto fgosDto) {
        Fgos fgos = fgosService.updateFgos(fgosId, fgosMapper.fromDto(fgosDto));
        return ResponseEntity.ok(fgosMapper.toDto(fgos));
    }

    @RequestMapping(path = "/create", method = RequestMethod.GET)
    public ResponseEntity<?> create() {
        Fgos fgos = fgosService.create();
        return ResponseEntity.ok(fgosMapper.toDto(fgos));
    }

    @RequestMapping(path = "{fgosId}/addCompetence", method = RequestMethod.GET)
    public ResponseEntity<?> addCompetence(@PathVariable int fgosId) {
        fgosService.addCompetence(fgosId);
        return ResponseEntity.ok(new Message("Competence added"));
    }

    @RequestMapping(path = "{fgosId}/addReuiredCourse", method = RequestMethod.GET)
    public ResponseEntity<?> addReuiredCourse(@PathVariable int fgosId) {
        fgosService.addReuiredCourse(fgosId);
        return ResponseEntity.ok(new Message("Competence added"));
    }
}
