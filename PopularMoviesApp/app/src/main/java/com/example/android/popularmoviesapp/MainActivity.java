package com.example.android.popularmoviesapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.popularmoviesapp.model.Movie;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements MoviesFragment.Callback {

    private static final String MOVIE_DETAIL_FRAG_TAG = "MOVIE_DETAIL_FRAG";

    //ArrayAdapter movieAdapter;
    ArrayList<Movie> movieArrayList;
    String sortBy;
    Boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sortBy = Utility.getSortOrder(this);

        setContentView(R.layout.activity_main);

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then activity is in two-pane mode.
            mTwoPane = true;
            // Show detail view by adding or replacing detail fragment
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieDetailFragment(), MOVIE_DETAIL_FRAG_TAG)
                        .commit();
            }
        }

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

    /*private void updateUI(){
        boolean fetch;

        if (sortBy != Utility.getSortOrder(this) && sortBy != "favourite") {
            updateMovieList();
            sortBy = Utility.getSortOrder(this);
        } else {
            // query local db

            updateMovieList();
        }
    }*/

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

    @Override
    public void onItemSelected(Uri contentUri, Cursor cursor, int position) {

        // make movie from cursor
        Movie movie = new Movie(cursor);

        if (mTwoPane) {
            // In two-pane mode, show detail view by adding or replacing
            // detail fragment using a fragment transaction.
            MovieDetailFragment fragment = MovieDetailFragment.newInstance(contentUri, movie);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, MOVIE_DETAIL_FRAG_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.setData(contentUri);
            intent.putExtra("selectedMovie", movie);
            startActivity(intent);
        }

    }
}
