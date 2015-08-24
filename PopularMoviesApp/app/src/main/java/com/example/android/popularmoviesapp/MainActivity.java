package com.example.android.popularmoviesapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.example.android.popularmoviesapp.adapter.MoviesTask;
import com.example.android.popularmoviesapp.data.contracts.MoviesContract;
import com.example.android.popularmoviesapp.listener.MoviesInterface;
import com.example.android.popularmoviesapp.model.Movie;

import java.util.ArrayList;
import java.util.Vector;


public class MainActivity extends AppCompatActivity implements MoviesFragment.Callback {

    private static final String MOVIE_DETAIL_FRAG_TAG = "MOVIE_DETAIL_FRAG";

    ArrayAdapter movieAdapter;
    ArrayList<Movie> movieArrayList;
    String sortBy;
    Boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sortBy = Utility.getSortOrder(this);

        setContentView(R.layout.activity_main);

        if(findViewById(R.id.movie_detail_container) != null){
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

        /*if(savedInstanceState == null){
            movieArrayList = new ArrayList<Movie>();
            updateMovieList();
        }else{
            movieArrayList = savedInstanceState.getParcelableArrayList("selectedMovie");
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
        });*/

        /*// load appropriate layout view
        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_main_landscape);
        }else{
            setContentView(R.layout.activity_main);
        }*/

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
        //if(sortBy != Utility.getSortOrder(this)){
            updateMovieList();
        //}
    }

    private void updateMovieList() {
        new MoviesTask(new MoviesInterface() {
            @Override
            public void update(ArrayList<Movie> movies) {

                // do bulk insert here
                Vector<ContentValues> cvVector = new Vector<>();
                for (Movie movie : movies) {
                    cvVector.add(movie.toContentValue());
                }

                ContentValues[] cvArray = new ContentValues[cvVector.size()];
                cvVector.toArray(cvArray);
                getContentResolver().bulkInsert(MoviesContract.getMoviesUri(), cvArray);

            }

            @Override
            public Context getListenerContext() {
                return MainActivity.this;
            }
        }, this).execute(sortBy);
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

    @Override
    public void onItemSelected(Uri contentUri, Cursor cursor, int position) {

        // map cursor to movie object
        Movie movie = new Movie(cursor);

        if (mTwoPane) {
            // In two-pane mode, show the detail view by adding or replacing
            // detail fragment using a fragment transaction.
            Bundle args = new Bundle();
            args.putParcelable(MovieDetailFragment.DETAIL_URI, contentUri);
            args.putParcelable("selectedMovie", (Parcelable)movie);

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, MOVIE_DETAIL_FRAG_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra("selectedMovie", (Parcelable) movie);
            //.setData(contentUri);
            startActivity(intent);
        }

    }
}
