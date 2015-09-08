package com.example.android.popularmoviesapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.popularmoviesapp.data.contract.VideosContract;
import com.example.android.popularmoviesapp.listener.Callbacks;
import com.example.android.popularmoviesapp.model.Movie;
import com.example.android.popularmoviesapp.model.Video;
import com.example.android.popularmoviesapp.sync.VideoAdapter;
import com.example.android.popularmoviesapp.sync.VideosTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Vector;


public class MovieDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

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

    private VideoAdapter mVideoAdapter;
    private ListView mListView;

    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movie = getIntent().getParcelableExtra("selectedMovie");
        TextView titleView = (TextView) findViewById(R.id.movie_title);
        TextView overviewView = (TextView) findViewById(R.id.overview);
        TextView ratingView = (TextView) findViewById(R.id.rating);
        TextView releaseDateView = (TextView) findViewById(R.id.release_date);
        TextView releaseYearView = (TextView) findViewById(R.id.release_year);
        ImageView thumbnailView = (ImageView) findViewById(R.id.thumbnail);
        String releaseYear = movie.releaseDate.substring(0, 4);

        Picasso.with(this)
                .load(movie.imageUrl)
                .fit()
                .centerCrop()
                .into(thumbnailView);

        getLoaderManager().initLoader(VIDEO_LOADER, null, this);

        // set view content
        if (movie != null) {
            titleView.setText(movie.title);
            overviewView.setText(movie.overview);
            releaseDateView.setText(movie.releaseDate);
            releaseYearView.setText(releaseYear);
            ratingView.setText("Rating - " + movie.vote_average);

            // movie trailers

            loadMovieTrailers(movie.movie_id);
        }


        // create adapter and attach to list view
        mVideoAdapter = new VideoAdapter(this, null, 0);
        mListView = (ListView)findViewById(R.id.trailer_list);
        mListView.setAdapter(mVideoAdapter);


    }

    private void loadMovieTrailers(final Long movieId) {

        new VideosTask(new Callbacks.VideoCallbacks() {
            @Override
            public void update(ArrayList<Video> videos) {
                // do bulk insert here
                Vector<ContentValues> cvVector = new Vector<>();
                for (Video video : videos) {
                    cvVector.add(video.toContentValue());
                }

                ContentValues[] cvArray = new ContentValues[cvVector.size()];
                cvVector.toArray(cvArray);
                getContentResolver().bulkInsert(VideosContract.getVideosUri(movieId), cvArray);

            }
        }, this).execute(String.valueOf(movieId));
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = Utility.getSortOrder(this);
        Uri videoUri = VideosContract.getVideosUri(movie.movie_id);
        return new CursorLoader(this,
                videoUri,
                VIDEO_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mVideoAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mVideoAdapter.swapCursor(null);
    }


}
