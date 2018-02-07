package ru.nsu.gulteam.prof_standards.backend.web.dto.request;

import lombok.Data;

@Data
public class RegisterData {
    private String firstName;
    private String secondName;
    private AuthData authData;
}
