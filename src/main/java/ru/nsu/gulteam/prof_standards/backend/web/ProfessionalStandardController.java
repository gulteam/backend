package ru.nsu.gulteam.prof_standards.backend.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.gulteam.prof_standards.backend.domain.node.BasicEducationProgram;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Knowledge;
import ru.nsu.gulteam.prof_standards.backend.domain.node.ProfessionalStandard;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Skills;
import ru.nsu.gulteam.prof_standards.backend.entity.FullCourseInfo;
import ru.nsu.gulteam.prof_standards.backend.entity.ProfSearchRequest;
import ru.nsu.gulteam.prof_standards.backend.service.KnowledgeService;
import ru.nsu.gulteam.prof_standards.backend.service.ProfessionalStandardService;
import ru.nsu.gulteam.prof_standards.backend.service.ProgramService;
import ru.nsu.gulteam.prof_standards.backend.service.SkillsService;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.*;
import ru.nsu.gulteam.prof_standards.backend.web.dto.request.ProfSearchParameters;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.BasicEducationProgramDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.Message;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1/professionalStandard")
public class ProfessionalStandardController {
    private ProfessionalStandardService professionalStandardService;
    private ProfessionalStandardMapper professionalStandardMapper;
    private SkillsService skillsService;
    private SkillsMapper skillsMapper;
    private KnowledgeService knowledgeService;
    private KnowledgeMapper knowledgeMapper;
    private ProfSearchParametersMapper profSearchParametersMapper;

    public ProfessionalStandardController(ProfessionalStandardService professionalStandardService, ProfessionalStandardMapper professionalStandardMapper,
                                          SkillsService skillsService, SkillsMapper skillsMapper,
                                          KnowledgeService knowledgeService, KnowledgeMapper knowledgeMapper, ProfSearchParametersMapper profSearchParametersMapper) {
        this.professionalStandardService = professionalStandardService;
        this.professionalStandardMapper = professionalStandardMapper;
        this.skillsService = skillsService;
        this.skillsMapper = skillsMapper;
        this.knowledgeService = knowledgeService;
        this.knowledgeMapper = knowledgeMapper;
        this.profSearchParametersMapper = profSearchParametersMapper;
    }

    @RequestMapping(path = "allStandards", method = RequestMethod.GET)
    public ResponseEntity<?> allStandards() {
        List<ProfessionalStandard> programs = professionalStandardService.getAllStandards();
        return ResponseEntity.ok(programs.stream().map(professionalStandardMapper::toDto).collect(Collectors.toList()));
    }

    @RequestMapping(path = "{professionalStandardId}", method = RequestMethod.GET)
    public  ResponseEntity<?> getProfessionalStandard(@PathVariable Long professionalStandardId){
        ProfessionalStandard professionalStandard = professionalStandardService.getProfessinalStandard(professionalStandardId);
        return ResponseEntity.ok(professionalStandardMapper.toDto(professionalStandard));
    }

    @RequestMapping(path = "{professionalStandardId}/allSkills", method = RequestMethod.GET)
    public  ResponseEntity<?> getSkillsByProfessionalStandardId(@PathVariable Long professionalStandardId){
        ProfessionalStandard professionalStandard = professionalStandardService.getProfessinalStandard(professionalStandardId);
        Set<Skills> skills = skillsService.getRequiredForStandard(professionalStandard);
        return ResponseEntity.ok(skills.stream().map(skillsMapper::toDto).collect(Collectors.toList()));
    }

    @RequestMapping(path = "{professionalStandardId}/allKnowledges", method = RequestMethod.GET)
    public ResponseEntity<?> getKnowledgesByProfessionalStandardId(@PathVariable Long professionalStandardId){
        ProfessionalStandard professionalStandard = professionalStandardService.getProfessinalStandard(professionalStandardId);
        Set<Knowledge> knowledges = knowledgeService.getRequiredForStandard(professionalStandard);
        return ResponseEntity.ok(knowledges.stream().map(knowledgeMapper::toDto).collect(Collectors.toList()));
    }

    @RequestMapping(path = "search", method = RequestMethod.POST)
    public ResponseEntity<?> searchByKnowledgeAndSkills(@RequestBody ProfSearchParameters profSearchParameters){
        List<ProfessionalStandard> professionalStandards = professionalStandardService.searchByKnowledgeAndSkills(profSearchParametersMapper.fromDto(profSearchParameters));
        return ResponseEntity.ok(professionalStandards.stream().map(professionalStandardMapper::toDto).collect(Collectors.toList()));
    }
}
