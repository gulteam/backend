package ru.nsu.gulteam.prof_standards.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.BasicEducationProgram;
import ru.nsu.gulteam.prof_standards.backend.domain.node.ProfessionalStandard;
import ru.nsu.gulteam.prof_standards.backend.entity.AnalyzeResult;
import ru.nsu.gulteam.prof_standards.backend.entity.FullCourseInfo;
import ru.nsu.gulteam.prof_standards.backend.entity.FullSearchRequest;
import ru.nsu.gulteam.prof_standards.backend.entity.Trajectory;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyzeService {
    private ProgramService programService;
    private CourseService courseService;
    private ProfessionalStandardService standardService;
    private SearchService searchService;
    private TrajectoryService trajectoryService;

    @Autowired
    public AnalyzeService(ProgramService programService, CourseService courseService, ProfessionalStandardService standardService, SearchService searchService, TrajectoryService trajectoryService) {
        this.programService = programService;
        this.courseService = courseService;
        this.standardService = standardService;
        this.searchService = searchService;
        this.trajectoryService = trajectoryService;
    }


    public AnalyzeResult analyze(BasicEducationProgram program) {
        Map<Long, FullCourseInfo> courses = new TreeMap<>();
        List<Trajectory> trajectories = trajectoryService.generateAllTrajectories(program);
        programService.getAllCourses(null, program).forEach(course -> courses.put(course.getCourse().getId(), course));
        List<String> errorMessages = new ArrayList<>();

        errorMessages.addAll(checkForIncorrectDependsOrder(courses));
        errorMessages.addAll(checkForAttainabilityOfStandards(program));
        errorMessages.addAll(checkForAttainabilityOfTrajectories(trajectories));
        errorMessages.addAll(checkForAttainabilityOfCourses(trajectories, courses));

        return new AnalyzeResult(errorMessages);
    }


    private List<String> checkForAttainabilityOfTrajectories(List<Trajectory> trajectories) {
        List<String> errorMessages = new ArrayList<>();

        for (Trajectory trajectory : trajectories) {
            if (trajectoryService.getProfessionalStandardsReachedBy(trajectory).isEmpty()) {
                errorMessages.add(String.format("Trajectory \"%s\" doesn't reach any standard", String.join(" | ",
                        trajectory.getCourses()
                                .stream()
                                .map(course -> course.getCourse().getName())
                                .collect(Collectors.toList())
                )));
            }
        }

        return errorMessages;
    }

    private List<String> checkForAttainabilityOfCourses(List<Trajectory> trajectories, Map<Long, FullCourseInfo> courses) {
        List<String> errorMessages = new ArrayList<>();

        courses.keySet().forEach(courseId -> {
            if (trajectories.stream().noneMatch(trajectory -> trajectory.getCourses().stream().anyMatch(course -> course.getCourse().getId().equals(courseId)))) {
                errorMessages.add(String.format("There is no trajectory that contains course \"%s\"", courses.get(courseId).getCourse().getName()));
            }
        });

        return errorMessages;
    }

    private List<String> checkForAttainabilityOfStandards(BasicEducationProgram program) {
        List<String> errorMessages = new ArrayList<>();

        for (ProfessionalStandard professionalStandard : standardService.getAllStandards()) {
            if (searchService.search(new FullSearchRequest(program, Collections.emptyList(), Collections.emptyList(), Collections.singletonList(professionalStandard), Collections.emptyList())).isEmpty()) {
                errorMessages.add(String.format("There is no trajectory for standard \"%s\"", professionalStandard.getName()));
            }
        }

        return errorMessages;
    }

    private List<String> checkForIncorrectDependsOrder(Map<Long, FullCourseInfo> courses) {
        List<String> errorMessages = new ArrayList<>();

        courses.values().forEach(course -> {
            int currentSemester = course.getCourse().getSemester();

            List<FullCourseInfo> previousCourses = course.getPreviousCourses().stream().map(courses::get).collect(Collectors.toList());

            previousCourses.stream()
                    .filter(previousCourse -> previousCourse.getCourse().getSemester() > currentSemester)
                    .forEach(previousCourse -> {
                        errorMessages.add(String.format("\"%s\" (%d semester) depends on \"%s\"(%d semester)", course.getCourse().getName(), currentSemester, previousCourse.getCourse().getName(), previousCourse.getCourse().getSemester()));
                    });

            Map<Long, List<FullCourseInfo>> dependenciesByBlocks = new TreeMap<>();
            previousCourses.stream().filter(FullCourseInfo::isImplementsTemplate).forEach(previousCourse -> {
                dependenciesByBlocks.putIfAbsent(previousCourse.getTemplateCourse(), new ArrayList<>());
                dependenciesByBlocks.get(previousCourse.getTemplateCourse()).add(previousCourse);
            });

            dependenciesByBlocks.keySet().stream().filter(key -> dependenciesByBlocks.get(key).size() > 1).forEach(key -> {
                errorMessages.add(String.format("\"%s\" depends on two or more courses from one block \"%s\"", course.getCourse().getName(), String.join(" | ", dependenciesByBlocks.get(key).stream().map(baseCourse -> baseCourse.getCourse().getName()).collect(Collectors.toList()))));
            });
        });

        return errorMessages;
    }
}
