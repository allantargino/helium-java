package com.microsoft.azure.helium.app.actor;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
 * ActorsControllerTest
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ActorsController.class)
public class ActorsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActorsService service;

    @Test
    public void getActorsEndpointShouldReturnAllActorsFromService() throws Exception {
        // Arrange
        String expectedId = UUID.randomUUID().toString();
        List<Actor> actors = Arrays.asList(ActorsUtils.createActorWithId(expectedId));
        when(service.getAllActors(any())).thenReturn(actors);

        // Act
        ResultActions action = this.mockMvc
            .perform(get("/api/actors"))
            .andDo(print());

        // Assert
        action
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(expectedId)));
        verify(service, times(1)).getAllActors(any());
    }

    @Test
    public void getSingleActorEndpointShouldReturnValidActorFromService() throws Exception {
        // Arrange
        String id = UUID.randomUUID().toString();
        Actor expected = ActorsUtils.createActorWithId(id);
        when(service.getActor(any())).thenReturn(Optional.of(expected));

        // Act
        ResultActions result = this.mockMvc
            .perform(get("/api/actors/{actorId}", id))
            .andDo(print());

        // Assert
        result
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.id", is(expected.getId())));
        verify(service, times(1)).getActor(any());
    }

    @Test
    public void getActorsEndpointShouldReturnNotFoundActorFromService() throws Exception {
        // Arrange
        when(service.getActor(any())).thenReturn(Optional.empty());

        // Act
        ResultActions result = this.mockMvc
            .perform(get("/api/actors/{actorId}", UUID.randomUUID().toString()))
            .andDo(print());

        // Assert
        result.andExpect(status().isNotFound());
        verify(service, times(1)).getActor(any());
    }
    
    @Test
    public void postActorEndpointShouldCreateAndReturnValidActorFromService() throws Exception {
        // Arrange
        String id = UUID.randomUUID().toString();
        Actor expected = ActorsUtils.createActorWithId(id);
        when(service.createActor(any())).thenReturn(expected);

        // Act
        ResultActions action = this.mockMvc
            .perform(
                post("/api/actors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(IntegrationTestsUtils.serializeObject(expected)))
            .andDo(print());

        // Assert
        action
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id", is(expected.getId())));
        verify(service, times(1)).createActor(any());
    }

}