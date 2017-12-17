package ru.nsu.gulteam.prof_standards.backend.web.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import ru.nsu.gulteam.prof_standards.backend.domain.type.UserRole;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDto {
    @NotNull
    private long id;

    @NotNull
    private UserRole name;

    public RoleDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserRole getName() {
        return name;
    }

    public void setName(UserRole name) {
        this.name = name;
    }
}
