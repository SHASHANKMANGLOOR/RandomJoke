package org.acme.rest.client.restClient;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.acme.rest.client.dto.JokeDTO;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestQuery;


@RegisterRestClient(baseUri = "https://official-joke-api.appspot.com/random_joke")
public interface JokeRestClientService {

      @GET
    JokeDTO getById(@RestQuery int id);

}
