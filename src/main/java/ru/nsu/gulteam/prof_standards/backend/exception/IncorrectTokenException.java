package ru.nsu.gulteam.prof_standards.backend.exception;

import org.springframework.security.core.AuthenticationException;

public class IncorrectTokenException extends AuthenticationException {
    public IncorrectTokenException(String msg, Throwable t) {
        super(msg, t);
    }

    public IncorrectTokenException(String msg) {
        super(msg);
    }
}
