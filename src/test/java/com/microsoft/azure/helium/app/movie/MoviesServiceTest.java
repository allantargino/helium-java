package com.microsoft.azure.helium.app.movie;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * MoviesServiceTest
 */
@RunWith(MockitoJUnitRunner.class)
public class MoviesServiceTest {

    @Mock
    private MoviesRepository repository;

    @InjectMocks
    private MoviesService service;

    @Test
    public void shouldReturnListOfAllMovies() throws Exception {
        // Arrange
        List<Movie> expected = Arrays.asList(mock(Movie.class));
        when(repository.findAll()).thenReturn(expected);

        // Act
        List<Movie> actual = service.getAllMovies(Optional.empty());

        // Assert
        verify(repository, times(1)).findAll();
        assertNotNull(actual);
        assertThat(actual, hasSize(expected.size()));
        assertThat(actual, containsInAnyOrder(expected.toArray()));
    }

    @Test
    public void shouldReturnListofMoviesWhenQueryingValue() throws Exception {
        // Arrange
        List<Movie> expected = Arrays.asList(mock(Movie.class));
        when(repository.findByTextSearchContaining(anyString())).thenReturn(expected);

        // Act
        List<Movie> actual = service.getAllMovies(Optional.of(UUID.randomUUID().toString()));

        // Assert
        verify(repository, times(1)).findByTextSearchContaining(anyString());
        assertNotNull(actual);
        assertThat(actual, hasSize(expected.size()));
        assertThat(actual, containsInAnyOrder(expected.toArray()));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenGettingMovieWithNullMovieId() {
        String movieId = null;

        service.getMovie(movieId);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenGettingMovieWithEmptyMovieId() {
        String movieId = "";

        service.getMovie(movieId);
    }

    @Test
    public void shouldReturnEmptyOptionalWhenNotFindingMovie() throws Exception {
        // Arrange
        List<Movie> expected = new ArrayList<>();
        when(repository.findByMovieId(anyString())).thenReturn(expected);

        // Act
        Optional<Movie> actual = service.getMovie(UUID.randomUUID().toString());

        // Assert
        verify(repository, times(1)).findByMovieId(anyString());
        assertNotNull(actual);
        assertFalse(actual.isPresent());
    }

    @Test
    public void shouldReturnMovieInOptionalWhenFindingMovie() throws Exception {
        // Arrange
        Movie expected = mock(Movie.class);
        List<Movie> list = Arrays.asList(expected);
        when(repository.findByMovieId(anyString())).thenReturn(list);

        // Act
        Optional<Movie> actual = service.getMovie(UUID.randomUUID().toString());

        // Assert
        verify(repository, times(1)).findByMovieId(anyString());
        assertNotNull(actual);
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenCreatingMovieWithNull() {
        Movie movie = null;

        service.createMovie(movie);
    }

    @Test
    public void shouldReturnMovieWhenCreatingMovie() throws Exception {
        // Arrange
        Movie expected = mock(Movie.class);
        when(repository.save(expected)).thenReturn(expected);

        // Act
        Movie actual = service.createMovie(expected);

        // Assert
        verify(repository, times(1)).save(expected);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenUpdatingMovieWithEmptyMovieId() {
        String movieId = "";
        Movie movie = mock(Movie.class);

        service.updateMovie(movieId, movie);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenUpdatingMovieWithNull() {
        String movieId = UUID.randomUUID().toString();
        Movie movie = null;

        service.updateMovie(movieId, movie);
    }

    @Test
    public void shouldReturnMovieWhenUpdatingMovie() {
        // Arrange
        String movieId = UUID.randomUUID().toString();
        Movie expected = MoviesUtils.createMovieWithId(movieId);
        when(repository.existsById(movieId)).thenReturn(true);
        when(repository.save(expected)).thenReturn(expected);

        // Act
        Movie actual = service.updateMovie(movieId, expected);

        // Assert
        verify(repository, times(1)).existsById(movieId);
        verify(repository, times(1)).save(any());
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowWhenUpdatingMovieWithNonExistMovieId() {
        // Arrange
        String movieId = UUID.randomUUID().toString();
        Movie expected = MoviesUtils.createMovieWithId(movieId);
        when(repository.existsById(movieId)).thenReturn(false);

        // Act
        service.updateMovie(movieId, expected);

        // Assert

    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenDeletingMovieWithEmptyMovieId() {
        String movieId = "";

        service.deleteMovie(movieId);
    }

    @Test
    public void shouldDeleteMovie() {
        // Arrange
        String movieId = UUID.randomUUID().toString();

        // Act
        service.deleteMovie(movieId);

        // Assert
        verify(repository, times(1)).deleteById(movieId);
    }
}