package ru.nsu.gulteam.prof_standards.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Faculty;
import ru.nsu.gulteam.prof_standards.backend.domain.node.User;

@AllArgsConstructor
public class FullBasicEducationProgramInfo {
    @Getter
    private int id;
    @Getter
    private String name;
    @Getter
    private boolean canEdit;
    @Getter
    private Faculty faculty;
    @Getter
    private User createdBy;
}
