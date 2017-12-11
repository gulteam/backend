package ru.nsu.gulteam.prof_standards.backend.web.dto.response;

import org.neo4j.ogm.annotation.Property;

public class BasicEducationProgramDto {
    private int id;
    private String name;

    public BasicEducationProgramDto() {
    }

    public BasicEducationProgramDto(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
