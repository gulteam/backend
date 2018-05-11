package ru.nsu.gulteam.prof_standards.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.*;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.CompetenceRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.DepartmentRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.FacultyRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.type.UserRole;
import ru.nsu.gulteam.prof_standards.backend.entity.FullUserInfo;
import ru.nsu.gulteam.prof_standards.backend.exception.IncorrectIdentifierException;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.FacultyMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompetenceService {
    private final CompetenceRepository competenceRepository;
    private final FgosService fgosService;

    public Competence get(long competenceId) {
        Competence competence = competenceRepository.findOne(competenceId);
        if (competence == null) {
            throw new IncorrectIdentifierException("There is no competence with id: " + competenceId);
        }
        return competence;
    }

    public void delete(Competence competence) {
        competenceRepository.deleteReferences(competence);
        competenceRepository.delete(competence);
    }

    public Competence update(long competenceId, Competence competence) {
        competence.setId(competenceId);
        return competenceRepository.save(competence);
    }

    private Fgos getFgos(Competence competence) {
        return competenceRepository.getFgos(competence);
    }

    // CRUD Permissions //--------------------------------------------------------------------------------------------//

    public boolean canReadCompetence(User user, Competence competence) {
        return true;
    }

    public boolean canUpdateCompetence(User user, Competence competence) {
        return fgosService.canUpdateFgos(user, getFgos(competence));
    }

    public boolean canDeleteCompetence(User user, Competence competence) {
        return fgosService.canUpdateFgos(user, getFgos(competence));
    }
}
