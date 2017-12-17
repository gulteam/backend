package ru.nsu.gulteam.prof_standards.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.ProfessionalStandard;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.ProfessionalStandardRepository;

import java.util.List;

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

}
