package ru.nsu.gulteam.prof_standards.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.BasicEducationProgram;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Knowledge;
import ru.nsu.gulteam.prof_standards.backend.domain.node.ProfessionalStandard;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Skills;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.BasicEducationProgramRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.KnowledgeRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.ProfessionalStandardRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.SkillsRepository;
import ru.nsu.gulteam.prof_standards.backend.entity.ProfSearchRequest;
import ru.nsu.gulteam.prof_standards.backend.entity.ProfessionalStandardStatus;
import sun.rmi.runtime.Log;

import java.util.*;
import java.util.logging.Logger;

@Service
public class ProfessionalStandardService {
    private ProfessionalStandardRepository professionalStandardRepository;
    private BasicEducationProgramRepository basicEducationProgramRepository;
    private SkillsRepository skillsRepository;
    private KnowledgeRepository knowledgeRepository;

    @Autowired
    public ProfessionalStandardService(ProfessionalStandardRepository professionalStandardRepository, BasicEducationProgramRepository basicEducationProgramRepository,
                                       SkillsRepository skillsRepository, KnowledgeRepository knowledgeRepository) {
        this.professionalStandardRepository = professionalStandardRepository;
        this.basicEducationProgramRepository = basicEducationProgramRepository;
        this.knowledgeRepository = knowledgeRepository;
        this.skillsRepository = skillsRepository;
    }

    public List<ProfessionalStandard> getAllStandards() {
        return professionalStandardRepository.findAll();
    }

    public ProfessionalStandard getProfessinalStandard(Long professionalStandardId){
        return professionalStandardRepository.findById(professionalStandardId);
    }

    public List<ProfessionalStandard> searchByKnowledgeAndSkills(ProfSearchRequest profSearchRequest){
        Map<Long, Integer> professionalStandardIntegerMap = new TreeMap<>();
        Integer totalSize = profSearchRequest.getIncludeKnowledges().size() + profSearchRequest.getIncludeSkills().size();
        List<ProfessionalStandard> result = new LinkedList<>();
        for(Skills skills: profSearchRequest.getIncludeSkills()){
            List<ProfessionalStandard> professionalStandards = professionalStandardRepository.findBySkills(skills);
            for (ProfessionalStandard professionalStandard: professionalStandards){
                Integer count = professionalStandardIntegerMap.get(professionalStandard.getId());
                if (count != null) {
                    professionalStandardIntegerMap.put(professionalStandard.getId(), ++count);
                } else  {
                    professionalStandardIntegerMap.put(professionalStandard.getId(), 1);
                }
            }
        }
        for(Knowledge knowledge: profSearchRequest.getIncludeKnowledges()){
            List<ProfessionalStandard> professionalStandards = professionalStandardRepository.findByKnowledge(knowledge);
            for (ProfessionalStandard professionalStandard: professionalStandards){
                Integer count = professionalStandardIntegerMap.get(professionalStandard.getId());
                if (count != null) {
                    professionalStandardIntegerMap.put(professionalStandard.getId(), ++count);
                } else  {
                    professionalStandardIntegerMap.put(professionalStandard.getId(), 1);
                }
            }
        }
        professionalStandardIntegerMap.forEach((key, value) -> {
            if (value.equals(totalSize)) {
                result.add(getProfessinalStandard(key));
            }
        });
        return result;
    }

    public List<ProfessionalStandardStatus> getAllProfessionalStandardStatus(){
        List<BasicEducationProgram> basicEducationPrograms = basicEducationProgramRepository.findAll();
        Set<Skills> skillsByProgram = new HashSet<>();
        Set<Knowledge> knowledgesByProgram = new HashSet<>();
        for (BasicEducationProgram basicEducationProgram: basicEducationPrograms){
            skillsByProgram.addAll(skillsRepository.getDevelopByBasicEducationProgram(basicEducationProgram));
            knowledgesByProgram.addAll(knowledgeRepository.getDevelopByBasicEducationProgram(basicEducationProgram));
        }
        List<ProfessionalStandardStatus> professionalStandardStatuses = new LinkedList<>();
        for (ProfessionalStandard professionalStandard: professionalStandardRepository.findAll()){
            ProfessionalStandardStatus professionalStandardStatus = new ProfessionalStandardStatus();
            professionalStandardStatus.setProfessionalStandard(professionalStandard);
            Set<Skills> skillsStandard = skillsRepository.getRequiredForStandard(professionalStandard);
            Set<Knowledge> knowledgesStandard = knowledgeRepository.getRequiredForStandard(professionalStandard);
            for (Skills skills: skillsStandard){
                if (!skillsByProgram.contains(skills)){
                    professionalStandardStatus.setStatus("NO");
                    break;
                }
            }
            if (professionalStandardStatus.getStatus() == null) {
                for (Knowledge knowledge : knowledgesStandard) {
                    if (!knowledgesByProgram.contains(knowledge)) {
                        professionalStandardStatus.setStatus("NO");
                    }
                }
            }
            if (professionalStandardStatus.getStatus() == null) {
                professionalStandardStatus.setStatus("YES");
            }
            professionalStandardStatuses.add(professionalStandardStatus);
        }
        return professionalStandardStatuses;
    }
}
