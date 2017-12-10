package ru.nsu.gulteam.prof_standards.backend.domain.node;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity(label = "TOKEN")
public class Token {
    @GraphId
    @Property(name = "ID")
    private Long id;

    @Property(name = "DATA")
    private String data;

    public Token() {
    }

    public Token(String data) {
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
