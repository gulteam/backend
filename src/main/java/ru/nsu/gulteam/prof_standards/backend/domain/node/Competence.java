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
@NodeEntity(label = "COMPETENCE")
public class Competence implements Comparable<Competence> {
    @GraphId
    @Property(name = "ID")
    private Long id;

    @Property(name = "CODE")
    private String code;

    @Property(name = "DESCRIPTION")
    private String description;

    public Competence(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public int compareTo(Competence o) {
        return (int)(id - o.id);
    }
}
