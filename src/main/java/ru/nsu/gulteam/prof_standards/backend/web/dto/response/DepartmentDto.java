package ru.nsu.gulteam.prof_standards.backend.web.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class DepartmentDto {
    @NotNull
    private long id;

    @NotNull
    private String name;
}
