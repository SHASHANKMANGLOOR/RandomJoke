package com.example.joke_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.joke_app.errorConstants.ErrorConstants.NO_CONTENT;
import static com.example.joke_app.errorConstants.ErrorConstants.SERVICE_UNAVAILABLE;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceExcpetion.class)
    public ResponseEntity<String> ServiceUnavailableExcpetion(ServiceExcpetion ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                SERVICE_UNAVAILABLE + ex.getMessage()
        );
    }

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<String> noContentException(NoContentException ex) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                NO_CONTENT + ex.getMessage()
        );
    }
}