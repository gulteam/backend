package ru.nsu.gulteam.prof_standards.backend.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.gulteam.prof_standards.backend.domain.node.User;
import ru.nsu.gulteam.prof_standards.backend.entity.FullCourseInfo;
import ru.nsu.gulteam.prof_standards.backend.service.CourseService;
import ru.nsu.gulteam.prof_standards.backend.service.SecurityService;
import ru.nsu.gulteam.prof_standards.backend.service.UserService;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.CourseMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.UserMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.CourseDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.Message;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.UserDto;

@RestController
@RequestMapping(path = "api/v1/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final CourseMapper courseMapper;
    private final UserMapper userMapper;
    private final UserService userService;
    private final SecurityService securityService;

    @RequestMapping(path = "{courseId}", method = RequestMethod.GET)
    public ResponseEntity<?> get(@PathVariable int courseId) {
        User user = userService.getUserEntity(securityService.getUserDetails());

        if (!courseService.canReadCourse(user, courseService.getCourse(courseId))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This user have no permissions to read course");
        }

        FullCourseInfo course = courseService.getFullCourseInfo(user, courseService.getCourse(courseId));
        CourseDto courseDto = courseMapper.toDto(course);

        UserDto userDto = userMapper.toDto(course.getCreatedBy());
        courseDto.setCreatedBy(userDto);

        return ResponseEntity.ok(courseDto);
    }

    @RequestMapping(path = "{courseId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable int courseId) {
        User user = userService.getUserEntity(securityService.getUserDetails());

        if (!courseService.canEditCourse(user, courseService.getCourse(courseId))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This user have no permissions to delete course");
        }

        courseService.deleteCourse(courseId);
        return ResponseEntity.ok(new Message("Course successfully deleted"));
    }

    @RequestMapping(path = "{courseId}", method = RequestMethod.POST)
    public ResponseEntity<?> update(@PathVariable int courseId, @RequestBody CourseDto courseDto) {
        User user = userService.getUserEntity(securityService.getUserDetails());

        if (!courseService.canEditCourse(user, courseService.getCourse(courseId))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This user have no permissions to edit course");
        }

        FullCourseInfo course = courseService.updateCourse(user, courseId, courseDto);
        return ResponseEntity.ok(courseMapper.toDto(course));
    }
}
