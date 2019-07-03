package com.microsoft.azure.helium.app.movie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
 * MoviesControllerIT
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest
public class MoviesControllerIT {

    @Autowired
    private MoviesController controller;

    @Autowired
    private MoviesRepository repository;

    private List<Movie> getRandomCases(int n) {
        return Stream
                .generate(() -> MoviesUtils.createMovieWithId(UUID.randomUUID().toString()))
                .limit(n)
                .collect(Collectors.toList());
    }

    @Before
    public void clearRepository(){
        repository.deleteAll();
    }

    @Test
    public void getMoviesEndpointShouldReturnAllMovies() {
        // Arrange
        Optional<String> query = Optional.empty();
        List<Movie> expected = getRandomCases(10);
        repository.saveAll(expected);

        // Act
        List<Movie> actual = controller.getAllMovies(query).getBody();

        // Assert
        assertNotNull(actual);
        assertThat(actual, hasSize(expected.size()));
        assertThat(actual, containsInAnyOrder(expected.toArray()));
    }
    
    @Test
    public void getSingleMovieEndpointShouldReturnValidMovie(){
        // Arrange
        int n = 10;
        List<Movie> movies = getRandomCases(n);
        Movie expected = movies.get(IntegrationTestsUtils.getRandomBetween(0, n));
        repository.saveAll(movies);

        // Act
        Movie actual = controller.getMovie(expected.getMovieId()).getBody();

        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void postMovieEndpointShouldCreateAndReturnValidMovie(){
        // Arrange
        Movie expected = MoviesUtils.createMovieWithId(UUID.randomUUID().toString());

        // Act
        controller.createMovie(expected);
        Movie actual = repository.findByMovieId(expected.getMovieId()).get(0);

        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void putMovieEndpointShouldUpdateAndReturnValidMovie(){
        // Arrange
        String movieId = UUID.randomUUID().toString();
        String movieName = UUID.randomUUID().toString();
        Movie movie = MoviesUtils.createMovieWithId(movieId);
        repository.save(movie);
        Movie expected = MoviesUtils.createMovieWithIdAndName(movieId, movieName);
        
        // Act
        controller.updateMovie(movie.getId(), expected);
        Movie actual = repository.findByMovieId(expected.getMovieId()).get(0);

        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
    }
    
    @Test
    public void deleteMovieEndpointShouldDeleteMovie(){
        // Arrange
        Movie movie = MoviesUtils.createMovieWithId(UUID.randomUUID().toString());
        repository.save(movie);
        
        // Act
        List<Movie> beforeList = repository.findByMovieId(movie.getMovieId());
        controller.deleteMovie(movie.getMovieId());
        List<Movie> afterList = repository.findByMovieId(movie.getMovieId());

        // Assert
        assertFalse(beforeList.isEmpty());
        assertTrue(afterList.isEmpty());
    }
}