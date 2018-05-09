package ru.nsu.gulteam.prof_standards.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.BasicEducationProgram;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Block;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Course;
import ru.nsu.gulteam.prof_standards.backend.domain.node.User;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.BasicEducationProgramRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.CourseRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.BlockRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.type.UserRole;
import ru.nsu.gulteam.prof_standards.backend.entity.FullCourseInfo;
import ru.nsu.gulteam.prof_standards.backend.entity.FullBlockInfo;
import ru.nsu.gulteam.prof_standards.backend.entity.FullUserInfo;
import ru.nsu.gulteam.prof_standards.backend.exception.IncorrectIdentifierException;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.BlockDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlockService {
    private final BlockRepository blockRepository;
    private final UserService userService;
    private final BasicEducationProgramRepository programRepository;
    private final CourseService courseService;
    private final ProgramService programService;
    private final CourseRepository courseRepository;

    public FullBlockInfo getFullBlockInfo(User user, Block block) {
        FullBlockInfo fullBlockInfo = new FullBlockInfo();
        fullBlockInfo.setBlock(block);
        fullBlockInfo.setCanEdit(Block(user, block));
        return fullBlockInfo;
    }

    public Block getBlock(long courseId) throws IncorrectIdentifierException {
        Block course = blockRepository.findOne(courseId);
        if (course == null) {
            throw new IncorrectIdentifierException("There is no block with id: " + courseId);
        }
        return course;
    }

    public void deleteBlock(long blockId) {
        Block course = blockRepository.findOne(blockId);
        if (course == null) {
            throw new IncorrectIdentifierException("There is no block with id: " + blockId);
        }
        blockRepository.delete(course);
    }

    public FullBlockInfo updateBlock(User user, long blockId, BlockDto blockDto) {
        Block oldBlock = blockRepository.findOne(blockId);

        oldBlock.setAmount(blockDto.getAmount());
        oldBlock.setSemester(blockDto.getSemester());
        oldBlock.setAttestationForm(blockDto.getAttestationForm());

        Block savedBlock = blockRepository.save(oldBlock);
        updateAllCoursesFromBlock(savedBlock);

        return getFullBlockInfo(user, savedBlock);
    }

    public void updateAllCoursesFromBlock(Block block){
        List<Course> courses = courseRepository.getAllCoursesFromBlock(block);
        courses.forEach(course -> {
            course.setSemester(block.getSemester());
            course.setAmount(block.getAmount());
            course.setAttestationForm(block.getAttestationForm());

            courseRepository.save(course);
        });
    }

    public boolean canReadBlock(User user, Block block) {
        return true;
    }

    public boolean Block(User user, Block block) {
        if (user == null) {
            return false;
        }

        FullUserInfo fullUserInfo = userService.getFullUserInfo(user);
        UserRole role = fullUserInfo.getRole().getName();

        return role.equals(UserRole.ADMINISTRATOR) ||
                role.equals(UserRole.DEAN_MEMBER);
    }


    public FullBlockInfo getFullBlockInfo(Block block) {
        return getFullBlockInfo(null, block);
    }

    public FullCourseInfo addCourseToBlock(User user, long blockId) {
        Block block = getBlock(blockId);

        BasicEducationProgram program = programRepository.getProgramOf(block);
        FullCourseInfo newCourse = programService.addCourseTo(user, program);
        courseRepository.connectToBlock(newCourse.getCourse(), block);

        updateAllCoursesFromBlock(block);

        return courseService.getFullCourseInfo(user, newCourse.getCourse());
    }

    public List<FullBlockInfo> getAllTemplateCourses(User user, long programId) {
        BasicEducationProgram program = programRepository.findOne(programId);
        if (program == null) {
            throw new IncorrectIdentifierException("There is no program with id: " + programId);
        }

        List<Block> courses = blockRepository.findAllFromProgram(program);
        return courses.stream().map(course-> getFullBlockInfo(user, course)).collect(Collectors.toList());
    }

    public FullBlockInfo addBlockTo(User creator, long programId) {
        BasicEducationProgram program = programRepository.findOne(programId);

        if (program == null) {
            throw new IncorrectIdentifierException("There is no program with id: " + programId);
        }

        Block course = blockRepository.save(new Block());
        blockRepository.connectToProgram(course, program);
        blockRepository.connectToCreator(course, creator);

        return getFullBlockInfo(creator, course);
    }
}