package ru.nsu.gulteam.prof_standards.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Knowledge;
import ru.nsu.gulteam.prof_standards.backend.domain.node.ProfessionalStandard;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Skills;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.ProfessionalStandardRepository;
import ru.nsu.gulteam.prof_standards.backend.entity.ProfSearchRequest;
import sun.rmi.runtime.Log;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

@Service
public class ProfessionalStandardService {
    private ProfessionalStandardRepository professionalStandardRepository;

    @Autowired
    public ProfessionalStandardService(ProfessionalStandardRepository professionalStandardRepository) {
        this.professionalStandardRepository = professionalStandardRepository;
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
}
