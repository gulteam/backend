package ru.nsu.gulteam.prof_standards.backend.web.dto.response;

import ru.nsu.gulteam.prof_standards.backend.domain.type.UserRole;

import javax.validation.constraints.NotNull;

public class DepartmentDto {
    @NotNull
    private long id;

    @NotNull
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
