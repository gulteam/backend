package ru.nsu.gulteam.prof_standards.backend.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.nsu.gulteam.prof_standards.backend.exception.NotAuthorizedException;

import javax.servlet.http.HttpServletRequest;

public class BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @ExceptionHandler({AccessDeniedException.class})
    @ResponseBody
    public ResponseEntity<?> handleException(HttpServletRequest request, AccessDeniedException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("Access denied");
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ResponseEntity<?> handlerException(HttpServletRequest request, Throwable throwable) {
        if (logger.isErrorEnabled()) {
            logger.error("Internal server error", throwable);
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal server error");
    }
}
