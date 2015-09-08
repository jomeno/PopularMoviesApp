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

    private static final String DATABASE_NAME = "popmovies.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase _db) {

        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + Database.Movies.TABLE_NAME + " (" +
                Database.Movies._ID + " INTEGER PRIMARY KEY," +
                Database.Movies.MOVIE_ID + " INTEGER NOT NULL," +
                Database.Movies.TITLE + " TEXT NOT NULL, " +
                Database.Movies.OVERVIEW + " TEXT NOT NULL, " +
                Database.Movies.RELEASE_DATE + " TEXT NOT NULL, " +
                Database.Movies.IMAGE_URL + " REAL NOT NULL, " +
                Database.Movies.VOTE_AVERAGE + " REAL NOT NULL, " +
                Database.Movies.POPULARITY + " REAL NOT NULL, " +
                Database.Movies.FAVOURITE + " REAL NOT NULL, " +

                // Ensure application has only one movie entry per id per title
                // create a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + Database.Movies.MOVIE_ID + ", " +
                Database.Movies.TITLE + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_VIDEOS_TABLE = "CREATE TABLE " + Database.Videos.TABLE_NAME + " (" +
                Database.Videos._ID + " INTEGER PRIMARY KEY," +
                Database.Videos.VIDEO_ID + " TEXT NOT NULL, " +
                Database.Videos.NAME + " TEXT NOT NULL, " +
                Database.Videos.MOVIE_ID + " INTEGER NOT NULL," +
                Database.Videos.SIZE + " INTEGER NOT NULL, " +
                Database.Videos.SITE + " TEXT NOT NULL, " +
                Database.Videos.TYPE + " TEXT NOT NULL, " +

                // Setup movie_id column as foreign key to movies table
                " FOREIGN KEY(" + Database.Videos._ID + ")" +
                " REFERENCES " + Database.Movies.TABLE_NAME + " (" + Database.Movies._ID + ") " +
                " ON UPDATE CASCADE ON DELETE CASCADE, " +

                // Ensure application has only one video entry per movie_id per video name
                // create a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + Database.Videos.MOVIE_ID + ", " +
                Database.Videos.NAME + ") ON CONFLICT REPLACE);";


        _db.execSQL(SQL_CREATE_MOVIES_TABLE);
        _db.execSQL(SQL_CREATE_VIDEOS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Database.Movies.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Database.Videos.TABLE_NAME);
        onCreate(db);
    }
}
