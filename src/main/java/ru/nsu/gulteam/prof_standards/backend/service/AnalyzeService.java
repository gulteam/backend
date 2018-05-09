package ru.nsu.gulteam.prof_standards.backend.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.*;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.AnalyzeRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.ProfessionalStandardRepository;
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
    private final ProfessionalStandardRepository standardRepository;
    private final SearchService searchService;
    private final TrajectoryService trajectoryService;
    private final AnalyzeRepository analyzeRepository;
    private final FgosService fgosService;

    public AnalyzeResult analyze(BasicEducationProgram program) {
        Map<Long, FullCourseInfo> courses = new TreeMap<>();
        List<Trajectory> trajectories = trajectoryService.generateAllTrajectories(program);
        programService.getAllCourses(null, program).forEach(course -> courses.put(course.getCourse().getId(), course));

        List<AnalyzeMessage> errorMessages = new ArrayList<>();

        if (program.getFgos() == null) {
            errorMessages.add(new AnalyzeMessage(AnalyzeMessage.Type.WARNING, "Для данной ООП не задан ФГОС!"));
        } else {
            Fgos fgos = fgosService.get(program.getFgos().getId());
            errorMessages.addAll(analyzeProgramVolume(program, fgos));
            errorMessages.addAll(analyzeRequiredCourses(program, courses));

            errorMessages.addAll(analyzeTrajectoriesAndCompetences(trajectories, program, fgos, courses));
        }

        errorMessages.addAll(analyzeDependsGraph(courses));

        errorMessages.addAll(analyzeProfStandartsReaching(trajectories, program, courses));

        return new AnalyzeResult(errorMessages);
    }

    private List<AnalyzeMessage> analyzeProfStandartsReaching(List<Trajectory> trajectories, BasicEducationProgram program, Map<Long, FullCourseInfo> courses) {
        List<AnalyzeMessage> messages = new ArrayList<>();

        Set<ProfessionalStandard> allReachedProfessionalStandarts = new TreeSet<>();

        trajectories.forEach(trajectory -> {
            List<ProfessionalStandard> professionalStandards = trajectoryService.getProfessionalStandardsReachedBy(trajectory);
            if(professionalStandards.isEmpty()){
                messages.add(new AnalyzeMessage(AnalyzeMessage.Type.ERROR, String.format("Траектория [%s] не достигает ни одного проф. стандарта", trajectoryToString(trajectory))));
            }

            allReachedProfessionalStandarts.addAll(professionalStandards);
        });

        standardRepository.findAll().stream().filter(s->!allReachedProfessionalStandarts.contains(s)).forEach(s->{
            messages.add(new AnalyzeMessage(AnalyzeMessage.Type.WARNING, String.format("Ни одна траектория не достигает проф. стандарта \"%s\"", s.getName())));
        });

        return messages;
    }

    private List<AnalyzeMessage> analyzeDependsGraph(Map<Long, FullCourseInfo> courses) {
        List<AnalyzeMessage> messages = new ArrayList<>();

        for (FullCourseInfo course : courses.values()) {
            Set<Long> previousCources = new TreeSet<>(course.getPreviousCourses());

            Set<Long> checkedCourses = new TreeSet<>();
            List<Long> queue = new ArrayList<>(course.getPreviousCourses());

            while (!queue.isEmpty()) {
                Long id = queue.remove(0);

                if (checkedCourses.contains(id)) {
                    continue;
                }

                List<Long> tempPrevious = courses.get(id).getPreviousCourses();
                tempPrevious.forEach(tempId -> {
                    if (checkedCourses.contains(id) || queue.contains(id)) {
                        return;
                    }

                    queue.add(tempId);
                });

                previousCources.addAll(tempPrevious);
                checkedCourses.add(id);
            }

            List<FullCourseInfo> badSemester = course.getPreviousCourses().stream().filter(pCourse -> courses.get(pCourse).getCourse().getSemester() >= course.getCourse().getSemester()).map(courses::get).collect(Collectors.toList());
            badSemester.forEach(badCourse -> {
                messages.add(new AnalyzeMessage(AnalyzeMessage.Type.ERROR, String.format("Дисциплина \"%s\" (%d семестр) требует предварительного изучения дисциплины \"%s\" (%d семестр)",
                        course.getCourse().getName(),
                        course.getCourse().getSemester(),
                        badCourse.getCourse().getName(),
                        badCourse.getCourse().getSemester())));
            });


            Map<Long, List<FullCourseInfo>> previousByBlocks = new TreeMap<>();
            previousCources.stream().map(courses::get).filter(FullCourseInfo::isImplementsTemplate).forEach(c -> {
                long blockId = c.getTemplateCourse();
                previousByBlocks.putIfAbsent(blockId, new ArrayList<>());
                previousByBlocks.get(blockId).add(c);
            });

            previousByBlocks.keySet().stream().filter(key -> previousByBlocks.get(key).size() > 1).forEach(key -> {
                List<FullCourseInfo> previousInOneBlock = previousByBlocks.get(key);

                messages.add(new AnalyzeMessage(AnalyzeMessage.Type.ERROR, String.format("Дисциплина \"%s\" требует предварительного изучения дисциплин [%s] из одного блока.",
                        course.getCourse().getName(),
                        StringUtils.join(previousInOneBlock.stream().map(c -> String.format("\"%s\"", c.getCourse().getName())).collect(Collectors.toList()), ", "))));
            });
        }

        return messages;
    }

    private List<AnalyzeMessage> analyzeTrajectoriesAndCompetences(List<Trajectory> trajectories, BasicEducationProgram program, Fgos fgos, Map<Long, FullCourseInfo> courses) {
        List<AnalyzeMessage> messages = new ArrayList<>();

        Set<Integer> mustBeDeveloped = fgos.getRequireCompetence().stream().map(competence -> (int) (long) competence.getId()).collect(Collectors.toSet());
        Map<Long, Competence> competencesById = new TreeMap<>();
        fgos.getRequireCompetence().forEach(competence -> competencesById.put(competence.getId(), competence));

        // Todo: возможно, в neo4j есть возможность в виде ответа выдавать Map<Trajectory, List<>>
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

            String trajectoryString = trajectoryToString(trajectory);
            String competencesString = StringUtils.join(notDeveloped.stream().map(id -> String.format("\"%s\"", competencesById.get((long) (int) id).getCode())).collect(Collectors.toList()), ", ");

            messages.add(new AnalyzeMessage(AnalyzeMessage.Type.ERROR, String.format("Траектория \"%s\", не формирует компетенции [%s]", trajectoryString, competencesString)));
        }

        if (messages.isEmpty()) {
            messages.add(new AnalyzeMessage(AnalyzeMessage.Type.INFO, "Все компетенции формируемы при любой траектории."));
        }

        return messages;
    }

    private String trajectoryToString(Trajectory trajectory) {
        return StringUtils.join(trajectory.getCourses().stream().filter(FullCourseInfo::isImplementsTemplate).map(course -> course.getCourse().getName()).collect(Collectors.toList()), ", ");
    }

    private List<AnalyzeMessage> analyzeRequiredCourses(BasicEducationProgram program, Map<Long, FullCourseInfo> courses) {
        List<AnalyzeMessage> messages = new ArrayList<>();

        List<FgosCourseRequirement> containsRequiredCourses = analyzeRepository.getContainsRequiredCourses(program);

        List<FgosCourseRequirement> notFoundRequiredCourses = new ArrayList<>(program.getFgos().getRequireCourses());
        notFoundRequiredCourses.removeAll(containsRequiredCourses);

        notFoundRequiredCourses.forEach(course -> {
            messages.add(new AnalyzeMessage(AnalyzeMessage.Type.WARNING, String.format("Не найдена обязательная дисциплина \"%s\"", course.getName())));
        });

        if (messages.isEmpty()) {
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
}
