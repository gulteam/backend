package ru.nsu.gulteam.prof_standards.backend.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AnalyzeMessage {
    public enum Type {INFO, WARNING, ERROR}

    private final Type type;
    private final String message;
}
