package ru.nsu.gulteam.prof_standards.backend.domain.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@NodeEntity(label = "DEPARTMENT")
public class Department {
    @GraphId
    @Property(name = "ID")
    private Long id;

    @Property(name = "NAME")
    private String name;

    public Department(String name) {
        this.name = name;
    }
}
