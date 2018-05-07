package ru.nsu.gulteam.prof_standards.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Competence;
import ru.nsu.gulteam.prof_standards.backend.domain.node.FgosCourseRequirement;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.CompetenceRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.FgosCourseRequirementRepository;
import ru.nsu.gulteam.prof_standards.backend.exception.IncorrectIdentifierException;

@Service
@RequiredArgsConstructor
public class FgosCourseRequirementService {
    private final FgosCourseRequirementRepository fgosCourseRequirementRepository;

    public FgosCourseRequirement get(long fgosCourseRequirementId) {
        FgosCourseRequirement fgosCourseRequirement = fgosCourseRequirementRepository.findOne(fgosCourseRequirementId);
        if (fgosCourseRequirement == null) {
            throw new IncorrectIdentifierException("There is no fgosCourseRequirement with id: " + fgosCourseRequirementId);
        }
        return fgosCourseRequirement;
    }

    public void delete(long fgosCourseRequirementId) {
        FgosCourseRequirement fgosCourseRequirement = fgosCourseRequirementRepository.findOne(fgosCourseRequirementId);
        if (fgosCourseRequirement == null) {
            throw new IncorrectIdentifierException("There is no fgosCourseRequirement with id: " + fgosCourseRequirementId);
        }
        fgosCourseRequirementRepository.deleteReferences(fgosCourseRequirement);
        fgosCourseRequirementRepository.delete(fgosCourseRequirement);
    }

    public FgosCourseRequirement update(long fgosCourseRequirementId, FgosCourseRequirement fgosCourseRequirement) {
        fgosCourseRequirement.setId(fgosCourseRequirementId);
        return fgosCourseRequirementRepository.save(fgosCourseRequirement);
    }
}
