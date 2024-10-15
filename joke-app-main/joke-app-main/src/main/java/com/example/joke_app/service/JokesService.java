package com.example.joke_app.service;

import com.example.joke_app.dto.JokesDto;
import com.example.joke_app.dto.JokesResponseDTO;
import com.example.joke_app.entity.RandomJoke;
import com.example.joke_app.exception.ServiceExcpetion;
import com.example.joke_app.repository.JokesRepository;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.example.joke_app.errorConstants.ErrorConstants.FAILED_FETCH;

@Service
public class JokesService {

    private static final Logger logger = LoggerFactory.getLogger(JokesService.class);

    @Autowired
    private RestClientCallService restClientCallService;

    @Autowired
    JokesRepository jokesRepository;

    public List<JokesResponseDTO> getJokes(final int count) {
        logger.info("Entered getJokes Method");
        List<JokesDto> jokesDto = new ArrayList<>();
        final int batches = (int) Math.ceil((double) count / 10);

        final List<List<JokesDto>> list = IntStream.range(0, batches)
                .mapToObj(i -> {
                    int batchSize = Math.min(10, count - i * 10);
                    return restClientCallService.fetchJokes(batchSize);
                })
                .toList();

        list.forEach(i -> {
            try {
                final List<JokesDto> batch = i.stream().toList();
                jokesDto.addAll(batch);
                saveJokes(batch);
            } catch (Exception e) {
                throw new ServiceExcpetion(FAILED_FETCH + e.getMessage());
            }
        });
        logger.info("Exiting getJokes Method");
        return jokesDto.stream().map(JokesResponseDTO::new).toList();
    }
    public void saveJokes(@NotNull final List<JokesDto> jokesDto) {
        logger.info("Entered saveJokes Method");
        List<RandomJoke> jokesToSave = jokesDto.stream()
                .map(RandomJoke::new)
                .filter(joke -> !jokesRepository.existsById(joke.getId()))
                .toList();
        jokesRepository.saveAll(jokesToSave);
        logger.info("saved!!!");
        logger.info("Exiting saveJokes Method");
    }
}
