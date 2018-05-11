package ru.nsu.gulteam.prof_standards.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Fgos;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullFgosInfo {
    private Fgos fgos;
    private boolean canUpdate;
    private boolean canDelete;
    private FullUserInfo fullCreator;
}
