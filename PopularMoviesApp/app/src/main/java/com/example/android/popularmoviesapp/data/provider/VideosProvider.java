package com.example.android.popularmoviesapp.data.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.example.android.popularmoviesapp.data.Database;
import com.example.android.popularmoviesapp.data.DatabaseHelper;
import com.example.android.popularmoviesapp.data.contract.MoviesContract;
import com.example.android.popularmoviesapp.data.contract.VideosContract;

/**
 * Created by Jomeno on 8/23/2015.
 */

// This class is responsible for serving requests for content from supported data source(s)

public class VideosProvider extends ContentProvider {

    public static final String CONTENT_AUTHORITY = "com.example.android.popularmoviesapp.videos";
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    static final int VIDEOS = 100;
    static final int VIDEO = 200;


    private DatabaseHelper mDatabaseHelper;


    public VideosProvider() {

    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code above.
        matcher.addURI(authority, MoviesContract.MOVIES_PATH + "/#/" +
                VideosContract.VIDEOS_PATH, VIDEOS); // must provide a movie id to get all videos
        matcher.addURI(authority, VideosContract.VIDEOS_PATH + "/#", VIDEO);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case VIDEOS:
                cursor = getVideos(uri, projection, selection, selectionArgs, sortOrder);
                break;
            case VIDEO:
                cursor = getVideo(uri, projection);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return cursor;
    }

    //build inner join query
    //videos INNER JOIN movies ON videos._ID = movies._ID
    private static final SQLiteQueryBuilder sVideoQueryBuilder;

    static {
        sVideoQueryBuilder = new SQLiteQueryBuilder();
        sVideoQueryBuilder.setTables(Database.Videos.TABLE_NAME +
                " INNER JOIN " + Database.Movies.TABLE_NAME +
                " ON " + Database.Videos.TABLE_NAME + "." + Database.Videos._ID +
                " = " + Database.Movies.TABLE_NAME + "." + Database.Movies._ID);
    }


    private Cursor getVideo(Uri uri, String[] projection) {

        final SQLiteDatabase _db = mDatabaseHelper.getReadableDatabase();
        //videos._ID = ?
        final String selection = Database.Videos.TABLE_NAME + "." + Database.Videos._ID + " = ?";
        String[] selectionArgs = new String[]{uri.getLastPathSegment()};
        return sVideoQueryBuilder.query(_db, projection, selection, selectionArgs, null, null, null);
    }

    private Cursor getVideos(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase _db = mDatabaseHelper.getReadableDatabase();
        selection = Database.Videos.MOVIE_ID + " = ?";
        String movieId = uri.getPathSegments().get(1);
        selectionArgs = new String[]{movieId};
        return _db.query(Database.Videos.TABLE_NAME,projection,selection,selectionArgs,null,null,null);
        //return sVideoQueryBuilder.query(_db, projection, selection, selectionArgs, null, null, null);
    }

    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case VIDEO:
                return MoviesContract.CONTENT_ITEM_TYPE;
            case VIDEOS:
                return MoviesContract.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase _db = mDatabaseHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case VIDEO: {
                long id = _db.insert(Database.Videos.TABLE_NAME, null, values);
                if (id > 0)
                    returnUri = VideosContract.getVideoUri(id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase _db = mDatabaseHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows, return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case VIDEOS:
                rowsDeleted = _db.delete(Database.Videos.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        selectionArgs = new String[]{uri.getLastPathSegment()};
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case VIDEO:
                selection = Database.Videos._ID + " = ?";
                rowsUpdated = db.update(Database.Videos.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase _db = mDatabaseHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case VIDEOS:
                _db.beginTransaction();
                int insertCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = _db.insert(Database.Videos.TABLE_NAME, null, value);
                        if (_id != -1) {
                            insertCount++;
                        }
                    }
                    _db.setTransactionSuccessful();
                } finally {
                    _db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return insertCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
