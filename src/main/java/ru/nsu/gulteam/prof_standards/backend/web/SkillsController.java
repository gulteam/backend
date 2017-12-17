package ru.nsu.gulteam.prof_standards.backend.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Skills;
import ru.nsu.gulteam.prof_standards.backend.service.SkillsService;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.SkillsMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1")
public class SkillsController {
    private SkillsMapper skillsMapper;
    private SkillsService skillsService;

    @Autowired
    public SkillsController(SkillsMapper skillsMapper, SkillsService skillsService) {
        this.skillsMapper = skillsMapper;
        this.skillsService = skillsService;
    }

    @RequestMapping(path = "allSkills", method = RequestMethod.GET)
    public ResponseEntity<?> allSkills() {
        List<Skills> skills = skillsService.getAllSkills();
        return ResponseEntity.ok(skills.stream().map(skillsMapper::toDto).collect(Collectors.toList()));
    }
}
