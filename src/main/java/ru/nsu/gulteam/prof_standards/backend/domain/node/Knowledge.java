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
@NodeEntity(label = "KNOWLEDGE")
public class Knowledge implements Comparable<Knowledge> {
    @GraphId
    @Property(name = "ID")
    private Long id;

    @Property(name = "DESCRIPTION")
    private String description;

    public Knowledge(String description) {
        this.description = description;
    }

    @Override
    public int compareTo(Knowledge o) {
        return (int)(id - o.id);
    }
}
