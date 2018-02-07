package ru.nsu.gulteam.prof_standards.backend.web.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    @NotNull
    private Long id;

    /**
     * Имя
     */
    @NotNull
    private String firstName;

    /**
     * Фамилия
     */
    @NotNull
    private String secondName;

    /**
     * Логин
     */
    @NotNull
    private String login;

    @NotNull
    private RoleDto role;

    private FacultyDto faculty;

    private DepartmentDto department;
}
