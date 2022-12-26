package com.example.megalab.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;


public class JwtAuthenticationException extends AuthenticationException {

    private HttpStatus httpStatus;

    public JwtAuthenticationException(String msg) {
        super(msg);
    }

    public JwtAuthenticationException(String s, HttpStatus httpStatus) {
        super(s);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
