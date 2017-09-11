package ru.nsu.gulteam.prof_standards.backend.exception;

import org.springframework.security.core.AuthenticationException;

public class NotAuthorizedException extends AuthenticationException {
    public NotAuthorizedException(String msg, Throwable t) {
        super(msg, t);
    }

    public NotAuthorizedException(String msg) {
        super(msg);
    }
}
