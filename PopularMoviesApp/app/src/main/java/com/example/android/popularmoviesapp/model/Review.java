package com.example.android.popularmoviesapp.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jomeno on 7/11/2015.
 */
public class Review extends MovieAsset implements Parcelable {

    public String author;
    public Long movie_id;
    public String content;
    public String type;

    public Review(){
    }

    public Review(Long movieId, JSONObject jsonReview) {
        try {
            author = jsonReview.getString("author");
            movie_id = movieId;
            content = jsonReview.getString("content");
            type = jsonReview.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Review(Parcel in) {
        author = in.readString();
        movie_id = in.readLong();
        content = in.readString();
        type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeLong(movie_id);
        dest.writeString(content);
        dest.writeString(type);
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        public Review[] newArray(int size) {
            return new Review[size];
        }
    };


    public ContentValues toContentValue() {
        ContentValues cv = new ContentValues();
        cv.put("AUTHOR", author);
        cv.put("MOVIE_ID", movie_id);
        cv.put("CONTENT", content);
        cv.put("TYPE", type);
        return cv;
    }
}
