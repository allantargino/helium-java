package com.microsoft.azure.helium.app.actor;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.microsoft.azure.helium.Application;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * ActorsRepositoryIT
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest
public class ActorsRepositoryIT {

    @Autowired
    private ActorsRepository repository;

    @Before
    public void cleanRepository() {
        repository.deleteAll();
    }

    @Test
    public void findByActorIdShouldReturnActor() {
        // Arrange
        String actorId = UUID.randomUUID().toString();
        Actor expected = ActorsUtils.createActorWithId(actorId);
        repository.save(expected);

        // Act
        List<Actor> actors = repository.findByActorId(actorId);
        Actor actual = actors.get(0);

        // Assert
        assertThat(actors, hasSize(1));
        assertNotNull(actual);
        assertEquals(expected.getActorId(), actual.getActorId());
    }

    @Test
    public void findByTextSearchShouldQueryActorsTextField() {
        // Arrange
        String actorId = UUID.randomUUID().toString();
        String actorName = "Joe Contoso";
        Actor expected = ActorsUtils.createActorWithIdAndName(actorId, actorName);
        repository.save(expected);

        // Act
        List<List<Actor>> searches = Arrays.asList(
                repository.findByTextSearchContaining(actorName.toLowerCase()),                 // joe contoso
                repository.findByTextSearchContaining(actorName.split(" ")[0].toLowerCase()),   // joe
                repository.findByTextSearchContaining(actorName.split(" ")[1].toLowerCase()));  // contoso

        // Assert
        searches.forEach(search -> {
            assertNotNull(search);
            assertThat(search, hasSize(1));
            assertEquals(expected.getActorId(), search.get(0).getActorId());
        });
    }

}