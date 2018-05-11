package ru.nsu.gulteam.prof_standards.backend.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.gulteam.prof_standards.backend.domain.node.FgosCourseRequirement;
import ru.nsu.gulteam.prof_standards.backend.domain.node.User;
import ru.nsu.gulteam.prof_standards.backend.service.FgosCourseRequirementService;
import ru.nsu.gulteam.prof_standards.backend.service.SecurityService;
import ru.nsu.gulteam.prof_standards.backend.service.UserService;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.FgosCourseRequirementMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.FgosCourseRequirementDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.Message;

@RestController
@RequestMapping(path = "api/v1/courseRequirement")
@RequiredArgsConstructor
public class FgosCourseRequirementController {
    private final SecurityService securityService;
    private final UserService userService;
    private final FgosCourseRequirementService fgosCourseRequirementService;
    private final FgosCourseRequirementMapper fgosCourseRequirementMapper;

    @RequestMapping(path = "{courseRequirement}", method = RequestMethod.GET)
    public ResponseEntity<?> get(@PathVariable int courseRequirement) {
        User user = userService.getUserEntity(securityService.getUserDetails());
        FgosCourseRequirement fgosCourseRequirement = fgosCourseRequirementService.get(courseRequirement);

        if (!fgosCourseRequirementService.canReadFgosCourseRequirement(user, fgosCourseRequirement)) {
            return ResponseEntity.badRequest().body("You have no permissions to read fgos course requirement");
        }

        return ResponseEntity.ok(fgosCourseRequirementMapper.toDto(fgosCourseRequirement));
    }

    @RequestMapping(path = "{courseRequirement}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable int courseRequirement) {
        User user = userService.getUserEntity(securityService.getUserDetails());
        FgosCourseRequirement fgosCourseRequirement = fgosCourseRequirementService.get(courseRequirement);

        if (!fgosCourseRequirementService.canDeleteFgosCourseRequirement(user, fgosCourseRequirement)) {
            return ResponseEntity.badRequest().body("You have no permissions to delete fgos course requirement");
        }

        fgosCourseRequirementService.delete(fgosCourseRequirement);
        return ResponseEntity.ok(new Message("FgosCourseRequirement successfully deleted"));
    }

    @RequestMapping(path = "{courseRequirement}", method = RequestMethod.POST)
    public ResponseEntity<?> update(@PathVariable int courseRequirement, @RequestBody FgosCourseRequirementDto fgosCourseRequirementDto) {
        User user = userService.getUserEntity(securityService.getUserDetails());
        FgosCourseRequirement oldFgosCourseRequirement = fgosCourseRequirementService.get(courseRequirement);

        if (!fgosCourseRequirementService.canUpdateFgosCourseRequirement(user, oldFgosCourseRequirement)) {
            return ResponseEntity.badRequest().body("You have no permissions to update fgos course requirement");
        }

        FgosCourseRequirement fgosCourseRequirement = fgosCourseRequirementService.update(courseRequirement, fgosCourseRequirementMapper.fromDto(fgosCourseRequirementDto));
        return ResponseEntity.ok(fgosCourseRequirementMapper.toDto(fgosCourseRequirement));
    }
}
