package ru.nsu.gulteam.prof_standards.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trajectory {
    private List<FullCourseInfo> courses;
}
