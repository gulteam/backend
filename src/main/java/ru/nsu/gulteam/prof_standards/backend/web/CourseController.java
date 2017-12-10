package ru.nsu.gulteam.prof_standards.backend.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.gulteam.prof_standards.backend.entity.FullCourseInfo;
import ru.nsu.gulteam.prof_standards.backend.service.CourseService;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.CourseMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.CourseDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.Message;

@RestController
@RequestMapping(path = "api/v1/course")
public class CourseController {

    private CourseService courseService;
    private CourseMapper courseMapper;

    public CourseController(CourseService courseService, CourseMapper courseMapper) {
        this.courseService = courseService;
        this.courseMapper = courseMapper;
    }

    @RequestMapping(path = "{courseId}", method = RequestMethod.GET)
    public ResponseEntity<?> get(@PathVariable int courseId) {
        FullCourseInfo course = courseService.getFullCourseInfo(courseService.getCourse(courseId));
        return ResponseEntity.ok(courseMapper.toDto(course));
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
