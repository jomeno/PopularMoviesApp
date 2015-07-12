package com.example.android.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.android.popularmoviesapp.adapter.MovieAdapter;
import com.example.android.popularmoviesapp.adapter.MoviesTask;
import com.example.android.popularmoviesapp.listener.MoviesInterface;
import com.example.android.popularmoviesapp.model.Movie;

import java.io.Serializable;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ArrayAdapter movieAdapter;
    String sortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdapterView movieList = (AdapterView) findViewById(android.R.id.list);
        final ArrayList<Movie> movieArrayList = new ArrayList<Movie>();
        movieAdapter = new MovieAdapter(this, movieArrayList);
        movieList.setAdapter(movieAdapter);

        movieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplication(), MovieDetailActivity.class);
                intent.putExtra("selectedMovie", (Serializable) movieAdapter.getItem(position));
                startActivity(intent);
            }
        });

        // set sorting order from preference
        sortBy = "popularity.desc";

        updateMovieList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateMovieList();
    }

    private void updateMovieList() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String sortBy = sharedPref.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_default));
        new MoviesTask(new MoviesInterface() {
            @Override
            public void update(ArrayList<Movie> result) {
                //Toast.makeText(getApplicationContext(), "Loading... Please wait", Toast.LENGTH_SHORT).show();

                movieAdapter.notifyDataSetChanged();
                if (result != null) {
                    movieAdapter.clear();
                    movieAdapter.addAll(result);
                    movieAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public Context getContext() {
                return getContext();
            }
        }).execute(sortBy);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
