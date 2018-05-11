package ru.nsu.gulteam.prof_standards.backend.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Fgos;
import ru.nsu.gulteam.prof_standards.backend.domain.node.User;
import ru.nsu.gulteam.prof_standards.backend.service.FgosService;
import ru.nsu.gulteam.prof_standards.backend.service.SecurityService;
import ru.nsu.gulteam.prof_standards.backend.service.UserService;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.FgosMapper;
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
    private final UserService userService;
    private final SecurityService securityService;

    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public ResponseEntity<?> allFgoses() {
        User user = userService.getUserEntity(securityService.getUserDetails());

        if (!fgosService.canReadFgosList(user)) {
            return ResponseEntity.badRequest().body("You have no permissions to read fgos list.");
        }

        List<Fgos> fgoses = fgosService.getAll();
        List<FgosDto> fgosDtos = fgoses.stream().map(f -> fgosService.getFullInfo(user, f)).map(fgosMapper::toDto).collect(Collectors.toList());

        return ResponseEntity.ok(fgosDtos);
    }

    @RequestMapping(path = "{fgosId}", method = RequestMethod.GET)
    public ResponseEntity<?> get(@PathVariable int fgosId) {
        User user = userService.getUserEntity(securityService.getUserDetails());
        Fgos fgos = fgosService.get(fgosId);

        if (!fgosService.canReadFgos(user, fgos)) {
            return ResponseEntity.badRequest().body("You have no permissions to read fgos.");
        }

        return ResponseEntity.ok(fgosMapper.toDto(fgosService.getFullInfo(user, fgos)));
    }

    @RequestMapping(path = "{fgosId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable int fgosId) {
        User user = userService.getUserEntity(securityService.getUserDetails());
        Fgos fgos = fgosService.get(fgosId);

        if (!fgosService.canDeleteFgos(user, fgos)) {
            return ResponseEntity.badRequest().body("You have no permissions to delete fgos.");
        }

        fgosService.delete(fgos);
        return ResponseEntity.ok(new Message("Faculty successfully deleted"));
    }

    @RequestMapping(path = "{fgosId}", method = RequestMethod.POST)
    public ResponseEntity<?> update(@PathVariable int fgosId, @RequestBody FgosDto fgosDto) {
        User user = userService.getUserEntity(securityService.getUserDetails());
        Fgos oldFgos = fgosService.get(fgosId);

        if (!fgosService.canUpdateFgos(user, oldFgos)) {
            return ResponseEntity.badRequest().body("You have no permissions to update fgos.");
        }

        Fgos fgos = fgosService.updateFgos(oldFgos, fgosMapper.fromDto(fgosDto));
        return ResponseEntity.ok(fgosMapper.toDto(fgosService.getFullInfo(user, fgos)));
    }

    @RequestMapping(path = "/create", method = RequestMethod.GET)
    public ResponseEntity<?> create() {
        User user = userService.getUserEntity(securityService.getUserDetails());

        if (!fgosService.canCreateFgos(user)) {
            return ResponseEntity.badRequest().body("You have no permissions to create fgos.");
        }

        Fgos fgos = fgosService.create(user);
        return ResponseEntity.ok(fgosMapper.toDto(fgosService.getFullInfo(user, fgos)));
    }

    @RequestMapping(path = "{fgosId}/addCompetence", method = RequestMethod.GET)
    public ResponseEntity<?> addCompetence(@PathVariable int fgosId) {
        User user = userService.getUserEntity(securityService.getUserDetails());
        Fgos fgos = fgosService.get(fgosId);

        if (!fgosService.canUpdateFgos(user, fgos)) {
            return ResponseEntity.badRequest().body("You have no permissions to add competences to fgos.");
        }

        fgosService.addCompetence(fgos);
        return ResponseEntity.ok(new Message("Competence added"));
    }

    @RequestMapping(path = "{fgosId}/addReuiredCourse", method = RequestMethod.GET)
    public ResponseEntity<?> addReuiredCourse(@PathVariable int fgosId) {
        User user = userService.getUserEntity(securityService.getUserDetails());
        Fgos fgos = fgosService.get(fgosId);

        if (!fgosService.canUpdateFgos(user, fgos)) {
            return ResponseEntity.badRequest().body("You have no permissions to add required courses to fgos.");
        }

        fgosService.addReuiredCourse(fgos);
        return ResponseEntity.ok(new Message("Competence added"));
    }
}
