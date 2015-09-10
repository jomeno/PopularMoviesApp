package com.example.android.popularmoviesapp.sync;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmoviesapp.R;
import com.example.android.popularmoviesapp.listener.MoviesInterface;
import com.example.android.popularmoviesapp.model.Movie;

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
public class MoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    Context context;

    private static String TMDB_BASE_URL;
    private static String TMDB_SORT_ORDER;
    private static String TMDB_API_KEY;

    ArrayList<Movie> movieArrayList;
    MoviesInterface listener;


    public MoviesTask(MoviesInterface listener, Context context) {
        TMDB_BASE_URL = "http://api.themoviedb.org/3/discover/movie";
        TMDB_API_KEY = "api_key";
        TMDB_SORT_ORDER = "sort_by";
        movieArrayList = new ArrayList<Movie>();

        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> result) {
        if(result != null) listener.update(result);
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String moviesJsonStr;
        try {

            // changes user's sort preference (if favourite) to popular
            // because sorting by 'favourite' only applies to user's local db
            //if(params[0] == Utility.favouriteSortOrder(context))
                //params[0] = Utility.popularSortOrder(context);

            Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                    .appendQueryParameter(TMDB_API_KEY, context.getResources().getString(R.string.api_key_value))
                    .appendQueryParameter(TMDB_SORT_ORDER, params[0] + ".desc").build();
            URL url = new URL(builtUri.toString());
            //"http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=996b2793844c7089ed0ee2354d4bba29";

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
            moviesJsonStr = buffer.toString();

            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray("results");
            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject jsonMovie = moviesArray.getJSONObject(i);
                movieArrayList.add(new Movie(jsonMovie));
            }

            return movieArrayList;

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
                    Log.e("MoviesTask", "Error closing stream", e);
                }
            }
        }
        return null;
    }
}
