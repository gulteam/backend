package ru.nsu.gulteam.prof_standards.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Block;

@Data
@NoArgsConstructor
public class FullBlockInfo {
    private Block course;
    private boolean canEdit;
}
