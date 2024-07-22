package com.desafio.communityiotdevice.config.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomHttpExceptionHandler {

    @ExceptionHandler(CustomHttpException.class)
    public ResponseEntity<?> handleCustomHttpException(CustomHttpException e) {
        ExceptionDetails exceptionDetails = new ExceptionDetails();
        exceptionDetails.setMessage(e.getMessage());
        exceptionDetails.setStatus(e.getStatus().value());
        return new ResponseEntity<>(exceptionDetails, e.getStatus());
    }

}
