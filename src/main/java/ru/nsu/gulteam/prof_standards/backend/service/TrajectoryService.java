package ru.nsu.gulteam.prof_standards.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.nsu.gulteam.prof_standards.backend.domain.node.*;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.*;
import ru.nsu.gulteam.prof_standards.backend.entity.FullCourseInfo;
import ru.nsu.gulteam.prof_standards.backend.entity.Trajectory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        checkTrajectory(trajectories);

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

    private List<Trajectory> generateAllTrajectories(BasicEducationProgram program) {
        List<Trajectory> result = new ArrayList<>();

        List<Course> baseCourses = courseRepository.findAllBaseFromProgram(program);
        List<TemplateCourse> templates = templateCourseRepository.findAllFromProgram(program);

        List<Course> courseKit = new ArrayList<>();
        Map<Integer, Integer> templateIndexes = new HashMap<>();
        fillCourseKit(courseKit, templateIndexes, baseCourses, templates);

        recursiveTrajectoryBuilding(result, new ArrayList<>(courseKit), templates, templateIndexes, 0);

        return result;
    }

    private void recursiveTrajectoryBuilding(List<Trajectory> result, List<Course> courses, List<TemplateCourse> templates, Map<Integer, Integer> templateIndexes, int depth) {
        if(depth == templates.size()){
            result.add(new Trajectory(courses.stream().map(courseService::getFullCourseInfo).collect(Collectors.toList())));
            return;
        }

        TemplateCourse templateCourse = templates.get(depth);
        List<Course> implementations = courseRepository.getImplementationsOf(templateCourse);
        int templateIndex = templateIndexes.get(depth);

        for(Course implementation : implementations) {
            courses.set(templateIndex, implementation);
            recursiveTrajectoryBuilding(result, courses, templates, templateIndexes, depth + 1);
            courses.set(templateIndex, null);
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

    private void checkTrajectory(List<Trajectory> trajectories) {
        for (Trajectory trajectory : trajectories) {
            if (trajectory.getCourses().stream().anyMatch(Objects::isNull)) {
                throw new RuntimeException("Trajectory with gap");
            }

            if (CollectionUtils.isEmpty(getProfessionalStandardsReachedBy(trajectory))) {
                throw new RuntimeException("Trajectory without prof. standard");
            }
        }
    }

    private void fillCourseKit(List<Course> courseKit, Map<Integer, Integer> templateIndexes, List<Course> basic, List<TemplateCourse> templates) {
        courseKit.clear();
        templateIndexes.clear();

        Queue<Holder> queue = new PriorityQueue<>();
        basic.stream().map(Holder::new).forEach(queue::add);
        IntStream.range(0, templates.size()).mapToObj(i -> new Holder(templates.get(i), i)).forEach(queue::add);

        for (int i = 0; !queue.isEmpty(); ++i) {
            Holder holder = queue.poll();

            if (holder.course == null) {
                courseKit.add(null);
                templateIndexes.put(holder.templateIndex, i);
                continue;
            }

            courseKit.add(holder.course);
        }
    }

    private static class Holder implements Comparable<Holder> {
        Course course;
        TemplateCourse template;
        int templateIndex;

        public Holder(Course course) {
            this.course = course;
        }

        public Holder(TemplateCourse template, int templateIndex) {
            this.template = template;
            this.templateIndex = templateIndex;
        }

        private int getSemester() {
            return course != null ? course.getSemester() : template.getSemester();
        }

        @Override
        public int compareTo(Holder o) {
            int semester = getSemester();
            int otherSemester = o.getSemester();
            if (semester != otherSemester) {
                return semester - otherSemester;
            }

            if (course != null && o.course == null) {
                return -1;
            }

            if (course == null && o.course != null) {
                return 1;
            }

            return 0;
        }
    }
}
