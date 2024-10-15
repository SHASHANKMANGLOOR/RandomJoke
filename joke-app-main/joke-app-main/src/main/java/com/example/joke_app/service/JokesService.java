package com.example.joke_app.service;

import com.example.joke_app.dto.JokesDto;
import com.example.joke_app.dto.JokesResponseDTO;
import com.example.joke_app.exception.RandomJokeFetchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

import static com.example.joke_app.errorConstants.ErrorConstants.PARALLEL_ERROR;

@Service
public class JokesService {

    private static final Logger logger = LoggerFactory.getLogger(JokesService.class);
    @Autowired
    private JokesPersistService jokesPersistService;
    @Autowired
    private RestClientCallService restClientCallService;

    public List<JokesResponseDTO> getJokes(final int count) {
        logger.info("Entered getJokes Method");
        List<JokesDto> jokesDto = new ArrayList<>();
        final int batches = (int) Math.ceil((double) count / 10);

        final List<CompletableFuture<List<JokesDto>>> futures = IntStream.range(0, batches)
                .mapToObj(i -> {
                    int batchSize = Math.min(10, count - i * 10);
                    return CompletableFuture.supplyAsync(() -> restClientCallService.fetchJokes(batchSize));
                })
                .toList();

        futures.forEach(future -> {
            try {
                final List<JokesDto> batch = future.get();
                jokesDto.addAll(batch);
                jokesPersistService.saveJokes(batch);
            } catch (ExecutionException | InterruptedException e) {
                throw new RandomJokeFetchException(PARALLEL_ERROR + e.getMessage());
            }
        });
        logger.info("Exiting getJokes Method");
        return jokesDto.stream().map(JokesResponseDTO::new).toList();
    }
}
