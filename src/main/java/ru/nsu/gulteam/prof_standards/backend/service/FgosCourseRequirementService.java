package ru.nsu.gulteam.prof_standards.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Competence;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Fgos;
import ru.nsu.gulteam.prof_standards.backend.domain.node.FgosCourseRequirement;
import ru.nsu.gulteam.prof_standards.backend.domain.node.User;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.CompetenceRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.FgosCourseRequirementRepository;
import ru.nsu.gulteam.prof_standards.backend.exception.IncorrectIdentifierException;

@Service
@RequiredArgsConstructor
public class FgosCourseRequirementService {
    private final FgosCourseRequirementRepository fgosCourseRequirementRepository;
    private final FgosService fgosService;

    public FgosCourseRequirement get(long fgosCourseRequirementId) {
        FgosCourseRequirement fgosCourseRequirement = fgosCourseRequirementRepository.findOne(fgosCourseRequirementId);
        if (fgosCourseRequirement == null) {
            throw new IncorrectIdentifierException("There is no fgosCourseRequirement with id: " + fgosCourseRequirementId);
        }
        return fgosCourseRequirement;
    }

    public void delete(FgosCourseRequirement fgosCourseRequirement) {
        fgosCourseRequirementRepository.deleteReferences(fgosCourseRequirement);
        fgosCourseRequirementRepository.delete(fgosCourseRequirement);
    }

    public FgosCourseRequirement update(long fgosCourseRequirementId, FgosCourseRequirement fgosCourseRequirement) {
        fgosCourseRequirement.setId(fgosCourseRequirementId);
        return fgosCourseRequirementRepository.save(fgosCourseRequirement);
    }

    private Fgos getFgos(FgosCourseRequirement fgosCourseRequirement) {
        return fgosCourseRequirementRepository.getFgos(fgosCourseRequirement);
    }
    
    // CRUD Permissions //--------------------------------------------------------------------------------------------//

    public boolean canReadFgosCourseRequirement(User user, FgosCourseRequirement fgosCourseRequirement) {
        return true;
    }

    public boolean canUpdateFgosCourseRequirement(User user, FgosCourseRequirement fgosCourseRequirement) {
        return fgosService.canUpdateFgos(user, getFgos(fgosCourseRequirement));
    }

    public boolean canDeleteFgosCourseRequirement(User user, FgosCourseRequirement fgosCourseRequirement) {
        return fgosService.canUpdateFgos(user, getFgos(fgosCourseRequirement));
    }
}
