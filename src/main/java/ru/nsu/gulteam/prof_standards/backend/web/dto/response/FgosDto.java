package ru.nsu.gulteam.prof_standards.backend.web.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Competence;
import ru.nsu.gulteam.prof_standards.backend.domain.node.FgosCourseRequirement;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
public class FgosDto {
    @NotNull
    private long id;

    @NotNull
    private String code;

    @NotNull
    private String name;

    @NotNull
    private Set<CompetenceDto> requireCompetence;

    @NotNull
    private double disciplineVolumeFrom;

    @NotNull
    private double practiceVolumeFrom;

    @NotNull
    private double attestationVolumeFrom;

    @NotNull
    private double summaryVolume;

    @NotNull
    private Set<FgosCourseRequirementDto> requireCourses;
}
