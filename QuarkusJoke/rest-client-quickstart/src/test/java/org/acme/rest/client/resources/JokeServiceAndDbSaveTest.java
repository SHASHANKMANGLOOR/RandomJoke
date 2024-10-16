package org.acme.rest.client.resources;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import jakarta.inject.Inject;
import org.acme.rest.client.dto.JokeDTO;
import org.acme.rest.client.service.RandomJokeDBService;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
public class JokeServiceAndDbSaveTest {

    @Inject
    RandomJokeDBService jokeService;

    @Test
    public void testSaveJoke() {
        // Create a JokeDTO object
        JokeDTO jokeDTO = new JokeDTO();
        jokeDTO.setId(1L);
        jokeDTO.setSetup("Test Joke");
        jokeDTO.setPunchline("test, joke");
        jokeDTO.setType("test, joke");

        // Call the saveJoke method
        jokeService.saveJoke(jokeDTO);

        // Verify that the joke was saved
        given()
                .when().get("/jokes?count=12")
                .then()
                .statusCode(200);

        given()
                .when().get("/jokes?count=0")
                .then()
                .statusCode(400);
    }
}