package com.example.android.popularmoviesapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.android.popularmoviesapp.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Jomeno on 7/11/2015.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {

    ArrayList<Movie> movies;

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        super(context, 0, movies);
        this.movies = movies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            imageView = new ImageView(getContext());
            imageView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 800));
            //imageView.setPadding(0,0,0,0);
        }else{
            imageView = (ImageView)convertView;
        }

        if (position < 0 || position >= movies.size()) return null;
        Movie movie = movies.get(position);

        Picasso.with(getContext())
                .load(movie.imageUrl)
                .fit()
                .centerCrop()
                //.placeholder(R.mipmap.image_placeholder)
                //.error(R.mipmap.no_image_placeholder)
                .into(imageView);
        return imageView;
    }
}
