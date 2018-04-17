package ru.nsu.gulteam.prof_standards.backend.web.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class BasicEducationProgramDto {
    @NotNull
    private int id;
    @NotNull
    private String name;
    @NotNull
    private boolean canEdit;
    @NotNull
    private FacultyDto faculty;
    @NotNull
    private UserDto createdBy;
}
