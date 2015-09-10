package com.example.android.popularmoviesapp.sync;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.popularmoviesapp.R;
import com.example.android.popularmoviesapp.data.contract.VideosContract;

/**
 * Created by Jomeno on 9/8/2015.
 */
public class VideoAdapter extends CursorAdapter {

    public VideoAdapter(Context context, Cursor c, int flags){
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie_trailer, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder)view.getTag();
        viewHolder.trailerTextView.setText(cursor.getString(cursor.getColumnIndex(VideosContract.Columns.NAME)));
    }

    public static class ViewHolder{
        public final TextView trailerTextView;

        public ViewHolder(View view){
            trailerTextView = (TextView)view.findViewById(R.id.item_trailer_text);
        }

    }
}
