package ru.nsu.gulteam.prof_standards.backend.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.gulteam.prof_standards.backend.domain.node.BasicEducationProgram;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Competence;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Department;
import ru.nsu.gulteam.prof_standards.backend.domain.node.User;
import ru.nsu.gulteam.prof_standards.backend.service.CompetenceService;
import ru.nsu.gulteam.prof_standards.backend.service.DepartmentService;
import ru.nsu.gulteam.prof_standards.backend.service.SecurityService;
import ru.nsu.gulteam.prof_standards.backend.service.UserService;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.CompetenceMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.DepartmentMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.CompetenceDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.DepartmentDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.Message;

@RestController
@RequestMapping(path = "api/v1/competence")
@RequiredArgsConstructor
public class CompetenceController {
    private final SecurityService securityService;
    private final UserService userService;
    private final CompetenceService competenceService;
    private final CompetenceMapper competenceMapper;

    @RequestMapping(path = "{competenceId}", method = RequestMethod.GET)
    public ResponseEntity<?> get(@PathVariable int competenceId) {
        User user = userService.getUserEntity(securityService.getUserDetails());
        Competence competence = competenceService.get(competenceId);

        if (!competenceService.canReadCompetence(user, competence)) {
            return ResponseEntity.badRequest().body("You have no permissions to read competence");
        }

        return ResponseEntity.ok(competenceMapper.toDto(competence));
    }

    @RequestMapping(path = "{competenceId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable int competenceId) {
        User user = userService.getUserEntity(securityService.getUserDetails());
        Competence competence = competenceService.get(competenceId);

        if (!competenceService.canDeleteCompetence(user, competence)) {
            return ResponseEntity.badRequest().body("You have no permissions to delete competence");
        }

        competenceService.delete(competence);
        return ResponseEntity.ok(new Message("Competence successfully deleted"));
    }

    @RequestMapping(path = "{competenceId}", method = RequestMethod.POST)
    public ResponseEntity<?> update(@PathVariable int competenceId, @RequestBody CompetenceDto competenceDto) {
        User user = userService.getUserEntity(securityService.getUserDetails());
        Competence oldCompetence = competenceService.get(competenceId);

        if (!competenceService.canUpdateCompetence(user, oldCompetence)) {
            return ResponseEntity.badRequest().body("You have no permissions to update competence");
        }

        Competence competence = competenceService.update(competenceId, competenceMapper.fromDto(competenceDto));
        return ResponseEntity.ok(competenceMapper.toDto(competence));
    }
}
