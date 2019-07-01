package com.microsoft.azure.helium.app.actor;

import java.util.List;

import com.microsoft.azure.helium.app.Constants;
import com.microsoft.azure.helium.app.movie.Movie;

import com.microsoft.azure.spring.data.cosmosdb.core.mapping.Document;
import com.microsoft.azure.spring.data.cosmosdb.core.mapping.PartitionKey;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Actor
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@Document(collection = Constants.DEFAULT_ACTOR_COLLECTION_NAME)
public class Actor {
    @Id
    private String id;
    private String actorId;
    private String textSearch;
    private String name;
    private String type;
    @PartitionKey
    private String key;
    private int birthYear;
    private List<String> profession;
    private List<Movie> movies;
}