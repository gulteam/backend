package ru.nsu.gulteam.prof_standards.backend.web.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nsu.gulteam.prof_standards.backend.domain.type.AttestationForm;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class BlockDto {
    @NotNull
    private long id;
    @NotNull
    private int amount;
    @NotNull
    private int semester;
    @NotNull
    private AttestationForm attestationForm;
    @NotNull
    private boolean canUpdate;
}
