package com.example.android.popularmoviesapp.sync;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmoviesapp.R;
import com.example.android.popularmoviesapp.data.contract.VideosContract;
import com.example.android.popularmoviesapp.listener.Callbacks;
import com.example.android.popularmoviesapp.model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Jomeno on 7/11/2015.
 */
public class VideosTask extends AsyncTask<String, Void, ArrayList<Video>> {

    Context context;

    private static String TMDB_BASE_URL;
    private static String TMDB_SORT_ORDER;
    private static String TMDB_API_KEY;

    ArrayList<Video> videoArrayList;
    Callbacks.VideoCallbacks listener;


    public VideosTask(Callbacks.VideoCallbacks callback, Context context) {
        TMDB_BASE_URL = "http://api.themoviedb.org/3/movie"; //http://api.themoviedb.org/3/discover/movie
        TMDB_API_KEY = "api_key";
        TMDB_SORT_ORDER = "sort_by";
        videoArrayList = new ArrayList<>();

        this.context = context;
        this.listener = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<Video> videos) {
        listener.update(videos);
    }

    @Override
    protected ArrayList<Video> doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String videosJsonStr;
        Long movieId = Long.parseLong(params[0]);
        try {
            Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                    .appendPath(params[0]).appendPath(VideosContract.VIDEOS_PATH)
                    .appendQueryParameter(TMDB_API_KEY, context.getResources().getString(R.string.api_key_value)).build();
            URL url = new URL(builtUri.toString());
            //URL url = new URL("http://api.themoviedb.org/3/movie/31413/videos?api_key=996b2793844c7089ed0ee2354d4bba29");
            //http://api.themoviedb.org/3/movie/31413/videos?api_key=996b2793844c7089ed0ee2354d4bba29

            // Create the request to TMDB.org, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // empty stream
                return null;
            }
            videosJsonStr = buffer.toString();

            JSONObject videosJson = new JSONObject(videosJsonStr);
            JSONArray videosArray = videosJson.getJSONArray("results");
            for (int i = 0; i < videosArray.length(); i++) {
                JSONObject jsonVideo = videosArray.getJSONObject(i);
                videoArrayList.add(new Video(movieId, jsonVideo));
            }

            return videoArrayList;

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("VideosTask", "Error closing stream", e);
                }
            }
        }
        return null;
    }
}
