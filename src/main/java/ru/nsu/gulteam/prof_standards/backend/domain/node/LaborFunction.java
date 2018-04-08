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
@NodeEntity(label = "LABOR_FUNCTION")
public class LaborFunction {
    @GraphId
    @Property(name = "ID")
    private Long id;

    @Property(name = "NAME")
    private String name;

    @Property(name = "CODE")
    private String code;

    @Property(name = "QUALIFICATION_LEVEL")
    private int qualificationLevel;

    public LaborFunction(String name, String code, int qualificationLevel) {
        this.name = name;
        this.code = code;
        this.qualificationLevel = qualificationLevel;
    }
}
