package com.example.joke_app.service;

import com.example.joke_app.dto.JokesResponseDTO;
import com.example.joke_app.restController.JokesRestController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(JokesRestController.class)
public class JokeRestControllerTest {

    @Autowired
    private MockMvc mock;

    @MockBean
    private JokesService service;

    @Test
    public void testJokesList() throws Exception {
        List<JokesResponseDTO> mockJokes =
                Arrays.asList(new JokesResponseDTO(1L, "Setup 1", "Punchline 1"), new JokesResponseDTO(2L, "Setup 2", "Punchline 2"));

        when(service.getJokes(2)).thenReturn(mockJokes);

        mock.perform(get("/jokes?count=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].question").value("Setup 1"))
                .andExpect(jsonPath("$.[0].answer").value("Punchline 1"))
                .andExpect(jsonPath("$.[1].question").value("Setup 2"))
                .andExpect(jsonPath("$.[1].answer").value("Punchline 2"));
    }
}