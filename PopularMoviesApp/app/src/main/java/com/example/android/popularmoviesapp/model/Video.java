package com.example.android.popularmoviesapp.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.popularmoviesapp.data.contract.VideosContract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jomeno on 7/11/2015.
 */
public class Video implements Parcelable {
    public int id;
    public String video_id;
    public Long movie_id;
    public String name;
    public int size;
    public String site;
    public String type;

    public Video(){
    }

    public Video(Cursor c){
        video_id = c.getString(c.getColumnIndex(VideosContract.Columns.VIDEO_ID));
        movie_id = c.getLong(c.getColumnIndex(VideosContract.Columns.MOVIE_ID));
        name = c.getString(c.getColumnIndex(VideosContract.Columns.NAME));
        size = c.getInt(c.getColumnIndex(VideosContract.Columns.SIZE));
        site = c.getString(c.getColumnIndex(VideosContract.Columns.SITE));
        type = c.getString(c.getColumnIndex(VideosContract.Columns.TYPE));
    }

    public Video(Long movieId, JSONObject jsonVideo) {
        try {
            video_id = jsonVideo.getString("id");
            movie_id = movieId;
            name = jsonVideo.getString("name");
            size = jsonVideo.getInt("size");
            site = jsonVideo.getString("site");
            type = jsonVideo.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Video(Parcel in) {
        id = in.readInt();
        video_id = in.readString();
        movie_id = in.readLong();
        name = in.readString();
        size = in.readInt();
        site = in.readString();
        type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(video_id);
        dest.writeLong(movie_id);
        dest.writeString(name);
        dest.writeInt(size);
        dest.writeString(site);
        dest.writeString(type);
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        public Video[] newArray(int size) {
            return new Video[size];
        }
    };


    public ContentValues toContentValue() {
        ContentValues cv = new ContentValues();
        cv.put(VideosContract.Columns.VIDEO_ID, video_id);
        cv.put(VideosContract.Columns.MOVIE_ID, movie_id);
        cv.put(VideosContract.Columns.NAME, name);
        cv.put(VideosContract.Columns.SIZE, size);
        cv.put(VideosContract.Columns.SITE, site);
        cv.put(VideosContract.Columns.TYPE, type);
        return cv;
    }
}
