package com.example.joke_app.service;

import com.example.joke_app.dto.JokesDto;
import com.example.joke_app.dto.JokesResponseDTO;
import com.example.joke_app.exception.RandomJokeFetchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private JokesPersistService persistService;

    @Mock
    private RestClientCallService restClientCallService;

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

        doNothing().when(persistService).saveJokes(anyList());
        List<JokesResponseDTO> mockResponse = service.getJokes(12);
        assertEquals(12, mockResponse.size());

        verify(persistService, times(2)).saveJokes(anyList());
    }

    @Test
    public void testgetJokesExceptionParral() {
        when(restClientCallService.fetchJokes(anyInt()))
                .thenThrow(new RuntimeException("Error fetching jokes"));

        assertThrows(RandomJokeFetchException.class, () -> service.getJokes(1));
    }
}