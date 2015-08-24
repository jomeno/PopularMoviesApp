package com.example.android.popularmoviesapp.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.popularmoviesapp.data.contracts.MoviesContract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jomeno on 7/11/2015.
 */
public class Movie implements Parcelable {
    public int id;
    public int movie_id;
    public String overview;
    public String title;
    public long vote_average;
    public String releaseDate;
    public String imageUrl;
    public int favourite;

    private String IMAGE_BASE_URL;
    private String[] imageSizes;
    private String IMAGE_SIZE;

    public Movie(){
        IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
        imageSizes = new String[] {"w185", "w342"};
        IMAGE_SIZE = imageSizes[1];
    }

    public Movie(Cursor c){
        this();
        movie_id = c.getInt(c.getColumnIndex(MoviesContract.Columns.MOVIE_ID));
        title = c.getString(c.getColumnIndex(MoviesContract.Columns.TITLE));
        overview = c.getString(c.getColumnIndex(MoviesContract.Columns.OVERVIEW));
        vote_average = c.getLong(c.getColumnIndex(MoviesContract.Columns.VOTE_AVERAGE));
        imageUrl = IMAGE_BASE_URL + IMAGE_SIZE + c.getString(c.getColumnIndex(MoviesContract.Columns.IMAGE_URL));
        releaseDate = c.getString(c.getColumnIndex(MoviesContract.Columns.RELEASE_DATE));
    }

    public Movie(JSONObject jsonMovie) {
        this();

        try {
            movie_id = jsonMovie.getInt("id");
            title = jsonMovie.getString("title");
            overview = jsonMovie.getString("overview");
            vote_average = jsonMovie.getLong("vote_average");
            imageUrl = IMAGE_BASE_URL + IMAGE_SIZE + jsonMovie.getString("poster_path");
            releaseDate = jsonMovie.getString("release_date");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Movie(Parcel in) {
        id = in.readInt();
        movie_id = in.readInt();
        overview = in.readString();
        title = in.readString();
        vote_average = in.readLong();
        releaseDate = in.readString();
        imageUrl = in.readString();
        favourite = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(movie_id);
        dest.writeString(overview);
        dest.writeString(title);
        dest.writeLong(vote_average);
        dest.writeString(releaseDate);
        dest.writeString(imageUrl);
        dest.writeInt(favourite);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


    public ContentValues toContentValue() {
        ContentValues cv = new ContentValues();
        cv.put(MoviesContract.Columns.MOVIE_ID, movie_id);
        cv.put(MoviesContract.Columns.TITLE, title);
        cv.put(MoviesContract.Columns.OVERVIEW, overview);
        cv.put(MoviesContract.Columns.IMAGE_URL, imageUrl);
        cv.put(MoviesContract.Columns.RELEASE_DATE, releaseDate);
        cv.put(MoviesContract.Columns.VOTE_AVERAGE, vote_average);
        cv.put(MoviesContract.Columns.FAVOURITE, favourite);
        return cv;
    }
}
