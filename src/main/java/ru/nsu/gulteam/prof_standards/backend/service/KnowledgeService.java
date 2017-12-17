package ru.nsu.gulteam.prof_standards.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Knowledge;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Skills;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.KnowledgeRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.SkillsRepository;

import java.util.List;

@Service
public class KnowledgeService {
    private KnowledgeRepository knowledgeRepository;

    @Autowired
    public KnowledgeService(KnowledgeRepository knowledgeRepository) {
        this.knowledgeRepository = knowledgeRepository;
    }

    public List<Knowledge> getAllKnowledge(){
        return knowledgeRepository.findAll();
    }
}
