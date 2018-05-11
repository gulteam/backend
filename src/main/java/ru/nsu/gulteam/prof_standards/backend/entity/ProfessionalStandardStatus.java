package ru.nsu.gulteam.prof_standards.backend.entity;

import lombok.Data;
import ru.nsu.gulteam.prof_standards.backend.domain.node.ProfessionalStandard;

@Data
public class ProfessionalStandardStatus {
    ProfessionalStandard professionalStandard;
    String status;
}
