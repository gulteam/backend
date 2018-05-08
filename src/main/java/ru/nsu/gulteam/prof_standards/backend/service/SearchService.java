package ru.nsu.gulteam.prof_standards.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Course;
import ru.nsu.gulteam.prof_standards.backend.domain.node.ProfessionalStandard;
import ru.nsu.gulteam.prof_standards.backend.domain.node.StudyTrajectory;
import ru.nsu.gulteam.prof_standards.backend.domain.node.TrajectoryCourse;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.CourseRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.ProfessionalStandardRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.TrajectoryCourseRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.TrajectoryRepository;
import ru.nsu.gulteam.prof_standards.backend.entity.FullSearchRequest;
import ru.nsu.gulteam.prof_standards.backend.entity.Trajectory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SearchService {
    private TrajectoryRepository trajectoryRepository;
    private TrajectoryCourseRepository trajectoryCourseRepository;
    private CourseRepository courseRepository;
    private ProfessionalStandardRepository professionalStandardRepository;
    private CourseService courseService;

    @Autowired
    public SearchService(TrajectoryService trajectoryService, TrajectoryRepository trajectoryRepository, TrajectoryCourseRepository trajectoryCourseRepository, CourseRepository courseRepository, ProfessionalStandardRepository professionalStandardRepository, CourseService courseService) {
        this.trajectoryRepository = trajectoryRepository;
        this.trajectoryCourseRepository = trajectoryCourseRepository;
        this.courseRepository = courseRepository;
        this.professionalStandardRepository = professionalStandardRepository;
        this.courseService = courseService;
    }

    public List<Trajectory> search(FullSearchRequest fullSearchRequest) {
        List<Trajectory> trajectories = new ArrayList<>();

        for (StudyTrajectory trajectory : trajectoryRepository.findAllByProgram(fullSearchRequest.getBasicEducationProgram())) {
            List<TrajectoryCourse> trajectoryCourses = trajectoryCourseRepository.getByTrajectory(trajectory).stream()
                    .sorted(Comparator.comparingLong(TrajectoryCourse::getId))
                    .collect(Collectors.toList());

            List<Course> courses = trajectoryCourses.stream()
                    .map(courseRepository::findByTrajectoryCourse)
                    .collect(Collectors.toList());
            Set<Long> courseIds = courses.stream().map(Course::getId).collect(Collectors.toSet());


            Set<Long> includeCourses = fullSearchRequest.getIncludeCourses().stream().map(Course::getId).collect(Collectors.toSet());
            Set<Long> excludeCourses = fullSearchRequest.getExcludeCourses().stream().map(Course::getId).collect(Collectors.toSet());


            Set<Long> professionalStandardsId = professionalStandardRepository.getByTrajectory(trajectory).stream().map(ProfessionalStandard::getId).collect(Collectors.toSet());

            Set<Long> includeStandards = fullSearchRequest.getIncludeStandards().stream().map(ProfessionalStandard::getId).collect(Collectors.toSet());
            Set<Long> excludeStandards = fullSearchRequest.getExcludeStandards().stream().map(ProfessionalStandard::getId).collect(Collectors.toSet());

            if (courseIds.containsAll(includeCourses) &&
                    courseIds.stream().noneMatch(excludeCourses::contains) &&
                    professionalStandardsId.containsAll(includeStandards) &&
                    professionalStandardsId.stream().noneMatch(excludeStandards::contains)) {
                trajectories.add(new Trajectory(courses.stream().map(courseService::getFullCourseInfo).collect(Collectors.toList())));
            }
        }

        return trajectories;
    }
}
