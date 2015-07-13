package com.example.android.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
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

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ArrayAdapter movieAdapter;
    ArrayList<Movie> movieArrayList;
    SharedPreferences sharedPref;
    String sortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sortBy = sharedPref.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_default));

        if(savedInstanceState == null){
            movieArrayList = new ArrayList<Movie>();
            updateMovieList();
        }else{
            movieArrayList = savedInstanceState.getParcelableArrayList("selectedMovie");
        }

        // load appropriate layout view
        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_main_landscape);
        }else{
            setContentView(R.layout.activity_main);
        }

        AdapterView movieList = (AdapterView) findViewById(android.R.id.list);
        movieAdapter = new MovieAdapter(this, movieArrayList);
        movieList.setAdapter(movieAdapter);

        movieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplication(), MovieDetailActivity.class);
                intent.putExtra("selectedMovie", (Parcelable) movieAdapter.getItem(position));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("selectedMovie", movieArrayList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        movieArrayList = savedInstanceState.getParcelableArrayList("selectedMovie");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(sortBy != sharedPref.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_default))){
            updateMovieList();
        }
    }

    private void updateMovieList() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String sortBy = sharedPref.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_default));
        new MoviesTask(new MoviesInterface() {
            @Override
            public void update(ArrayList<Movie> result) {
                //Toast.makeText(getApplicationContext(), "Loading... Please wait", Toast.LENGTH_SHORT).show();
                //movieAdapter.notifyDataSetChanged();
                if (result != null) {
                    movieAdapter.clear();
                    movieAdapter.addAll(result);
                    movieAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public Context getListenerContext() {
                return MainActivity.this;
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
