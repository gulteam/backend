package ru.nsu.gulteam.prof_standards.backend.domain.node;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity(label = "PROFESSIONAL_STANDARD")
public class ProfessionalStandard {
    @GraphId
    @Property(name = "ID")
    private Long id;

    @Property(name = "NAME")
    private String name;

    @Property(name = "CODE")
    private String code;

    public ProfessionalStandard() {
    }

    public ProfessionalStandard(String name, String code) {
        this.name = name;
        this.code = code;
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

    @Override
    public String toString() {
        return "ProfessionalStandard{" +
                "name='" + name + '\'' +
                '}';
    }
}
