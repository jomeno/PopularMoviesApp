package com.example.android.popularmoviesapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.popularmoviesapp.model.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailFragment extends Fragment {

    static final String DETAIL_URI = "URI";

    public static MovieDetailFragment newInstance(String param1, String param2) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Movie movie = new Movie();
        Bundle arguments = getArguments();

        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        rootView.setVisibility(View.GONE);

        if (arguments != null) {
            movie = arguments.getParcelable("selectedMovie");
            rootView.setVisibility(View.VISIBLE);
        }

        //movie = (Movie) getIntent().getParcelableExtra("selectedMovie");
        TextView titleView = (TextView) rootView.findViewById(R.id.movie_title);
        TextView overviewView = (TextView) rootView.findViewById(R.id.overview);
        TextView ratingView = (TextView) rootView.findViewById(R.id.rating);
        TextView releaseDateView = (TextView) rootView.findViewById(R.id.release_date);
        TextView releaseYearView = (TextView) rootView.findViewById(R.id.release_year);
        ImageView thumbnailView = (ImageView) rootView.findViewById(R.id.thumbnail);
        //String releaseYear = movie.releaseDate.substring(0, 4);

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
            //releaseYearView.setText(releaseYear);
            ratingView.setText("Rating - " + movie.vote_average);
        }

        // trailers
        ListView trailerList = (ListView) rootView.findViewById(R.id.listView);
        String[] trailers = new String[]{"Trailer 1", "Trailer 2", "Trailer 3", "Trailer 4"};
        ArrayAdapter<String> trailerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_trailer,
                R.id.item_trailer_text, trailers);
        trailerList.setAdapter(trailerAdapter);

        return rootView;
    }


}
