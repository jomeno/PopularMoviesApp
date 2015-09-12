package com.example.android.popularmoviesapp;


import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.android.popularmoviesapp.data.contract.MoviesContract;
import com.example.android.popularmoviesapp.listener.MoviesInterface;
import com.example.android.popularmoviesapp.model.Movie;
import com.example.android.popularmoviesapp.sync.MovieAdapter;
import com.example.android.popularmoviesapp.sync.MoviesTask;

import java.util.ArrayList;
import java.util.Vector;


public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = MoviesFragment.class.getSimpleName();
    private MovieAdapter mMovieAdapter;
    private GridView mListView;
    private int mPosition = ListView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";

    private static final int MOVIE_LOADER = 0;

    private ArrayList<Movie> movieArrayList;

    private static final String[] MOVIE_COLUMNS = {
            MoviesContract.Columns._ID,
            MoviesContract.Columns.MOVIE_ID,
            MoviesContract.Columns.TITLE,
            MoviesContract.Columns.FAVOURITE,
            MoviesContract.Columns.IMAGE_URL,
            MoviesContract.Columns.OVERVIEW,
            MoviesContract.Columns.VOTE_AVERAGE,
            MoviesContract.Columns.POPULARITY,
            MoviesContract.Columns.RELEASE_DATE
    };

    public static MoviesFragment newInstance(String param1, String param2) {
        MoviesFragment fragment = new MoviesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshUI();
    }

    private void refreshUI() {
        String sortOrder = Utility.getSortOrder(getActivity()) + " DESC LIMIT 20";

        Uri moviesUri = MoviesContract.getMoviesUri();
        Cursor cursor = getActivity().getContentResolver().query(moviesUri, null, null, null, sortOrder);
        mMovieAdapter.swapCursor(cursor);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String sortBy = Utility.getSortOrder(getActivity());
        if (savedInstanceState == null) {
            new MoviesTask(new MoviesInterface() {
                @Override
                public void update(ArrayList<Movie> movies) {
                    if (movies == null) return;
                    // do bulk insert here
                    Vector<ContentValues> cvVector = new Vector<>();
                    for (Movie movie : movies) {
                        cvVector.add(movie.toContentValue());
                    }

                    ContentValues[] cvArray = new ContentValues[cvVector.size()];
                    cvVector.toArray(cvArray);
                    getActivity().getContentResolver().bulkInsert(MoviesContract.getMoviesUri(), cvArray);
                    refreshUI();
                }
            }, getActivity()).execute(sortBy);
        }

        // The MovieAdapter will take data from a source
        // to populate the ListView to which it is attached.
        mMovieAdapter = new MovieAdapter(getActivity(), null, 0);
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        // Get a reference to the ListView, and attach adapter to it.
        mListView = (GridView) rootView.findViewById(android.R.id.list);
        mListView.setAdapter(mMovieAdapter);
        // Call MainActivity for onItemSelected Implementation
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                String columnName = MoviesContract.Columns._ID;
                int colIndex = cursor.getColumnIndex(columnName);
                Uri moviesUri = MoviesContract.getMovieUri(cursor.getLong(colIndex));

                if (cursor != null) {
                    ((Callback) getActivity()).onItemSelected(moviesUri, cursor, position);
                }
                mPosition = position;
            }
        });

        // If there's instance state, mine it for useful information.
        // The end-goal here is that the user never knows that turning their device sideways
        // does crazy lifecycle related things.  It should feel like some stuff stretched out,
        // or magically appeared to take advantage of room, but data or place in the app was never
        // actually *lost*.
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.
            // Actually perform the swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.

        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortOrder = Utility.getSortOrder(getActivity());
        Uri moviesUri = MoviesContract.getMoviesUri();
        return new CursorLoader(getActivity(),
                moviesUri,
                MOVIE_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMovieAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.swapCursor(null);
    }

    public interface Callback {
        /**
         * MovieDetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri contentUri, Cursor cursor, int position);
    }
}
