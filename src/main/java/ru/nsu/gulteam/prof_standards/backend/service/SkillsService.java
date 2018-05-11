package ru.nsu.gulteam.prof_standards.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.BasicEducationProgram;
import ru.nsu.gulteam.prof_standards.backend.domain.node.ProfessionalStandard;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Role;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Skills;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.BasicEducationProgramRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.RoleRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.SkillsRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SkillsService {
    private SkillsRepository skillsRepository;
    private BasicEducationProgramRepository basicEducationProgramRepository;

    @Autowired
    public SkillsService(SkillsRepository skillsRepository, BasicEducationProgramRepository basicEducationProgramRepository) {
        this.skillsRepository = skillsRepository;
        this.basicEducationProgramRepository = basicEducationProgramRepository;
    }

    public List<Skills> getAllSkills(){
        return skillsRepository.findAll();
    }

    public Set<Skills> getRequiredForStandard(ProfessionalStandard professionalStandard){
        return skillsRepository.getRequiredForStandard(professionalStandard);
    }

    public Set<Skills> getNotInEducationForStandard(ProfessionalStandard professionalStandard){
        List<BasicEducationProgram> basicEducationPrograms = basicEducationProgramRepository.findAll();
        Set<Skills> skillsByProgram = new HashSet<>();
        for (BasicEducationProgram basicEducationProgram: basicEducationPrograms) {
            skillsByProgram.addAll(skillsRepository.getDevelopByBasicEducationProgram(basicEducationProgram));
        }

        Set<Skills> skillsStandard = getRequiredForStandard(professionalStandard);

        skillsStandard.removeAll(skillsByProgram);
        return skillsStandard;
    }
}
