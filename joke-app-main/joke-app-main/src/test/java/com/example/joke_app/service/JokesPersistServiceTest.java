package com.example.joke_app.service;

import com.example.joke_app.dto.JokesDto;
import com.example.joke_app.entity.RandomJoke;
import com.example.joke_app.exception.GlobalExceptionHandler;
import com.example.joke_app.exception.InvalidRandomJokesException;
import com.example.joke_app.repository.JokesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class JokesPersistServiceTest {

    @Mock
    private JokesRepository jokesRepository;

    @InjectMocks
    private JokesPersistService jokesPersistService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveJokesSuccessful() {
        JokesDto jokeDto1 = new JokesDto(1L, "Setup Joke 1", "Punchline Joke 1", "type");
        JokesDto jokeDto2 = new JokesDto(2L, "Setup Joke 2", "Punchline Joke 2", "type");
        List<JokesDto> jokesDto = Arrays.asList(jokeDto1, jokeDto2);

        jokesPersistService.saveJokes(jokesDto);

        ArgumentCaptor<List<RandomJoke>> jokeCaptor = ArgumentCaptor.forClass(List.class);
        verify(jokesRepository, times(1)).saveAll(jokeCaptor.capture());
        List<RandomJoke> savedJokes = jokeCaptor.getValue();

        assertEquals(2, savedJokes.size());
        assertEquals("Setup Joke 1", savedJokes.get(0).getSetup());
        assertEquals("Punchline Joke 1", savedJokes.get(0).getPunchline());
        assertEquals("Setup Joke 2", savedJokes.get(1).getSetup());
        assertEquals("Punchline Joke 2", savedJokes.get(1).getPunchline());
    }

    @Test
    void testSaveJokesNullCase() {
        List<JokesDto> jokesDto = null;

        InvalidRandomJokesException thrown = assertThrows(InvalidRandomJokesException.class,
                () -> jokesPersistService.saveJokes(jokesDto));
        assertEquals("No Jokes are present", thrown.getMessage());
    }

    @Test
    void testInvalidJoke() {
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
        InvalidRandomJokesException exception = new InvalidRandomJokesException("No Jokes");

        ResponseEntity<String> response = exceptionHandler.invalidJokesException(exception);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("No Content: No Jokes", response.getBody());
    }

}
