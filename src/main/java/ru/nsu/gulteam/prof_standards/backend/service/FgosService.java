package ru.nsu.gulteam.prof_standards.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.*;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.CompetenceRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.FgosCourseRequirementRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.FgosRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.ProfessionalStandardRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.type.UserRole;
import ru.nsu.gulteam.prof_standards.backend.entity.FullFgosInfo;
import ru.nsu.gulteam.prof_standards.backend.entity.FullUserInfo;
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
    private final UserService userService;

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

    public void delete(Fgos fgos) {
        fgosRepository.delete(fgos);
    }

    public Fgos updateFgos(Fgos oldFgos, Fgos fgos) {
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

    public Fgos create(User creator) {
        return fgosRepository.save(new Fgos("", "", creator));
    }

    public void addCompetence(Fgos fgos) {
        fgos.getRequireCompetence().add(competenceRepository.save(new Competence()));
        fgosRepository.save(fgos);
    }

    public void addReuiredCourse(Fgos fgos) {
        fgos.getRequireCourses().add(fgosCourseRequirementRepository.save(new FgosCourseRequirement()));
        fgosRepository.save(fgos);
    }

    public FullFgosInfo getFullInfo(User user, Fgos fgos){
        return new FullFgosInfo(fgos,
                canUpdateFgos(user, fgos),
                canDeleteFgos(user, fgos),
                userService.getFullUserInfo(fgos.getCreator()));
    }

    // CRUD Permissions //--------------------------------------------------------------------------------------------//

    public boolean canReadFgosList(User user) {
        return true;
    }

    public boolean canReadFgos(User user, Fgos fgos) {
        return true;
    }

    public boolean canCreateFgos(User user) {
        if (user == null) {
            return false;
        }

        FullUserInfo fullUserInfo = userService.getFullUserInfo(user);

        UserRole role = fullUserInfo.getRole().getName();

        return role.equals(UserRole.ADMINISTRATOR) ||
                (role.equals(UserRole.DEAN_MEMBER));
    }

    public boolean canUpdateFgos(User user, Fgos fgos) {
        if (user == null) {
            return false;
        }

        FullUserInfo fullUserInfo = userService.getFullUserInfo(user);

        UserRole role = fullUserInfo.getRole().getName();

        return role.equals(UserRole.ADMINISTRATOR) ||
                (role.equals(UserRole.DEAN_MEMBER));
    }

    public boolean canDeleteFgos(User user, Fgos fgos) {
        if (user == null) {
            return false;
        }

        FullUserInfo fullUserInfo = userService.getFullUserInfo(user);

        UserRole role = fullUserInfo.getRole().getName();

        return role.equals(UserRole.ADMINISTRATOR) ||
                (role.equals(UserRole.DEAN_MEMBER) && user.getId().equals(fgos.getCreator().getId()));
    }
}
