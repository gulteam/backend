package ru.nsu.gulteam.prof_standards.backend.domain.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@NodeEntity(label = "BASIC_EDUCATION_PROGRAM")
public class BasicEducationProgram {
    @GraphId
    @Property(name = "ID")
    private Long id;

    @Property(name = "NAME")
    private String name;

    @Relationship(type="BASED_ON", direction = Relationship.OUTGOING)
    private Fgos fgos;

    public BasicEducationProgram(String name) {
        this.name = name;
    }
}
