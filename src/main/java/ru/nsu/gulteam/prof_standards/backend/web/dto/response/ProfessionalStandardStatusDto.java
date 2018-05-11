package ru.nsu.gulteam.prof_standards.backend.web.dto.response;

import lombok.Data;

@Data
public class ProfessionalStandardStatusDto {
    ProfessionalStandardDto professionalStandard;
    String status;
}
