package com.example.joke_app.service;

import com.example.joke_app.dto.JokesDto;
import com.example.joke_app.exception.ServiceExcpetion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.example.joke_app.errorConstants.ErrorConstants.FAILED_FETCH;
import static com.example.joke_app.errorConstants.ErrorConstants.SERVER_ERROR;

@Service
public class RestClientCallService {

    private static final String URL = "https://official-joke-api.appspot.com/random_joke";
    private static final Logger logger = LoggerFactory.getLogger(RestClientCallService.class);
    @Autowired
    private RestTemplate restTemplate;

    public List<JokesDto> fetchJokes(final int batchSize) {
        logger.info("Inside fetchJokes method to perform RestClient Call");
        final List<JokesDto> jokeDtoList = IntStream.range(0, batchSize)
                .parallel()
                .mapToObj(i -> {
                    try {
                        ResponseEntity<JokesDto> response = restTemplate.getForEntity(URL, JokesDto.class);
                        if (response.getStatusCode().equals(HttpStatus.OK)) {
                            return response.getBody();
                        } else {
                            throw new ServiceExcpetion(FAILED_FETCH + response.getStatusCode());
                        }
                    } catch (RestClientException exception) {
                        throw new ServiceExcpetion(SERVER_ERROR + exception.getMessage());
                    } catch (Exception exception) {
                        throw new ServiceExcpetion(exception.getMessage());
                    }
                })
                .filter(joke -> joke != null)
                .collect(Collectors.toList());
        return jokeDtoList;
    }

}
