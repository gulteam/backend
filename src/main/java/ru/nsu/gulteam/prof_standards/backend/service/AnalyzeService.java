package ru.nsu.gulteam.prof_standards.backend.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.BasicEducationProgram;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Competence;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Fgos;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.AnalyzeRepository;
import ru.nsu.gulteam.prof_standards.backend.entity.AnalyzeMessage;
import ru.nsu.gulteam.prof_standards.backend.entity.AnalyzeResult;
import ru.nsu.gulteam.prof_standards.backend.entity.FullCourseInfo;
import ru.nsu.gulteam.prof_standards.backend.entity.Trajectory;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyzeService {
    private final ProgramService programService;
    private final CourseService courseService;
    private final ProfessionalStandardService standardService;
    private final SearchService searchService;
    private final TrajectoryService trajectoryService;
    private final AnalyzeRepository analyzeRepository;
    private final FgosService fgosService;

    public AnalyzeResult analyze(BasicEducationProgram program) {
        Map<Long, FullCourseInfo> courses = new TreeMap<>();
        List<Trajectory> trajectories = trajectoryService.generateAllTrajectories(program);
        programService.getAllCourses(null, program).forEach(course -> courses.put(course.getCourse().getId(), course));

        List<AnalyzeMessage> errorMessages = new ArrayList<>();

        //errorMessages.addAll(checkForIncorrectDependsOrder(courses));
        //errorMessages.addAll(checkForAttainabilityOfStandards(program));
        //errorMessages.addAll(checkForAttainabilityOfTrajectories(trajectories));
        //errorMessages.addAll(checkForAttainabilityOfCourses(trajectories, courses));

        if (program.getFgos() == null) {
            errorMessages.add(new AnalyzeMessage(AnalyzeMessage.Type.WARNING, "Для данной ООП не задан ФГОС!"));
        } else {
            Fgos fgos = fgosService.get(program.getFgos().getId());
            errorMessages.addAll(analyzeProgramVolume(program, fgos));
            errorMessages.addAll(analyzeRequiredCourses(program, courses));

            errorMessages.addAll(analyzeTrajectoriesAndCompetences(trajectories, program, fgos, courses));
        }

        return new AnalyzeResult(errorMessages);
    }

    private List<AnalyzeMessage> analyzeTrajectoriesAndCompetences(List<Trajectory> trajectories, BasicEducationProgram program, Fgos fgos, Map<Long, FullCourseInfo> courses) {
        List<AnalyzeMessage> messages = new ArrayList<>();

        Set<Integer> mustBeDeveloped = fgos.getRequireCompetence().stream().map(competence -> (int) (long) competence.getId()).collect(Collectors.toSet());
        Map<Long, Competence> competencesById = new TreeMap<>();
        fgos.getRequireCompetence().forEach(competence -> competencesById.put(competence.getId(), competence));

        for (Trajectory trajectory : trajectories) {
            Set<Integer> developedCompetences = trajectory
                    .getCourses()
                    .stream()
                    .flatMap(course -> course.getDevelopCompetence().stream())
                    .distinct()
                    .collect(Collectors.toSet());

            Set<Integer> notDeveloped = mustBeDeveloped.stream().filter(id -> !developedCompetences.contains(id)).collect(Collectors.toSet());
            if (notDeveloped.isEmpty()) {
                continue;
            }

            String trajectoryString = StringUtils.join(trajectory.getCourses().stream().filter(FullCourseInfo::isImplementsTemplate).map(course -> course.getCourse().getName()).collect(Collectors.toList()), ", ");
            String competencesString = StringUtils.join(notDeveloped.stream().map(id -> String.format("\"%s\"", competencesById.get((long) (int) id).getCode())).collect(Collectors.toList()), ", ");

            messages.add(new AnalyzeMessage(AnalyzeMessage.Type.ERROR, String.format("Траектория \"%s\", не формирует компетенции [%s]", trajectoryString, competencesString)));
        }

        if(messages.isEmpty()){
            messages.add(new AnalyzeMessage(AnalyzeMessage.Type.INFO, "Все компетенции формируемы при любой траектории."));
        }

        return messages;
    }

    private List<AnalyzeMessage> analyzeRequiredCourses(BasicEducationProgram program, Map<Long, FullCourseInfo> courses) {
        List<AnalyzeMessage> messages = new ArrayList<>();

        program.getFgos().getRequireCourses().forEach(course -> {
            if (courses.values().stream().noneMatch(c -> !c.isImplementsTemplate() && c.getCourse().getName().equals(course.getName()))) {
                messages.add(new AnalyzeMessage(AnalyzeMessage.Type.WARNING, String.format("Не найдена обязательная дисциплина \"%s\"", course.getName())));
            }
        });


        if(messages.isEmpty()){
            messages.add(new AnalyzeMessage(AnalyzeMessage.Type.INFO, "Все обязательные дисциплины присутствуют в ООП."));
        }

        return messages;
    }

    private List<AnalyzeMessage> analyzeProgramVolume(BasicEducationProgram program, Fgos fgos) {
        List<AnalyzeMessage> messages = new ArrayList<>();
        double summaryDisciplineVolume = analyzeRepository.getVolumeOfBasicPart(program) +
                analyzeRepository.getVolumeOfVariablePart(program);

        double requiredDisciplineVolume = fgos.getDisciplineVolumeFrom();

        if (summaryDisciplineVolume < requiredDisciplineVolume) {
            messages.add(new AnalyzeMessage(AnalyzeMessage.Type.ERROR, String.format("Суммарный объём дисциплин = %d з.е. Требуемый объём - не менее %d з.е.", (int) summaryDisciplineVolume, (int) requiredDisciplineVolume)));
        } else {
            messages.add(new AnalyzeMessage(AnalyzeMessage.Type.INFO, String.format("Суммарный объём дисциплин = %d з.е.", (int) summaryDisciplineVolume)));
        }

        return messages;
    }


    /*private List<AnalyzeMessage> checkForAttainabilityOfTrajectories(List<Trajectory> trajectories) {
        List<AnalyzeMessage> errorMessages = new ArrayList<>();

        for (Trajectory trajectory : trajectories) {
            if (trajectoryService.getProfessionalStandardsReachedBy(trajectory).isEmpty()) {
                errorMessages.add(new AnalyzeMessage(AnalyzeMessage.Type.ERROR, String.format("Trajectory \"%s\" doesn't reach any standard", String.join(" | ",
                        trajectory.getCourses()
                                .stream()
                                .map(course -> course.getCourse().getName())
                                .collect(Collectors.toList())
                ))));
            }
        }

        return errorMessages;
    }

    private List<AnalyzeMessage> checkForAttainabilityOfCourses(List<Trajectory> trajectories, Map<Long, FullCourseInfo> courses) {
        List<AnalyzeMessage> errorMessages = new ArrayList<>();

        courses.keySet().forEach(courseId -> {
            if (trajectories.stream().noneMatch(trajectory -> trajectory.getCourses().stream().anyMatch(course -> course.getCourse().getId().equals(courseId)))) {
                errorMessages.add(new AnalyzeMessage(AnalyzeMessage.Type.ERROR, String.format("There is no trajectory that contains course \"%s\"", courses.get(courseId).getCourse().getName())));
            }
        });

        return errorMessages;
    }

    private List<AnalyzeMessage> checkForAttainabilityOfStandards(BasicEducationProgram program) {
        List<AnalyzeMessage> errorMessages = new ArrayList<>();

        for (ProfessionalStandard professionalStandard : standardService.getAllStandards()) {
            if (searchService.search(new FullSearchRequest(program, Collections.emptyList(), Collections.emptyList(), Collections.singletonList(professionalStandard), Collections.emptyList())).isEmpty()) {
                errorMessages.add(new AnalyzeMessage(AnalyzeMessage.Type.ERROR, String.format("There is no trajectory for standard \"%s\"", professionalStandard.getName())));
            }
        }

        return errorMessages;
    }

    private List<AnalyzeMessage> checkForIncorrectDependsOrder(Map<Long, FullCourseInfo> courses) {
        List<AnalyzeMessage> errorMessages = new ArrayList<>();

        courses.values().forEach(course -> {
            int currentSemester = course.getCourse().getSemester();

            List<FullCourseInfo> previousCourses = course.getPreviousCourses().stream().map(courses::get).collect(Collectors.toList());

            previousCourses.stream()
                    .filter(previousCourse -> previousCourse.getCourse().getSemester() > currentSemester)
                    .forEach(previousCourse -> {
                        errorMessages.add(new AnalyzeMessage(AnalyzeMessage.Type.ERROR, String.format("\"%s\" (%d semester) depends on \"%s\"(%d semester)", course.getCourse().getName(), currentSemester, previousCourse.getCourse().getName(), previousCourse.getCourse().getSemester())));
                    });

            Map<Long, List<FullCourseInfo>> dependenciesByBlocks = new TreeMap<>();
            previousCourses.stream().filter(FullCourseInfo::isImplementsTemplate).forEach(previousCourse -> {
                dependenciesByBlocks.putIfAbsent(previousCourse.getTemplateCourse(), new ArrayList<>());
                dependenciesByBlocks.get(previousCourse.getTemplateCourse()).add(previousCourse);
            });

            dependenciesByBlocks.keySet().stream().filter(key -> dependenciesByBlocks.get(key).size() > 1).forEach(key -> {
                errorMessages.add(new AnalyzeMessage(AnalyzeMessage.Type.ERROR, String.format("\"%s\" depends on two or more courses from one block \"%s\"", course.getCourse().getName(), String.join(" | ", dependenciesByBlocks.get(key).stream().map(baseCourse -> baseCourse.getCourse().getName()).collect(Collectors.toList())))));
            });
        });

        return errorMessages;
    }*/
}
