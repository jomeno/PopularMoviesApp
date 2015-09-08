package com.example.android.popularmoviesapp.data.contract;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.android.popularmoviesapp.data.provider.VideosProvider;

/**
 * Created by Jomeno on 9/3/2015.
 */
public class VideosContract {

    public static final String VIDEOS_PATH = "videos";

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + VideosProvider.CONTENT_AUTHORITY + "/" + VIDEOS_PATH;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + VideosProvider.CONTENT_AUTHORITY + "/" + VIDEOS_PATH;


    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + VideosProvider.CONTENT_AUTHORITY);
    private static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(VIDEOS_PATH).build();

    public static final class Columns implements BaseColumns {
        public static final String MOVIE_ID = "movie_id";
        public static final String VIDEO_ID = "video_id";
        public static final String NAME = "name";
        public static final String SIZE = "size";
        public static final String SITE = "site";
        public static final String TYPE = "type";
    }

    public static Uri getVideosUri(Long movieId) {

        return ContentUris.appendId(BASE_CONTENT_URI.buildUpon().appendPath(MoviesContract.MOVIES_PATH), movieId)
                .appendPath(VideosContract.VIDEOS_PATH).build();
    }

    public static Uri getVideoUri(Long videoId) {
        return ContentUris.withAppendedId(CONTENT_URI, videoId);
    }


}
