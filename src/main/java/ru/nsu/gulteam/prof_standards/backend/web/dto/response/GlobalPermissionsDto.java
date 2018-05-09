package ru.nsu.gulteam.prof_standards.backend.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalPermissionsDto {
    @NotNull
    private boolean canCreateProgram;

    @NotNull
    private boolean canReadProgramList;

    @NotNull
    private boolean canCreateFgos;

    @NotNull
    private boolean canReadFgosList;

    @NotNull
    private boolean canEditFacultiesAndDepartments;
}
