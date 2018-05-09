package ru.nsu.gulteam.prof_standards.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Faculty;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Fgos;
import ru.nsu.gulteam.prof_standards.backend.domain.node.User;

@AllArgsConstructor
public class FullBasicEducationProgramInfo {
    @Getter
    private int id;
    @Getter
    private String name;
    @Getter
    private Faculty faculty;
    @Getter
    private User createdBy;
    @Getter
    private Fgos fgos;

    @Getter
    private boolean canUpdate;
    @Getter
    private boolean canDelete;
    @Getter
    private boolean canAddVariableCourse;
}
