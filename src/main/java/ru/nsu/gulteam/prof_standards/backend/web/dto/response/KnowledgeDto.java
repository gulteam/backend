package ru.nsu.gulteam.prof_standards.backend.web.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class KnowledgeDto {
    @NotNull
    private long id;

    @NotNull
    private String description;

    public KnowledgeDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
