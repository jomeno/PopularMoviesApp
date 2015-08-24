package com.example.android.popularmoviesapp.data.contracts;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.android.popularmoviesapp.data.MoviesProvider;

/**
 * Created by Jomeno on 8/23/2015.
 */

// This class represents what to expect by signing this contract

public class MoviesContract {

    public static final String MOVIES_PATH = "movies";

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + MoviesProvider.CONTENT_AUTHORITY + "/" + MOVIES_PATH;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + MoviesProvider.CONTENT_AUTHORITY + "/" + MOVIES_PATH;


    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + MoviesProvider.CONTENT_AUTHORITY);
    private static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(MOVIES_PATH).build();

    public static final class Columns implements BaseColumns {
        public static final String MOVIE_ID = "movie_id";
        public static final String TITLE = "title";
        public static final String OVERVIEW = "overview";
        public static final String VOTE_AVERAGE = "vote_average";
        public static final String RELEASE_DATE = "release_date";
        public static final String IMAGE_URL = "image_url";
        public static final String FAVOURITE = "favourite";
    }

    public static Uri getMoviesUri() {
        return CONTENT_URI;
    }

    public static Uri getMovieUri(Long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
    public static Uri getMovieUri(String sort_by) {
        return CONTENT_URI.buildUpon().appendQueryParameter("sort_by", sort_by).build();
    }


}
