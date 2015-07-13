package com.example.android.popularmoviesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesapp.model.Movie;
import com.squareup.picasso.Picasso;


public class MovieDetailActivity extends AppCompatActivity {

    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movie = (Movie) getIntent().getParcelableExtra("selectedMovie");
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

        // set view content
        if (movie != null) {
            titleView.setText(movie.title);
            overviewView.setText(movie.overview);
            releaseDateView.setText(movie.releaseDate);
            releaseYearView.setText(releaseYear);
            ratingView.setText("Rating - "+movie.rating);
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
