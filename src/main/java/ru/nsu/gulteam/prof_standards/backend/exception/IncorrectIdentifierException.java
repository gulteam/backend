package ru.nsu.gulteam.prof_standards.backend.exception;

import org.springframework.security.core.AuthenticationException;

public class IncorrectIdentifierException extends AuthenticationException {
    public IncorrectIdentifierException(String msg, Throwable t) {
        super(msg, t);
    }

    public IncorrectIdentifierException(String msg) {
        super(msg);
    }
}
