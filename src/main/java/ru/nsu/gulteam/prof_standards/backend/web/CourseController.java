package ru.nsu.gulteam.prof_standards.backend.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.gulteam.prof_standards.backend.entity.FullCourseInfo;
import ru.nsu.gulteam.prof_standards.backend.service.CourseService;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.CourseMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.UserMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.CourseDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.Message;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.UserDto;

@RestController
@RequestMapping(path = "api/v1/course")
public class CourseController {

    private CourseService courseService;
    private CourseMapper courseMapper;
    private UserMapper userMapper;

    public CourseController(CourseService courseService, CourseMapper courseMapper, UserMapper userMapper) {
        this.courseService = courseService;
        this.courseMapper = courseMapper;
        this.userMapper = userMapper;
    }

    @RequestMapping(path = "{courseId}", method = RequestMethod.GET)
    public ResponseEntity<?> get(@PathVariable int courseId) {
        FullCourseInfo course = courseService.getFullCourseInfo(courseService.getCourse(courseId));
        CourseDto courseDto = courseMapper.toDto(course);

        UserDto userDto = userMapper.toDto(course.getCreatedBy());
        courseDto.setCreatedBy(userDto);

        return ResponseEntity.ok(courseDto);
    }

    @RequestMapping(path = "{courseId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable int courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.ok(new Message("Course successfully deleted"));
    }

    @RequestMapping(path = "{courseId}", method = RequestMethod.POST)
    public ResponseEntity<?> update(@PathVariable int courseId, @RequestBody CourseDto courseDto) {
        FullCourseInfo course = courseService.updateCourse(courseId, courseDto);
        return ResponseEntity.ok(courseMapper.toDto(course));
    }
}
