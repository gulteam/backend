package ru.nsu.gulteam.prof_standards.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Course;
import ru.nsu.gulteam.prof_standards.backend.domain.node.ProfessionalStandard;
import ru.nsu.gulteam.prof_standards.backend.entity.FullCourseInfo;
import ru.nsu.gulteam.prof_standards.backend.entity.FullSearchRequest;
import ru.nsu.gulteam.prof_standards.backend.entity.Trajectory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SearchService {
    private TrajectoryService trajectoryService;

    @Autowired
    public SearchService(TrajectoryService trajectoryService) {
        this.trajectoryService = trajectoryService;
    }

    public List<Trajectory> search(FullSearchRequest fullSearchRequest) {
        List<Trajectory> trajectories = new ArrayList<>();

        for (Trajectory trajectory : trajectoryService.generateAllTrajectories(fullSearchRequest.getBasicEducationProgram())) {
            List<FullCourseInfo> fullCourseInfos = trajectory.getCourses();
            Set<Long> courseIds = fullCourseInfos.stream().map(fullCourseInfo -> fullCourseInfo.getCourse().getId()).collect(Collectors.toSet());

            Set<Long> includeCourses = fullSearchRequest.getIncludeCourses().stream().map(Course::getId).collect(Collectors.toSet());
            Set<Long> excludeCourses = fullSearchRequest.getExcludeCourses().stream().map(Course::getId).collect(Collectors.toSet());

            Set<Long> professionalStandardsId = trajectoryService.getProfessionalStandardsReachedBy(trajectory).stream().map(ProfessionalStandard::getId).collect(Collectors.toSet());

            Set<Long> includeStandards = fullSearchRequest.getIncludeStandards().stream().map(ProfessionalStandard::getId).collect(Collectors.toSet());
            Set<Long> excludeStandards = fullSearchRequest.getExcludeStandards().stream().map(ProfessionalStandard::getId).collect(Collectors.toSet());

            if (courseIds.containsAll(includeCourses) &&
                    courseIds.stream().noneMatch(excludeCourses::contains) &&
                    professionalStandardsId.containsAll(includeStandards) &&
                    professionalStandardsId.stream().noneMatch(excludeStandards::contains)) {
                trajectories.add(trajectory);
            }
        }

        return trajectories;
    }
}
