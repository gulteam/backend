package ru.nsu.gulteam.prof_standards.backend.exception;

import org.springframework.security.core.AuthenticationException;

public class RegisterException  extends AuthenticationException {
    public RegisterException(String msg, Throwable t) {
        super(msg, t);
    }

    public RegisterException(String msg) {
        super(msg);
    }
}