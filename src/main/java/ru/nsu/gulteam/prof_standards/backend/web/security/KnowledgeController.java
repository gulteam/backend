package ru.nsu.gulteam.prof_standards.backend.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Knowledge;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Skills;
import ru.nsu.gulteam.prof_standards.backend.service.KnowledgeService;
import ru.nsu.gulteam.prof_standards.backend.service.SkillsService;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.KnowledgeMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.SkillsMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1")
public class KnowledgeController {
    private KnowledgeMapper knowledgeMapper;
    private KnowledgeService knowledgeService;

    @Autowired
    public KnowledgeController(KnowledgeMapper knowledgeMapper, KnowledgeService knowledgeService) {
        this.knowledgeMapper = knowledgeMapper;
        this.knowledgeService = knowledgeService;
    }

    @RequestMapping(path = "allKnowledge", method = RequestMethod.GET)
    public ResponseEntity<?> allKnowledge() {
        List<Knowledge> knowledges = knowledgeService.getAllKnowledge();
        return ResponseEntity.ok(knowledges.stream().map(knowledgeMapper::toDto).collect(Collectors.toList()));
    }
}
