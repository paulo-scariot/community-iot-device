package com.desafio.communityiotdevice.config.exception;

import org.springframework.http.HttpStatus;

public class CustomHttpException extends RuntimeException {
    private final HttpStatus status;

    public CustomHttpException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
