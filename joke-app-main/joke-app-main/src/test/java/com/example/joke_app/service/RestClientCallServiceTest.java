package com.example.joke_app.service;

import com.example.joke_app.dto.JokesDto;
import com.example.joke_app.exception.GlobalExceptionHandler;
import com.example.joke_app.exception.RandomJokeFetchException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestClientCallServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RestClientCallService restClientCallService;

    @Test
    public void testRestClientStatusIsNotOk() {
        when(restTemplate.getForEntity(anyString(), eq(JokesDto.class)))
                .thenReturn(ResponseEntity.badRequest().body(new JokesDto()));
        assertThrows(RandomJokeFetchException.class, () -> restClientCallService.fetchJokes(8));
    }

    @Test
    void testResourceAccessException() {
        when(restTemplate.getForEntity(anyString(), eq(JokesDto.class)))
                .thenThrow(new ResourceAccessException("error!!!"));
        assertThrows(RandomJokeFetchException.class, () -> restClientCallService.fetchJokes(5));
    }

    @Test
    void testRestClientCallException() {
        when(restTemplate.getForEntity(anyString(), eq(JokesDto.class)))
                .thenThrow(new RestClientException("ServerSide error"));
        assertThrows(RandomJokeFetchException.class, () -> restClientCallService.fetchJokes(9));
    }

    @Test
    void testBatch_ReturnCorrentBatch() {
        JokesDto mockJoke = new JokesDto(1L, "Setup RandomJoke", "Punchline RandomJoke", "type RandomJoke");
        when(restTemplate.getForEntity(anyString(), eq(JokesDto.class))).thenReturn(ResponseEntity.ok(mockJoke));

        List<JokesDto> mockResponse = restClientCallService.fetchJokes(20);

        assertEquals(20, mockResponse.size());
        verify(restTemplate, times(20)).getForEntity(anyString(), eq(JokesDto.class));
    }

    @Test
    void testFetchJokeExceptionHandlerDirectly() {
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
        RandomJokeFetchException exception = new RandomJokeFetchException("Invalid!!!");

        ResponseEntity<String> response = exceptionHandler.jokeFetchException(exception);

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("Service failed: Invalid!!!", response.getBody());
    }
}
