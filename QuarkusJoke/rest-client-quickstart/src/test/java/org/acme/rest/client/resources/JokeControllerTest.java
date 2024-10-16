package org.acme.rest.client.resources;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
public class JokeControllerTest {


    @Test
    public void testFetchJokesByCount() {
        // Test with valid count
        given()
                .when().get("/jokes?count=5")
                .then()
                .statusCode(200)
                .body(notNullValue());

        given()
                .when().get("/jokes?count=12")
                .then()
                .statusCode(200)
                .body(notNullValue());

        // Test with invalid count (less than 1)
        given()
                .when().get("/jokes?count=0")
                .then()
                .statusCode(400);

        // Test with missing count parameter
        given()
                .when().get("/jokes")
                .then()
                .statusCode(400);
    }
}