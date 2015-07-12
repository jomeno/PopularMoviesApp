package com.example.android.popularmoviesapp.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Jomeno on 7/11/2015.
 */
public class Movie implements Serializable {
    public String overview;
    public String title;
    public String rating;
    public String releaseDate;
    public String imageUrl;

    public Movie(JSONObject jsonMovie) throws JSONException {

        String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
        String IMAGE_SIZE = "w185";

        title = jsonMovie.getString("title");
        overview = jsonMovie.getString("overview");
        rating = jsonMovie.getString("vote_average");
        imageUrl = IMAGE_BASE_URL + IMAGE_SIZE + jsonMovie.getString("poster_path");
        releaseDate = jsonMovie.getString("release_date");

    }
}
