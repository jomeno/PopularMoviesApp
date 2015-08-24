package com.example.android.popularmoviesapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jomeno on 8/23/2015.
 */

// This class handles SQL query execution

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "PopMovies.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + Database.Movies.TABLE_NAME + " (" +
                Database.Movies._ID + " INTEGER PRIMARY KEY," +
                Database.Movies.MOVIE_ID + " INTEGER NOT NULL," +
                Database.Movies.TITLE + " TEXT NOT NULL, " +
                Database.Movies.OVERVIEW + " TEXT NOT NULL, " +
                Database.Movies.RELEASE_DATE + " TEXT NOT NULL, " +
                Database.Movies.IMAGE_URL + " REAL NOT NULL, " +
                Database.Movies.VOTE_AVERAGE + " REAL NOT NULL, " +
                Database.Movies.FAVOURITE + " REAL NOT NULL, " +

                // Ensures the application has just one movie entry per id per title
                // create a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + Database.Movies.MOVIE_ID + ", " +
                Database.Movies.TITLE + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_MOVIES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Database.Movies.TABLE_NAME);
        onCreate(db);
    }
}
