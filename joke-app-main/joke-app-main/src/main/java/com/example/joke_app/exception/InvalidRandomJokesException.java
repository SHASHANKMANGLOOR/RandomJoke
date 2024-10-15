package com.example.joke_app.exception;

public class InvalidRandomJokesException extends RuntimeException {
    public InvalidRandomJokesException(String message) {
        super(message);
    }
}
