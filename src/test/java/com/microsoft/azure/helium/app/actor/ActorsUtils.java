package com.microsoft.azure.helium.app.actor;

import java.util.ArrayList;

import com.microsoft.azure.helium.app.movie.Movie;

/**
 * ActorsUtils
 */
public class ActorsUtils {

    public static Actor createActorWithId(String id){
        return new Actor(id, id, "", "", "actor", "0", 0, new ArrayList<String>(), new ArrayList<Movie>());
    }

    public static Actor createActorWithIdAndName(String id, String name){
        return new Actor(id, id, name.toLowerCase(), name, "actor", "0", 0, new ArrayList<String>(), new ArrayList<Movie>());
    }
    
}