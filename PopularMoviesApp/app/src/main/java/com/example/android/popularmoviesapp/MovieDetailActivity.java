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


        //getLoaderManager().initLoader(VIDEO_LOADER, null, this);

        /*// create adapter and attach to list view
        mVideoAdapter = new VideoAdapter(this, null, 0);
        mListView = (ListView) findViewById(R.id.trailer_list);

        // set list view header
        View header = getLayoutInflater().inflate(R.layout.item_movie_header, mListView, false);
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
        });*/

    }

    /*private void setHeaderContent(View header, Movie movie) {
        TextView titleView = (TextView) header.findViewById(R.id.movie_title);
        TextView overviewView = (TextView) header.findViewById(R.id.overview);
        TextView ratingView = (TextView) header.findViewById(R.id.rating);
        TextView releaseDateView = (TextView) header.findViewById(R.id.release_date);
        TextView releaseYearView = (TextView) header.findViewById(R.id.release_year);
        ImageView thumbnailView = (ImageView) header.findViewById(R.id.thumbnail);
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
                getContentResolver().bulkInsert(VideosContract.getVideosUri(movieId), cvArray);

            }
        }, this).execute(String.valueOf(movieId));
    }*/

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

/*    @Override
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
    }*/


}
