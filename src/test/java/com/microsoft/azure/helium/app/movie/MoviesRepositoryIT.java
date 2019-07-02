package com.microsoft.azure.helium.app.movie;

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
 * MoviesRepositoryIT
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest
public class MoviesRepositoryIT {

    @Autowired
    private MoviesRepository repository;

    @Before
    public void cleanRepository() {
        repository.deleteAll();
    }

    @Test
    public void findByMovieIdShouldReturnMovie() {
        // Arrange
        String movieId = UUID.randomUUID().toString();
        Movie expected = MoviesUtils.createMovieWithId(movieId);
        repository.save(expected);

        // Act
        List<Movie> movies = repository.findByMovieId(movieId);
        Movie actual = movies.get(0);

        // Assert
        assertThat(movies, hasSize(1));
        assertNotNull(actual);
        assertEquals(expected.getMovieId(), actual.getMovieId());
    }

    @Test
    public void findByTextSearchShouldQueryMoviesTextField() {
        // Arrange
        String movieId = UUID.randomUUID().toString();
        String movieName = "The Great Contoso";
        Movie expected = MoviesUtils.createMovieWithIdAndName(movieId, movieName);
        repository.save(expected);

        // Act
        List<List<Movie>> searches = Arrays.asList(
                repository.findByTextSearchContaining(movieName.toLowerCase()),                 // the great contoso
                repository.findByTextSearchContaining(movieName.split(" ")[0].toLowerCase()),   // the
                repository.findByTextSearchContaining(movieName.split(" ")[1].toLowerCase()),   // great
                repository.findByTextSearchContaining(movieName.split(" ")[2].toLowerCase()));  // contoso

        // Assert
        searches.forEach(search -> {
            assertNotNull(search);
            assertThat(search, hasSize(1));
            assertEquals(expected.getMovieId(), search.get(0).getMovieId());
        });
    }

}