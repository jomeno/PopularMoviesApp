package com.example.android.popularmoviesapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.popularmoviesapp.data.contract.VideosContract;
import com.example.android.popularmoviesapp.model.Movie;


public class MovieDetailActivity extends AppCompatActivity {

    private static final String MOVIE_DETAIL_FRAG_TAG = "MOVIE_DETAIL_FRAG";
    private static final int VIDEO_LOADER = 0;
    private static final String[] VIDEO_COLUMNS = {
            VideosContract.Columns._ID,
            VideosContract.Columns.VIDEO_ID,
            VideosContract.Columns.MOVIE_ID,
            VideosContract.Columns.NAME,
            VideosContract.Columns.SIZE,
            VideosContract.Columns.SITE,
            VideosContract.Columns.TYPE
    };

    //private VideoAdapter mVideoAdapter;
    //private ListView mListView;
    //private int mPosition = ListView.INVALID_POSITION;

    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movie = getIntent().getParcelableExtra("selectedMovie");
        Uri contentUri = getIntent().getData();
        // restore any saved instance states
        if (savedInstanceState != null && savedInstanceState.containsKey("selectedMovie")) {
            movie = savedInstanceState.getParcelable("selectedMovie");
        }

        // Show detail view by adding or replacing detail fragment if movie object is available
        if (movie != null) {
            MovieDetailFragment fragment = MovieDetailFragment.newInstance(contentUri, movie);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, MOVIE_DETAIL_FRAG_TAG)
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
