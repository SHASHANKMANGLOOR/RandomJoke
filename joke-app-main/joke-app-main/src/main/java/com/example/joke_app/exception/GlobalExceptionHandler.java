package com.example.joke_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.joke_app.errorConstants.ErrorConstants.NO_CONTENT;
import static com.example.joke_app.errorConstants.ErrorConstants.SERVICE_UNAVAILABLE;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RandomJokeFetchException.class)
    public ResponseEntity<String> jokeFetchException(RandomJokeFetchException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                SERVICE_UNAVAILABLE + ex.getMessage()
        );
    }

    @ExceptionHandler(InvalidRandomJokesException.class)
    public ResponseEntity<String> invalidJokesException(InvalidRandomJokesException ex) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                NO_CONTENT+ ex.getMessage()
        );
    }
}