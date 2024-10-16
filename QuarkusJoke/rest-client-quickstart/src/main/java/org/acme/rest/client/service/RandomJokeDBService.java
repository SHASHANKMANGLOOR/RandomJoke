package org.acme.rest.client.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.rest.client.dto.JokeDTO;
import org.acme.rest.client.entity.RandomJoke;
import org.acme.rest.client.repository.JokeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class RandomJokeDBService {

    private static final Logger logger = LoggerFactory.getLogger(RandomJokeService.class);

    @Inject
    JokeRepository jokeRepository;
    public void saveJoke(JokeDTO joke){
        logger.info("saveJoke");
        RandomJoke randomJoke = new RandomJoke(joke);
        jokeRepository.create(randomJoke);
    }
}
