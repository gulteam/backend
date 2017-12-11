package ru.nsu.gulteam.prof_standards.backend.domain.node;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

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

    public LaborFunction() {
    }

    public LaborFunction(String name, String code, int qualificationLevel) {
        this.name = name;
        this.code = code;
        this.qualificationLevel = qualificationLevel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getQualificationLevel() {
        return qualificationLevel;
    }

    public void setQualificationLevel(int qualificationLevel) {
        this.qualificationLevel = qualificationLevel;
    }
}
