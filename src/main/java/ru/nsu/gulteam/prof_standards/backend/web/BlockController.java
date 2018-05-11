package ru.nsu.gulteam.prof_standards.backend.web;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.gulteam.prof_standards.backend.domain.node.BasicEducationProgram;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Block;
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
        BasicEducationProgram program = programService.getProgram(programId);

        if (!programService.canAddVariableCourse(user, program)) {
            return ResponseEntity.badRequest().body("You have no permissions to add variable course");
        }

        FullCourseInfo course = blockService.addCourseToBlock(user, programId);
        return ResponseEntity.ok(courseMapper.toDto(course));
    }

    @RequestMapping(path = "{blockId}", method = RequestMethod.GET)
    public ResponseEntity<?> get(@PathVariable int blockId) {
        User user = userService.getUserEntity(securityService.getUserDetails());
        Block rawBlock = blockService.getBlock(blockId);

        if(!blockService.canReadBlock(user, rawBlock)){
            return ResponseEntity.badRequest().body("You have no permissions to read block");
        }

        FullBlockInfo block = blockService.getFullBlockInfo(user, rawBlock);
        BlockDto blockDto = blockMapper.toDto(block);
        return ResponseEntity.ok(blockDto);
    }

    @RequestMapping(path = "{blockId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable int blockId) {
        User user = userService.getUserEntity(securityService.getUserDetails());
        Block block = blockService.getBlock(blockId);

        if(!blockService.canDeleteBlock(user, block)){
            return ResponseEntity.badRequest().body("You have no permissions to delete block");
        }

        blockService.deleteBlock(block);
        return ResponseEntity.ok(new Message("Course successfully deleted"));
    }

    @RequestMapping(path = "{blockId}", method = RequestMethod.POST)
    public ResponseEntity<?> update(@PathVariable int blockId, @RequestBody BlockDto blockDto) {
        User user = userService.getUserEntity(securityService.getUserDetails());
        Block oldBlock = blockService.getBlock(blockId);

        if(!blockService.canUpdateBlock(user, oldBlock)){
            return ResponseEntity.badRequest().body("You have no permissions to update block");
        }

        FullBlockInfo block = blockService.updateBlock(user, blockId, blockDto);
        return ResponseEntity.ok(blockMapper.toDto(block));
    }
}
