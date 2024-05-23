package com.desafio.communityiotdevice.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionGlobalHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException e) {
        ExceptionDetails exceptionDetails = new ExceptionDetails();
        exceptionDetails.setMessage(e.getMessage());
        exceptionDetails.setStatus(HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(exceptionDetails, HttpStatus.BAD_REQUEST);
    }

}
