package com.example.android.popularmoviesapp.listener;

import com.example.android.popularmoviesapp.model.MovieAsset;

import java.util.ArrayList;

/**
 * Created by Jomeno on 9/7/2015.
 */
public class Callbacks {

    public interface VideoCallbacks{
        public void update(ArrayList<MovieAsset> videos);
    }
}
