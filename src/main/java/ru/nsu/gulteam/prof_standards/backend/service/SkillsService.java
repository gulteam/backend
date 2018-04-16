package ru.nsu.gulteam.prof_standards.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.ProfessionalStandard;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Role;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Skills;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.RoleRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.SkillsRepository;

import java.util.List;
import java.util.Set;

@Service
public class SkillsService {
    private SkillsRepository skillsRepository;

    @Autowired
    public SkillsService(SkillsRepository skillsRepository) {
        this.skillsRepository = skillsRepository;
    }

    public List<Skills> getAllSkills(){
        return skillsRepository.findAll();
    }

    public Set<Skills> getRequiredForStandard(ProfessionalStandard professionalStandard){
        return skillsRepository.getRequiredForStandard(professionalStandard);
    }
}
