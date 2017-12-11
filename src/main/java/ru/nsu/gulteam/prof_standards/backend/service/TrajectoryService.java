package ru.nsu.gulteam.prof_standards.backend.service;

import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.*;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.*;
import ru.nsu.gulteam.prof_standards.backend.entity.Trajectory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
public class TrajectoryService {
    private CourseRepository courseRepository;
    private TemplateCourseRepository templateCourseRepository;
    private ProfessionalStandardRepository professionalStandardRepository;
    private SkillsRepository skillsRepository;
    private KnowledgeRepository knowledgeRepository;

    public TrajectoryService(CourseRepository courseRepository, TemplateCourseRepository templateCourseRepository, ProfessionalStandardRepository professionalStandardRepository, SkillsRepository skillsRepository, KnowledgeRepository knowledgeRepository) {
        this.courseRepository = courseRepository;
        this.templateCourseRepository = templateCourseRepository;
        this.professionalStandardRepository = professionalStandardRepository;
        this.skillsRepository = skillsRepository;
        this.knowledgeRepository = knowledgeRepository;
    }

    public List<Trajectory> generateAllTrajectories(BasicEducationProgram program){
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
            result.add(new Trajectory(courses));
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

    public List<ProfessionalStandard> getProfessionalStandardsReachedBy(Trajectory trajectory) {
        List<ProfessionalStandard> reachedStandards = new ArrayList<>();

        for(ProfessionalStandard professionalStandard : professionalStandardRepository.findAll()){
            Set<Skills> requiredSkills = skillsRepository.getRequiredForStandard(professionalStandard);
            Set<Knowledge> requiredKnowledge = knowledgeRepository.getRequiredForStandard(professionalStandard);

            Set<Skills> developsSkills = new TreeSet<>();
            Set<Knowledge> developsKnowledge = new TreeSet<>();
            for(Course course : trajectory.getCourses()){
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
