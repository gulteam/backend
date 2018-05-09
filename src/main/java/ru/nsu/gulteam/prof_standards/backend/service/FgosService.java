package ru.nsu.gulteam.prof_standards.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Competence;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Fgos;
import ru.nsu.gulteam.prof_standards.backend.domain.node.FgosCourseRequirement;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.CompetenceRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.FgosCourseRequirementRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.FgosRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.ProfessionalStandardRepository;
import ru.nsu.gulteam.prof_standards.backend.exception.IncorrectIdentifierException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FgosService {
    private final FgosRepository fgosRepository;
    private final CompetenceRepository competenceRepository;
    private final FgosCourseRequirementRepository fgosCourseRequirementRepository;
    private final ProfessionalStandardRepository professionalStandardRepository;

    public List<Fgos> getAll() {
        List<Fgos> result = new ArrayList<>();

        for (Fgos fgos : fgosRepository.findAll()) {
            result.add(fgos);
        }

        return result;
    }

    public Fgos get(long fgosId) throws IncorrectIdentifierException {
        Fgos fgos = fgosRepository.findOne(fgosId);
        if (fgos == null) {
            throw new IncorrectIdentifierException("There is no fgos with id: " + fgosId);
        }
        return fgos;
    }

    public void delete(long fgosId) {
        Fgos fgos = fgosRepository.findOne(fgosId);
        if (fgos == null) {
            throw new IncorrectIdentifierException("There is no fgos with id: " + fgosId);
        }
        fgosRepository.delete(fgos);
    }

    public Fgos updateFgos(long fgosId, Fgos fgos) {
        Fgos oldFgos = fgosRepository.findOne(fgosId);

        oldFgos.setName(fgos.getName());
        oldFgos.setCode(fgos.getCode());

        oldFgos.setDisciplineVolumeFrom(fgos.getDisciplineVolumeFrom());
        oldFgos.setAttestationVolumeFrom(fgos.getAttestationVolumeFrom());
        oldFgos.setPracticeVolumeFrom(fgos.getPracticeVolumeFrom());
        oldFgos.setSummaryVolume(fgos.getSummaryVolume());

        oldFgos.getProfessionalStandards().clear();
        fgos.getProfessionalStandards().forEach(professionalStandard -> {
            oldFgos.getProfessionalStandards().add(professionalStandardRepository.findOne(professionalStandard.getId()));
        });

        return fgosRepository.save(oldFgos);
    }

    public Fgos create() {
        return fgosRepository.save(new Fgos());
    }

    public void addCompetence(long fgosId) {
        Fgos fgos = fgosRepository.findOne(fgosId);
        fgos.getRequireCompetence().add(competenceRepository.save(new Competence()));
        fgosRepository.save(fgos);
    }

    public void addReuiredCourse(long fgosId) {
        Fgos fgos = fgosRepository.findOne(fgosId);
        fgos.getRequireCourses().add(fgosCourseRequirementRepository.save(new FgosCourseRequirement()));
        fgosRepository.save(fgos);
    }
}
