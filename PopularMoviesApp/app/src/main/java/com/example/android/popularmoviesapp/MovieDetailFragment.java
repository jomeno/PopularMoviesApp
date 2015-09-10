package com.example.android.popularmoviesapp;


import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.popularmoviesapp.data.Database;
import com.example.android.popularmoviesapp.data.contract.MoviesContract;
import com.example.android.popularmoviesapp.data.contract.VideosContract;
import com.example.android.popularmoviesapp.listener.Callbacks;
import com.example.android.popularmoviesapp.model.Movie;
import com.example.android.popularmoviesapp.model.Video;
import com.example.android.popularmoviesapp.sync.VideoAdapter;
import com.example.android.popularmoviesapp.sync.VideosTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Vector;

public class MovieDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    static final String DETAIL_URI = "URI";
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
    private int mPosition = ListView.INVALID_POSITION;
    private VideoAdapter mVideoAdapter;
    private ListView mListView;
    Movie movie;

    public static MovieDetailFragment newInstance(Uri contentUri, Movie movie) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(MovieDetailFragment.DETAIL_URI, contentUri);
        args.putParcelable("selectedMovie", movie);
        fragment.setArguments(args);
        return fragment;
    }

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(VIDEO_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("selectedMovie")) {
            movie = savedInstanceState.getParcelable("selectedMovie");
        }

        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        //rootView.setVisibility(View.GONE); // for now remain blank until a movie is selected

        // create adapter and attach to list view
        mVideoAdapter = new VideoAdapter(getActivity(), null, 0);
        mListView = (ListView) rootView.findViewById(R.id.trailer_list);

        if (getArguments() != null) {
            movie = getArguments().getParcelable("selectedMovie");
            //rootView.setVisibility(View.VISIBLE);

            /*// create adapter and attach to list view
            mVideoAdapter = new VideoAdapter(getActivity(), null, 0);
            mListView = (ListView) rootView.findViewById(R.id.trailer_list);*/

            // set list view header
            View header = inflater.inflate(R.layout.item_movie_header, mListView, false);
            setHeaderContent(header, movie);
            mListView.addHeaderView(header, null, false);

            mListView.setAdapter(mVideoAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    // CursorAdapter returns a cursor at the correct position for getItem(), or null
                    // if it cannot seek to that position.
                    Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                    if (cursor != null) {
                        // a working sample video id dXTBbM21plg
                        String videoId = cursor.getString(cursor.getColumnIndex(Database.Videos.VIDEO_ID));
                        Intent intent;

                        try {
                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
                            intent.putExtra("VIDEO_ID", videoId);
                        } catch (ActivityNotFoundException ex) {
                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoId));
                        }
                        startActivity(intent);
                    }
                    mPosition = position;
                }
            });

            // set click listeners
            Button buttonFavourite = (Button) rootView.findViewById(R.id.button_favourite);
            buttonFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContentValues values = new ContentValues();
                    values.put(MoviesContract.Columns.FAVOURITE, 1);

                    String[] selectionArgs = new String[]{String.valueOf(movie.movie_id)};
                    String selection = MoviesContract.Columns.MOVIE_ID + " = ?";
                    int _id = getActivity().getContentResolver()
                            .update(MoviesContract.getMovieUri(movie.movie_id), values, selection, selectionArgs);
                }
            });

        }

        return rootView;
    }

    private void setHeaderContent(View header, Movie movie) {
        TextView titleView = (TextView) header.findViewById(R.id.movie_title);
        TextView overviewView = (TextView) header.findViewById(R.id.overview);
        TextView ratingView = (TextView) header.findViewById(R.id.rating);
        TextView releaseDateView = (TextView) header.findViewById(R.id.release_date);
        TextView releaseYearView = (TextView) header.findViewById(R.id.release_year);
        ImageView thumbnailView = (ImageView) header.findViewById(R.id.thumbnail);
        String releaseYear = movie.releaseDate.substring(0, 4);

        Picasso.with(getActivity())
                .load(movie.imageUrl)
                .fit()
                .centerCrop()
                .into(thumbnailView);

        // set view content
        if (movie != null) {
            titleView.setText(movie.title);
            overviewView.setText(movie.overview);
            releaseDateView.setText(movie.releaseDate);
            releaseYearView.setText(releaseYear);
            ratingView.setText("Rating - " + movie.vote_average);

            //movie trailers
            loadMovieTrailers(movie.movie_id);
        }

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
                getActivity().getContentResolver().bulkInsert(VideosContract.getVideosUri(movieId), cvArray);

                // query locally saved data and swap adapter cursor to update list
                Cursor cursor = getActivity().getContentResolver().query(VideosContract.getVideosUri(movieId), VIDEO_COLUMNS, null, null, null);
                mVideoAdapter.swapCursor(cursor);

            }
        }, getActivity()).execute(String.valueOf(movieId));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = Utility.getSortOrder(getActivity());
        Uri videoUri = VideosContract.getVideosUri((long) 0);
        if (movie != null)
            videoUri = VideosContract.getVideosUri(movie.movie_id);
        return new CursorLoader(getActivity(),
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
