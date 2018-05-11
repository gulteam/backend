package ru.nsu.gulteam.prof_standards.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.BasicEducationProgram;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Knowledge;
import ru.nsu.gulteam.prof_standards.backend.domain.node.ProfessionalStandard;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Skills;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.BasicEducationProgramRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.KnowledgeRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class KnowledgeService {
    private KnowledgeRepository knowledgeRepository;
    private BasicEducationProgramRepository basicEducationProgramRepository;

    @Autowired
    public KnowledgeService(KnowledgeRepository knowledgeRepository, BasicEducationProgramRepository basicEducationProgramRepository) {
        this.knowledgeRepository = knowledgeRepository;
        this.basicEducationProgramRepository = basicEducationProgramRepository;
    }

    public List<Knowledge> getAllKnowledge(){
        return knowledgeRepository.findAll();
    }

    public Set<Knowledge> getRequiredForStandard(ProfessionalStandard professionalStandard){
        return knowledgeRepository.getRequiredForStandard(professionalStandard);
    }

    public Set<Knowledge> getNotInEducationForStandard(ProfessionalStandard professionalStandard){
        List<BasicEducationProgram> basicEducationPrograms = basicEducationProgramRepository.findAll();
        Set<Knowledge> knowledgesByProgram = new HashSet<>();
        for (BasicEducationProgram basicEducationProgram: basicEducationPrograms) {
            knowledgesByProgram.addAll(knowledgeRepository.getDevelopByBasicEducationProgram(basicEducationProgram));
        }

        Set<Knowledge> knowledgesStandard = getRequiredForStandard(professionalStandard);

        knowledgesStandard.removeAll(knowledgesByProgram);
        return knowledgesStandard;
    }
}
