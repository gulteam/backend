package ru.nsu.gulteam.prof_standards.backend.entity;

import java.util.List;

public class AnalyzeResult {
    private List<String> errorMessages;

    public AnalyzeResult() {
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public AnalyzeResult(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }
}
