package ru.nsu.gulteam.prof_standards.backend.web.dto.request;

import lombok.Data;

@Data
public class AuthData {
    private String login;
    private String password;
}
