package com.microsoft.azure.helium.app.movie;

import java.util.ArrayList;

import com.microsoft.azure.helium.app.actor.Actor;

/**
 * MoviesUtils
 */
public class MoviesUtils {

    public static Movie createMovieWithId(String id) {
        return new Movie(id, id, "", "", "movie", "0", 0, 0, 0, new ArrayList<String>(), new ArrayList<Actor>());
    }

    public static Movie createMovieWithIdAndName(String id, String name) {
        return new Movie(id, id, name.toLowerCase(), name, "movie", "0", 0, 0, 0, new ArrayList<String>(), new ArrayList<Actor>());
    }
}