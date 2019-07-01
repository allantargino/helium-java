package com.microsoft.azure.helium.app.actor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.microsoft.azure.helium.Application;
import com.microsoft.azure.helium.utils.IntegrationTestsUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * ActorsControllerIT
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest
public class ActorsControllerIT {

    @Autowired
    private ActorsController controller;

    @Autowired
    private ActorsRepository repository;

    private List<Actor> getRandomCases(int n) {
        return Stream
                .generate(() -> ActorsUtils.createActorWithId(UUID.randomUUID().toString()))
                .limit(n)
                .collect(Collectors.toList());
    }

    @Before
    public void clearRepository(){
        repository.deleteAll();
    }

    @Test
    public void getActorsEndpointShouldReturnAllActors() {
        // Arrange
        Optional<String> query = Optional.empty();
        List<Actor> expected = getRandomCases(10);
        repository.saveAll(expected);

        // Act
        List<Actor> actual = controller.getAllActors(query).getBody();

        // Assert
        assertNotNull(actual);
        assertThat(actual, hasSize(expected.size()));
        assertThat(actual, containsInAnyOrder(expected.toArray()));
    }
    
    @Test
    public void getSingleActorEndpointShouldReturnValidActor(){
        // Arrange
        int n = 10;
        List<Actor> actors = getRandomCases(n);
        Actor expected = actors.get(IntegrationTestsUtils.getRandomBetween(0, n));
        repository.saveAll(actors);

        // Act
        Actor actual = controller.getActor(expected.getActorId()).getBody();

        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void postActorEndpointShouldCreateAndReturnValidActor(){
        // Arrange
        Actor expected = ActorsUtils.createActorWithId(UUID.randomUUID().toString());

        // Act
        controller.createActor(expected).getBody();
        Actor actual = repository.findByActorId(expected.getActorId()).get(0);

        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}