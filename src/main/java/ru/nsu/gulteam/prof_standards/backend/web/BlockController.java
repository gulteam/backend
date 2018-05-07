package ru.nsu.gulteam.prof_standards.backend.web;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.gulteam.prof_standards.backend.domain.node.User;
import ru.nsu.gulteam.prof_standards.backend.entity.FullBlockInfo;
import ru.nsu.gulteam.prof_standards.backend.entity.FullCourseInfo;
import ru.nsu.gulteam.prof_standards.backend.service.BlockService;
import ru.nsu.gulteam.prof_standards.backend.service.ProgramService;
import ru.nsu.gulteam.prof_standards.backend.service.SecurityService;
import ru.nsu.gulteam.prof_standards.backend.service.UserService;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.BlockMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.CourseMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.BlockDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.Message;

@RestController
@RequestMapping(path = "api/v1/block")
@AllArgsConstructor
public class BlockController {
    private UserService userService;
    private SecurityService securityService;
    private ProgramService programService;
    private CourseMapper courseMapper;
    private BlockService blockService;
    private BlockMapper blockMapper;

    @RequestMapping(path = "{programId}/addCourse", method = RequestMethod.GET)
    public ResponseEntity<?> addCourse(@PathVariable int programId) {
        User user = userService.getUserEntity(securityService.getUserDetails());

        if (user == null) {
            throw new RuntimeException("Cann't create course without user");
        }


        FullCourseInfo course = blockService.addCourseToBlock(user, programId);
        return ResponseEntity.ok(courseMapper.toDto(course));
    }

    @RequestMapping(path = "{blockId}", method = RequestMethod.GET)
    public ResponseEntity<?> get(@PathVariable int blockId) {
        User user = userService.getUserEntity(securityService.getUserDetails());

        FullBlockInfo block = blockService.getFullBlockInfo(user, blockService.getBlock(blockId));
        BlockDto blockDto = blockMapper.toDto(block);

        return ResponseEntity.ok(blockDto);
    }

    @RequestMapping(path = "{blockId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable int blockId) {
        User user = userService.getUserEntity(securityService.getUserDetails());


        blockService.deleteBlock(blockId);
        return ResponseEntity.ok(new Message("Course successfully deleted"));
    }

    @RequestMapping(path = "{blockId}", method = RequestMethod.POST)
    public ResponseEntity<?> update(@PathVariable int blockId, @RequestBody BlockDto blockDto) {
        User user = userService.getUserEntity(securityService.getUserDetails());

        FullBlockInfo block = blockService.updateBlock(user, blockId, blockDto);
        return ResponseEntity.ok(blockMapper.toDto(block));
    }
}
