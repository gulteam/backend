package ru.nsu.gulteam.prof_standards.backend.service;

import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.*;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.*;
import ru.nsu.gulteam.prof_standards.backend.entity.FullCourseInfo;
import ru.nsu.gulteam.prof_standards.backend.entity.Trajectory;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TrajectoryService {
    private CourseRepository courseRepository;
    private TemplateCourseRepository templateCourseRepository;
    private ProfessionalStandardRepository professionalStandardRepository;
    private SkillsRepository skillsRepository;
    private KnowledgeRepository knowledgeRepository;
    private CourseService courseService;
    private TrajectoryRepository trajectoryRepository;
    private TrajectoryCourseRepository trajectoryCourseRepository;

    public TrajectoryService(CourseRepository courseRepository, TemplateCourseRepository templateCourseRepository, ProfessionalStandardRepository professionalStandardRepository, SkillsRepository skillsRepository, KnowledgeRepository knowledgeRepository, CourseService courseService, TrajectoryRepository trajectoryRepository, TrajectoryCourseRepository trajectoryCourseRepository) {
        this.courseRepository = courseRepository;
        this.templateCourseRepository = templateCourseRepository;
        this.professionalStandardRepository = professionalStandardRepository;
        this.skillsRepository = skillsRepository;
        this.knowledgeRepository = knowledgeRepository;
        this.courseService = courseService;
        this.trajectoryRepository = trajectoryRepository;
        this.trajectoryCourseRepository = trajectoryCourseRepository;
    }

    public void updateTrajectories(BasicEducationProgram program) {
        List<Trajectory> trajectories = generateAllTrajectories(program);

        for (StudyTrajectory tr : trajectoryRepository.findAllByProgram(program)) {
            Set<TrajectoryCourse> byTrajectory = trajectoryCourseRepository.getByTrajectory(tr);
            trajectoryCourseRepository.delete(byTrajectory);
            trajectoryRepository.delete(tr);
        }

        for (Trajectory trajectory : trajectories) {
            StudyTrajectory tr = new StudyTrajectory();
            tr = trajectoryRepository.save(tr);
            tr = trajectoryRepository.connectToProgram(tr, program);

            for (int i = 0; i < trajectory.getCourses().size(); ++i) {
                Course course = trajectory.getCourses().get(i).getCourse();
                TrajectoryCourse trajectoryCourse = new TrajectoryCourse();
                trajectoryCourse.setOrder(i);
                trajectoryCourse = trajectoryCourseRepository.save(trajectoryCourse);
                trajectoryCourse = trajectoryCourseRepository.connectToTrajectory(tr, trajectoryCourse);
                trajectoryCourseRepository.connectToCourse(course, trajectoryCourse);
            }

            for (ProfessionalStandard standard : getProfessionalStandardsReachedBy(trajectory)) {
                tr = trajectoryRepository.connectToStandard(tr, standard);
            }
        }
    }

    private List<Trajectory> generateAllTrajectories(BasicEducationProgram program){
        List<Trajectory> result = new ArrayList<>();

        List<Course> baseCourses = courseRepository.findAllBaseFromProgram(program);
        List<TemplateCourse> templates = templateCourseRepository.findAllFromProgram(program);
        recursiveTrajectoryBuilding(result, baseCourses, new ArrayList<>(), templates, 0);

        return result;
    }

    private void recursiveTrajectoryBuilding(List<Trajectory> result, List<Course> baseCourses, List<Course> variableCourses, List<TemplateCourse> templates, int depth) {
        if(depth == templates.size()){
            List<Course> courses = new ArrayList<>(baseCourses);
            courses.addAll(variableCourses);

            courses.sort((c1, c2)->{
                if(c1.getSemester() != c2.getSemester()){
                    return c1.getSemester() - c2.getSemester();
                }
                else if(baseCourses.contains(c1) && !baseCourses.contains(c2)){
                    return -1;
                }
                else if(!baseCourses.contains(c1) && baseCourses.contains(c2)){
                    return 1;
                }
                else{
                    return (int)(c1.getId() - c2.getId());
                }
            });

            result.add(new Trajectory(courses.stream().map(courseService::getFullCourseInfo).collect(Collectors.toList())));
            return;
        }

        TemplateCourse templateCourse = templates.get(depth);
        List<Course> implementations = courseRepository.getImplementationsOf(templateCourse);

        for(Course implementation : implementations){
            variableCourses.add(implementation);
            recursiveTrajectoryBuilding(result, baseCourses, variableCourses, templates, depth + 1);
            variableCourses.remove(implementation);
        }
    }

    private List<ProfessionalStandard> getProfessionalStandardsReachedBy(Trajectory trajectory) {
        List<ProfessionalStandard> reachedStandards = new ArrayList<>();

        for(ProfessionalStandard professionalStandard : professionalStandardRepository.findAll()){
            Set<Skills> requiredSkills = skillsRepository.getRequiredForStandard(professionalStandard);
            Set<Knowledge> requiredKnowledge = knowledgeRepository.getRequiredForStandard(professionalStandard);

            Set<Skills> developsSkills = new TreeSet<>();
            Set<Knowledge> developsKnowledge = new TreeSet<>();
            for(FullCourseInfo fullCourseInfo : trajectory.getCourses()){
                Course course = fullCourseInfo.getCourse();
                developsSkills.addAll(skillsRepository.getDevelopsByCourse(course));
                developsKnowledge.addAll(knowledgeRepository.getDevelopsByCourse(course));
            }

            if(developsSkills.containsAll(requiredSkills) && developsKnowledge.containsAll(requiredKnowledge)){
                reachedStandards.add(professionalStandard);
            }
        }

        return reachedStandards;
    }
}
