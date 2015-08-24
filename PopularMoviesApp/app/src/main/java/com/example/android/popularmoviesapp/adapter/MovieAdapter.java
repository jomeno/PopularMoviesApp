package com.example.android.popularmoviesapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.android.popularmoviesapp.data.contracts.MoviesContract;
import com.squareup.picasso.Picasso;

/**
 * Created by Jomeno on 7/11/2015.
 */
public class MovieAdapter extends CursorAdapter {

    private static final int VIEW_TYPE_MOVIE = 1;
    private static final int VIEW_TYPE_COUNT = 1;

    /**
     * Cache of the children views for a movie list item.
     */
    public static class ViewHolder {
        public final ImageView imageView;

        public ViewHolder(View view) {
            imageView = (ImageView) view;
        }
    }

    public MovieAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public View getMovieView(Context context, Cursor cursor) {

        ImageView imageView = new ImageView(context);
        //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        imageView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 800));
        //imageView.setPadding(0,0,0,0);

        String imageUrl = cursor.getString(cursor.getColumnIndex(MoviesContract.Columns.IMAGE_URL));

        Picasso.with(context)
                .load(imageUrl)
                .fit()
                .centerCrop()
                        //.placeholder(R.mipmap.image_placeholder)
                        //.error(R.mipmap.no_image_placeholder)
                .into(imageView);
        return imageView;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = getMovieView(context, cursor);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        // consider refactoring to the setImageResource here

    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_MOVIE;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }
}
