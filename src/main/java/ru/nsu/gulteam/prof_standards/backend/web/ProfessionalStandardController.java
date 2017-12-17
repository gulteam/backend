package ru.nsu.gulteam.prof_standards.backend.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.gulteam.prof_standards.backend.domain.node.BasicEducationProgram;
import ru.nsu.gulteam.prof_standards.backend.domain.node.ProfessionalStandard;
import ru.nsu.gulteam.prof_standards.backend.entity.FullCourseInfo;
import ru.nsu.gulteam.prof_standards.backend.service.ProfessionalStandardService;
import ru.nsu.gulteam.prof_standards.backend.service.ProgramService;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.CourseMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.ProfessionalStandardMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.ProgramMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.BasicEducationProgramDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.Message;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1/professionalStandard")
public class ProfessionalStandardController {
    private ProfessionalStandardService professionalStandardService;
    private ProfessionalStandardMapper professionalStandardMapper;

    public ProfessionalStandardController(ProfessionalStandardService professionalStandardService, ProfessionalStandardMapper professionalStandardMapper) {
        this.professionalStandardService = professionalStandardService;
        this.professionalStandardMapper = professionalStandardMapper;
    }

    @RequestMapping(path = "allStandards", method = RequestMethod.GET)
    public ResponseEntity<?> allStandards() {
        List<ProfessionalStandard> programs = professionalStandardService.getAllStandards();
        return ResponseEntity.ok(programs.stream().map(professionalStandardMapper::toDto).collect(Collectors.toList()));
    }
}
