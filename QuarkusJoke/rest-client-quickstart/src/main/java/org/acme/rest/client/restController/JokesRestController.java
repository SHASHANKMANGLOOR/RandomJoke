package org.acme.rest.client.restController;

import io.smallrye.common.constraint.NotNull;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.acme.rest.client.dto.JokeDTO;
import org.acme.rest.client.dto.JokesResponseDTO;
import org.acme.rest.client.service.RandomJokeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Path("")
public class JokesRestController {

    private static final Logger logger = LoggerFactory.getLogger(JokesRestController.class);
    @Inject
    RandomJokeService randomJokeService;

    @GET
    @Path("/jokes")
    public Response fetchjokesbyCount(@QueryParam("count")
              @Valid @Min(value = 1, message = "Minimum value of 1") @NotNull int count)
    {
        logger.info("fetchjokesbyCount method");
        List<List<JokeDTO>> jokes = randomJokeService.fetchJokes(count);
        return (jokes!= null ? Response.status(Response.Status.OK).entity(jokes).build() :
                Response.status(Response.Status.BAD_REQUEST).entity(null).build());
    }
}
