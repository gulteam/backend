package ru.nsu.gulteam.prof_standards.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Block;

import java.util.List;

@Data
@NoArgsConstructor
public class FullBlockInfo {
    private Block block;
    private List<FullCourseInfo> courses;
    private boolean canEdit;

    public FullBlockInfo(Block block, List<FullCourseInfo> courses) {
        this.block = block;
        this.courses = courses;
    }
}
