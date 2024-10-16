package org.acme.rest.client.repository;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.acme.rest.client.entity.RandomJoke;
import org.acme.rest.client.service.RandomJokeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.acme.rest.client.errorConstants.ErrorConstants.FAILED_SAVE;

@ApplicationScoped
public class JokeRepository {

    @PersistenceContext
    EntityManager em;
    private static final Logger logger = LoggerFactory.getLogger(RandomJokeService.class);

    @Transactional
    public void create(RandomJoke joke) {
        try {
            if(find(joke.getId())!=null) {
                em.persist(joke);
           }
        } catch (Exception e) {
            logger.error(FAILED_SAVE, e.getMessage());
            // Handle the exception,
        }
    }
    @Transactional
    public RandomJoke find(Long id) {
        return em.find(RandomJoke.class, id);
    }

}
