package com.example.joke_app.service;

import com.example.joke_app.dto.JokesDto;
import com.example.joke_app.entity.RandomJoke;
import com.example.joke_app.exception.InvalidRandomJokesException;
import com.example.joke_app.repository.JokesRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JokesPersistService {

    private static final Logger logger = LoggerFactory.getLogger(JokesPersistService.class);
    @Autowired
    JokesRepository jokesRepository;

    public void saveJokes(List<JokesDto> jokesDto) {
        logger.info("Entered saveJokes Method");
        if (ObjectUtils.isEmpty(jokesDto)) {
            throw new InvalidRandomJokesException("No Jokes are present");
        }

        List<RandomJoke> jokesToSave = jokesDto.stream()
                .map(RandomJoke::new)
                .filter(joke -> !jokesRepository.existsById(joke.getId()))
                .toList();
        jokesRepository.saveAll(jokesToSave);
        logger.info("saved!!!");
        logger.info("Exiting saveJokes Method");
    }
}
