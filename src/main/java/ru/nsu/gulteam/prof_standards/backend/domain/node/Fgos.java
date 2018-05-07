package ru.nsu.gulteam.prof_standards.backend.domain.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;
import java.util.TreeSet;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@NodeEntity(label = "FGOS")
public class Fgos {
    @GraphId
    @Property(name = "ID")
    private Long id;

    @Property(name = "CODE")
    private String code;

    @Property(name = "NAME")
    private String name;

    @Relationship(type="REQUIRE")
    Set<Competence> requireCompetence = new TreeSet<>();

    @Property(name = "DISCIPLINE_VOLUME_FROM")
    private double disciplineVolumeFrom;

    @Property(name = "PRACTICE_VOLUME_TO")
    private double practiceVolumeFrom;

    @Property(name = "ATTESTATION_VOLUME_FROM")
    private double attestationVolumeFrom;

    @Property(name = "SUMMARY_VOLUME")
    private double summaryVolume;

    @Relationship(type="REQUIRE")
    Set<FgosCourseRequirement> requireCourses = new TreeSet<>();

    public Fgos(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
