package ru.nsu.gulteam.prof_standards.backend.web.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SkillsDto {
    @NotNull
    private long id;

    @NotNull
    private String description;
}
