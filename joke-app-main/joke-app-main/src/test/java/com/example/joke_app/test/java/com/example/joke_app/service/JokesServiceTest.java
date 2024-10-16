package com.example.joke_app.service;

import com.example.joke_app.dto.JokesDto;
import com.example.joke_app.dto.JokesResponseDTO;
import com.example.joke_app.entity.RandomJoke;
import com.example.joke_app.exception.GlobalExceptionHandler;
import com.example.joke_app.exception.NoContentException;
import com.example.joke_app.repository.JokesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JokesServiceTest {

    @InjectMocks
    private JokesService service;

    @Mock
    private RestClientCallService restClientCallService;

    @Mock
    private JokesRepository jokesRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testgetJokesSuccess() {
        List<JokesDto> mockBatch = IntStream.range(0, 10)
                .mapToObj(i -> new JokesDto((long) i, "Setup " + i, "Punchline " + i, "type"))
                .toList();

        when(restClientCallService.fetchJokes(10)).thenReturn(mockBatch);
        when(restClientCallService.fetchJokes(2)).thenReturn(mockBatch.subList(0, 2));

        List<JokesResponseDTO> mockResponse = service.getJokes(12);
        assertEquals(12, mockResponse.size());
    }


    @Test
    void testSaveJokesSuccess() {
        JokesDto jokeDto1 = new JokesDto(1L, "Setup Joke 1", "Punchline Joke 1", "type");
        JokesDto jokeDto2 = new JokesDto(2L, "Setup Joke 2", "Punchline Joke 2", "type");
        List<JokesDto> jokesDto = Arrays.asList(jokeDto1, jokeDto2);

        service.saveJokes(jokesDto);

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
        assertThrows(NullPointerException.class,
                () -> service.saveJokes(jokesDto));
    }

    @Test
    void testNocontentJoke() {
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
        NoContentException exception = new NoContentException("No Jokes");

        ResponseEntity<String> response = exceptionHandler.noContentException(exception);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("No Content: No Jokes", response.getBody());
    }

}
