package ru.nsu.gulteam.prof_standards.backend.web.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Fgos;

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
    @NotNull
    private FgosDto fgos;
}
