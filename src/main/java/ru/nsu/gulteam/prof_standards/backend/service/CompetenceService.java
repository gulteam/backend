package ru.nsu.gulteam.prof_standards.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Competence;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Department;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Faculty;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.CompetenceRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.DepartmentRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.FacultyRepository;
import ru.nsu.gulteam.prof_standards.backend.exception.IncorrectIdentifierException;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.FacultyMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompetenceService {
    private final CompetenceRepository competenceRepository;

    public Competence get(long competenceId) {
        Competence competence = competenceRepository.findOne(competenceId);
        if (competence == null) {
            throw new IncorrectIdentifierException("There is no competence with id: " + competenceId);
        }
        return competence;
    }

    public void delete(long competenceId) {
        Competence competence = competenceRepository.findOne(competenceId);
        if (competence == null) {
            throw new IncorrectIdentifierException("There is no competence with id: " + competenceId);
        }
        competenceRepository.deleteReferences(competence);
        competenceRepository.delete(competence);
    }

    public Competence update(long competenceId, Competence competence) {
        competence.setId(competenceId);
        return competenceRepository.save(competence);
    }
}
