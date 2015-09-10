package com.example.android.popularmoviesapp.data.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.android.popularmoviesapp.data.Database;
import com.example.android.popularmoviesapp.data.DatabaseHelper;
import com.example.android.popularmoviesapp.data.contract.MoviesContract;

/**
 * Created by Jomeno on 8/23/2015.
 */

// This class is responsible for serving requests for content from supported data source(s)

public class MoviesProvider extends ContentProvider {

    public static final String CONTENT_AUTHORITY = "com.example.android.popularmoviesapp.movies";
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    static final int MOVIE = 100;
    static final int MOVIES = 200;

    private DatabaseHelper mDatabaseHelper;


    public MoviesProvider() {

    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code above.
        matcher.addURI(authority, MoviesContract.MOVIES_PATH, MOVIES);
        matcher.addURI(authority, MoviesContract.MOVIES_PATH + "/#", MOVIE);
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
            case MOVIE:
                cursor = getMovie(uri, projection);
                break;
            case MOVIES:
                cursor = getMovies(projection, selection, selectionArgs, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return cursor;
    }

    private Cursor getMovie(Uri uri, String[] projection) {

        String selection = Database.Movies._ID + " = ?";
        String[] selectionArgs = new String[]{uri.getLastPathSegment()};
        return mDatabaseHelper.getReadableDatabase()
                .query(Database.Movies.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
    }

    private Cursor getMovies(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mDatabaseHelper.getReadableDatabase()
                .query(Database.Movies.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE:
                return MoviesContract.CONTENT_ITEM_TYPE;
            case MOVIES:
                return MoviesContract.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIE: {
                long _id = db.insert(Database.Movies.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MoviesContract.getMovieUri(_id);
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
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows, return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case MOVIES:
                rowsDeleted = db.delete(
                        Database.Movies.TABLE_NAME, selection, selectionArgs);
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

        // uneccessary (even dangerous) to accept this because it can be derived from the URI.
        selectionArgs = new String[]{uri.getLastPathSegment()};
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIE:
                selection = Database.Movies.MOVIE_ID + " = ?";
                rowsUpdated = db.update(Database.Movies.TABLE_NAME, values, selection,
                        selectionArgs);
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
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(Database.Movies.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
