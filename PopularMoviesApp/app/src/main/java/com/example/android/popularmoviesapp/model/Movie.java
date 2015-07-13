package com.example.android.popularmoviesapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jomeno on 7/11/2015.
 */
public class Movie implements Parcelable {
    public String overview;
    public String title;
    public String rating;
    public String releaseDate;
    public String imageUrl;

    public Movie(JSONObject jsonMovie) {

        String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
        String IMAGE_SIZE = "w185";
        try {
            title = jsonMovie.getString("title");
            overview = jsonMovie.getString("overview");
            rating = jsonMovie.getString("vote_average");
            imageUrl = IMAGE_BASE_URL + IMAGE_SIZE + jsonMovie.getString("poster_path");
            releaseDate = jsonMovie.getString("release_date");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Movie(Parcel in){
        overview = in.readString();
        title = in.readString();
        rating = in.readString();
        releaseDate = in.readString();
        imageUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(overview);
        dest.writeString(title);
        dest.writeString(rating);
        dest.writeString(releaseDate);
        dest.writeString(imageUrl);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
