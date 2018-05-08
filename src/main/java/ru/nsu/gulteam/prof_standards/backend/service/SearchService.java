package ru.nsu.gulteam.prof_standards.backend.service;

import javafx.util.Pair;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.BasicEducationProgram;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Course;
import ru.nsu.gulteam.prof_standards.backend.domain.node.ProfessionalStandard;
import ru.nsu.gulteam.prof_standards.backend.domain.node.TemplateCourse;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.CourseRepository;
import ru.nsu.gulteam.prof_standards.backend.entity.FullCourseInfo;
import ru.nsu.gulteam.prof_standards.backend.entity.FullSearchRequest;
import ru.nsu.gulteam.prof_standards.backend.entity.FullTemplateCourseInfo;
import ru.nsu.gulteam.prof_standards.backend.entity.Trajectory;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SearchService {
    private TrajectoryService trajectoryService;
    private CourseRepository courseRepository;
    private CourseService courseService;
    private ProgramService programService;

    public List<Trajectory> search(FullSearchRequest fullSearchRequest) {
        return new Searcher(fullSearchRequest).search();
    }


    private class Searcher {
        private List<FullCourseInfo> currentTrajectory = new ArrayList<>();
        private List<FullTemplateCourseInfo> sortedBlocks = new ArrayList<>();
        private int[] maxSelectedCourseFromBlock = new int[0];

        final Set<Pair<Double, Trajectory>> trajectories = new TreeSet<>((a, b) -> {
            double aWeight = a.getKey();
            double bWeigth = b.getKey();

            if (Double.compare(aWeight, bWeigth) == 0) {
                return a.hashCode() - b.hashCode();
            }
            return -Double.compare(a.getKey(), b.getKey());
        });

        private int fixedBlock = 0;

        private Map<Long, FullCourseInfo> coursesById = new TreeMap<>();
        private FullSearchRequest fullSearchRequest;

        public Searcher(FullSearchRequest fullSearchRequest) {
            this.fullSearchRequest = fullSearchRequest;
        }


        public List<Trajectory> search() {
            List<FullCourseInfo> allCourses = programService.getAllCourses(null, fullSearchRequest.getBasicEducationProgram());

            List<FullCourseInfo> baseCourses = new ArrayList<>();

            for (FullCourseInfo courseInfo : allCourses) {
                coursesById.put(courseInfo.getCourse().getId(), courseInfo);
            }

            changeRating(fullSearchRequest);

            Map<Long, FullTemplateCourseInfo> blocks = new TreeMap<>();
            List<TemplateCourse> templateCourses = programService.getAllTemplateCourses(null, fullSearchRequest.getBasicEducationProgram());
            for (TemplateCourse templateCourse : templateCourses) {
                blocks.put(templateCourse.getId(), new FullTemplateCourseInfo(templateCourse, new ArrayList<>()));
            }

            for (FullCourseInfo courseInfo : allCourses) {
                if (!courseInfo.isImplementsTemplate()) {
                    baseCourses.add(courseInfo);
                    continue;
                }

                blocks.get(courseInfo.getTemplateCourse()).getCourses().add(courseInfo);
            }

            sortCoursesInBlocks(blocks.values());

            sortedBlocks = new ArrayList<>(blocks.values());
            sortedBlocks.sort((a, b) -> {
                int aSemester = a.getTemplateCourse().getSemester();
                int bSemester = b.getTemplateCourse().getSemester();

                if (aSemester == bSemester) {
                    return (int) (a.getTemplateCourse().getId() - b.getTemplateCourse().getId());
                }

                return aSemester - bSemester;
            });

            // User info
            int currentStudentSemester = 0;
            Map<TemplateCourse, Course> coursesThatUserAlreadyKnow = new TreeMap<>();

            int fromBlock = 0;
            for (int i = 0; i < sortedBlocks.size(); ++i) {
                if (sortedBlocks.get(i).getTemplateCourse().getSemester() > currentStudentSemester) {
                    fromBlock = i;
                    break;
                }
            }

            maxSelectedCourseFromBlock = new int[sortedBlocks.size()];

            final int CLIENT_SHOW_TRAJECTORY_COUNT = 10;
            final int SEARCH_TRAJECTORY_COUNT = CLIENT_SHOW_TRAJECTORY_COUNT * 2;

          /*  trajectories = new TreeSet<>((a, b)->{
                double aWeight = a.getKey();
                double bWeigth = b.getKey();

                if(Double.compare(aWeight, bWeigth) == 0){
                    return a.hashCode() - b.hashCode();
                }
                return -Double.compare(a.getKey(), b.getKey());
            });*/

            fixedBlock = 0;
            while (trajectories.size() < SEARCH_TRAJECTORY_COUNT) {
                currentTrajectory = new ArrayList<>();
                recursiveSearchingTrajectories(fromBlock);

                int nextBlock = -1;
                double maxNextRating = 0.0;
                for (int i = fromBlock; i < sortedBlocks.size(); ++i) {
                    int currentMaxSelected = maxSelectedCourseFromBlock[i];

                    if (currentMaxSelected == sortedBlocks.get(i).getCourses().size() - 1) { // In case if we already select all courses from this block
                        continue;
                    }

                    double nextCourseRating = sortedBlocks.get(i).getCourses().get(currentMaxSelected + 1).getCourse().getRating();
                    if (nextBlock == -1 || nextCourseRating > maxNextRating) {
                        nextBlock = i;
                        maxNextRating = nextCourseRating;
                    }
                }

                if (nextBlock == -1) {
                    break;
                }

                maxSelectedCourseFromBlock[nextBlock]++;
                fixedBlock = nextBlock;
            }

            List<Trajectory> result = trajectories.stream()
                    .map(Pair::getValue)
                    .limit(CLIENT_SHOW_TRAJECTORY_COUNT)
                    .collect(Collectors.toList());

            return result;
        }

        private void recursiveSearchingTrajectories(int currentBlock) {
            if (currentBlock == sortedBlocks.size()) {
                double trajectoryWeight = currentTrajectory.stream().mapToDouble(a -> a.getCourse().getRating()).sum(); // Todo: use prof. standarts

                if (trajectoryService.isThisTrajecory(currentTrajectory)) {
                    List<ProfessionalStandard> achievedProfessionalStandard = trajectoryService.getProfessionalStandardsReachedBy(new Trajectory(currentTrajectory));

                    Set<Long> includeStandards = fullSearchRequest.getIncludeStandards().stream().map(ProfessionalStandard::getId).collect(Collectors.toSet());
                    Set<Long> excludeStandards = fullSearchRequest.getExcludeStandards().stream().map(ProfessionalStandard::getId).collect(Collectors.toSet());

                    for (ProfessionalStandard profStandart : achievedProfessionalStandard) {
                        for (Long idInclude : includeStandards) {
                            if (profStandart.getId() == idInclude)
                                trajectoryWeight += 20;
                        }
                        for (Long idExclude : excludeStandards) {
                            if (profStandart.getId() == idExclude)
                                trajectoryWeight -= 20;
                        }
                    }
                    trajectories.add(new Pair<>(trajectoryWeight, new Trajectory(new ArrayList<>(currentTrajectory))));
                }
                return;
            }

            FullTemplateCourseInfo block = sortedBlocks.get(currentBlock);

            int from = 0;
            if (currentBlock == fixedBlock) {
                from = maxSelectedCourseFromBlock[currentBlock];
            }

            for (int i = from; i <= maxSelectedCourseFromBlock[currentBlock]; ++i) {
                FullCourseInfo course = block.getCourses().get(i);
                currentTrajectory.add(course);
                recursiveSearchingTrajectories(currentBlock + 1);
                currentTrajectory.remove(course);
            }
        }

        private void sortCoursesInBlocks(Collection<FullTemplateCourseInfo> blocks) {
            for (FullTemplateCourseInfo block : blocks) {
                block.getCourses().sort((a, b) -> -((int) a.getCourse().getRating() - (int) b.getCourse().getRating()));
            }
        }

        private void changeRating(FullSearchRequest fullSearchRequest) {
            Set<Long> includeCourses = fullSearchRequest.getIncludeCourses().stream().map(Course::getId).collect(Collectors.toSet());
            Set<Long> excludeCourses = fullSearchRequest.getExcludeCourses().stream().map(Course::getId).collect(Collectors.toSet());

            for (Long idCourse : includeCourses) {
                Course course = coursesById.get(idCourse).getCourse();
                course.setRating(course.getRating() + 10);
            }

            for (Long idCourse : excludeCourses) {
                Course course = coursesById.get(idCourse).getCourse();
                course.setRating(course.getRating() - 10);
            }
        }
    }
}
