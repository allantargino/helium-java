package com.microsoft.azure.helium.app.movie;

import java.util.List;

import com.microsoft.azure.helium.app.Constants;
import com.microsoft.azure.spring.data.cosmosdb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.microsoft.azure.helium.app.actor.Actor;

/**
 * Movie
 */
@Getter
@AllArgsConstructor
@Document(collection = Constants.DEFAULT_MOVIE_COLLECTION_NAME)
public class Movie {
  private String id;
  private String movieId;
  private String textSearch;
  private String title;
  private String type;
  private String key;
  private float year;
  private float rating;
  private float votes;
  private List<String> genres;
  private List<Actor> roles;
}