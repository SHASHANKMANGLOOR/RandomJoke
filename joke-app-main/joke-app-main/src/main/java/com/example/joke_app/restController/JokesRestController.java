package com.example.joke_app.restController;

import com.example.joke_app.dto.JokesResponseDTO;
import com.example.joke_app.service.JokesService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JokesRestController {

    private static final Logger logger = LoggerFactory.getLogger(JokesRestController.class);
    @Autowired
    JokesService jokeService;

    @GetMapping("/jokes")
    public ResponseEntity<List<JokesResponseDTO>> fetchjokesbyCount
            (@RequestParam @Valid @Min(value = 1, message = "Minimum value of 1") @NotNull(message = "Count value cant be null") int count) {
        logger.info("Entered GetMapping method");
        return ResponseEntity.ok(jokeService.getJokes(count));
    }
}
