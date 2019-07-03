package com.microsoft.azure.helium.app.movie;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.microsoft.azure.helium.utils.IntegrationTestsUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * MoviesControllerTest
 */
@RunWith(SpringRunner.class)
@WebMvcTest(MoviesController.class)
public class MoviesControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MoviesService service;

    @Test
    public void getMoviesEndpointShouldReturnAllMoviesFromService() throws Exception {
        // Arrange
        String expectedId = UUID.randomUUID().toString();
        List<Movie> movies = Arrays.asList(MoviesUtils.createMovieWithId(expectedId));
        when(service.getAllMovies(any())).thenReturn(movies);

        // Act
        ResultActions action = this.mockMvc
            .perform(get("/api/movies"))
            .andDo(print());

        // Assert
        action
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(expectedId)));
        verify(service, times(1)).getAllMovies(any());
    }

    @Test
    public void getSingleMovieEndpointShouldReturnValidMovieFromService() throws Exception {
        // Arrange
        String id = UUID.randomUUID().toString();
        Movie expected = MoviesUtils.createMovieWithId(id);
        when(service.getMovie(any())).thenReturn(Optional.of(expected));

        // Act
        ResultActions result = this.mockMvc
            .perform(get("/api/movies/{movieId}", id))
            .andDo(print());

        // Assert
        result
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.id", is(expected.getId())));
        verify(service, times(1)).getMovie(any());
    }

    @Test
    public void getMoviesEndpointShouldReturnNotFoundMovieFromService() throws Exception {
        // Arrange
        when(service.getMovie(any())).thenReturn(Optional.empty());

        // Act
        ResultActions result = this.mockMvc
            .perform(get("/api/movies/{movieId}", UUID.randomUUID().toString()))
            .andDo(print());

        // Assert
        result.andExpect(status().isNotFound());
        verify(service, times(1)).getMovie(any());
    }
    
    @Test
    public void postMovieEndpointShouldCreateAndReturnValidMovieFromService() throws Exception {
        // Arrange
        String id = UUID.randomUUID().toString();
        Movie expected = MoviesUtils.createMovieWithId(id);
        when(service.createMovie(any())).thenReturn(expected);

        // Act
        ResultActions action = this.mockMvc
            .perform(
                post("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(IntegrationTestsUtils.serializeObject(expected)))
            .andDo(print());

        // Assert
        action
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id", is(expected.getId())));
        verify(service, times(1)).createMovie(any());
    }

    @Test
    public void putMovieEndpointShouldUpdateAndReturnValidMovieFromService() throws Exception {
        // Arrange
        String id = UUID.randomUUID().toString();
        Movie expected = MoviesUtils.createMovieWithId(id);
        when(service.updateMovie(anyString(), any())).thenReturn(expected);

        // Act
        ResultActions action = this.mockMvc
            .perform(
                put("/api/movies/{movieId}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(IntegrationTestsUtils.serializeObject(expected)))
            .andDo(print());

        // Assert
        action
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id", is(expected.getId())));
        verify(service, times(1)).updateMovie(anyString(), any());
    }

    @Test
    public void deleteMoviesEndpointShouldReturnNoContent() throws Exception {
        // Arrange
        String id = UUID.randomUUID().toString();
        Movie movie = MoviesUtils.createMovieWithId(id);
        when(service.getMovie(id)).thenReturn(Optional.of(movie));

        // Act
        ResultActions result = this.mockMvc
            .perform(delete("/api/movies/{movieId}", id))
            .andDo(print());

        // Assert
        result.andExpect(status().isNoContent());
        verify(service, times(1)).getMovie(id);
        verify(service, times(1)).deleteMovie(id);
    }

    @Test
    public void deleteMoviesEndpointShouldReturnNotFoundFromService() throws Exception {
        // Arrange
        when(service.getMovie(anyString())).thenReturn(Optional.empty());

        // Act
        ResultActions result = this.mockMvc
            .perform(delete("/api/movies/{movieId}", UUID.randomUUID().toString()))
            .andDo(print());

        // Assert
        result.andExpect(status().isNotFound());
        verify(service, times(1)).getMovie(anyString());
        verify(service, times(0)).deleteMovie(anyString());
    }
}