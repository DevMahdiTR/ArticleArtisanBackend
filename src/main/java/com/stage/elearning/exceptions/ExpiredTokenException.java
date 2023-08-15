package com.stage.elearning.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ExpiredTokenException extends  RuntimeException {
    public ExpiredTokenException(String msg) { super(msg); }
}

