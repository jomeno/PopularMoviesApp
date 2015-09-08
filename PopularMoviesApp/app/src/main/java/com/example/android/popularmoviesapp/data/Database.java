package com.example.android.popularmoviesapp.data;

import android.provider.BaseColumns;

/**
 * Created by Jomeno on 8/23/2015.
 */


// This class is essentially a schema of the database
public class Database {

    public static final class Movies implements BaseColumns {

        public static final String TABLE_NAME = Database.Movies.class.getSimpleName();

        public static final String MOVIE_ID = "movie_id";
        public static final String TITLE = "title";
        public static final String OVERVIEW = "overview";
        public static final String VOTE_AVERAGE = "vote_average";
        public static final String POPULARITY = "popularity";
        public static final String RELEASE_DATE = "release_date";
        public static final String IMAGE_URL = "image_url";
        public static final String FAVOURITE = "favourite";

    }

    public static final class Videos implements BaseColumns {

        public static final String TABLE_NAME = Database.Videos.class.getSimpleName();

        public static final String VIDEO_ID = "video_id";
        public static final String MOVIE_ID = "movie_id";
        public static final String NAME = "name";
        public static final String SIZE = "size";
        public static final String SITE = "site";
        public static final String TYPE = "type";
    }
}
