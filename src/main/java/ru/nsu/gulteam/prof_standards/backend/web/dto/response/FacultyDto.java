package ru.nsu.gulteam.prof_standards.backend.web.dto.response;

import javax.validation.constraints.NotNull;
import java.util.List;

public class FacultyDto {
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
