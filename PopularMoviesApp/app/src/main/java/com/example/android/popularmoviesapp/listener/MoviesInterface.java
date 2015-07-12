package com.example.android.popularmoviesapp.listener;

import android.content.Context;

import com.example.android.popularmoviesapp.model.Movie;

import java.util.ArrayList;

/**
 * Created by Jomeno on 7/11/2015.
 */
public interface MoviesInterface {
    void update(ArrayList<Movie> result);
    Context getListenerContext();
}
