package com.example.android.popularmoviesapp.sync;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.popularmoviesapp.R;
import com.example.android.popularmoviesapp.data.contract.VideosContract;
import com.example.android.popularmoviesapp.model.MovieAsset;

/**
 * Created by Jomeno on 9/8/2015.
 */
public class VideoAdapter extends CursorAdapter {

    private static final int VIEW_TYPE_COUNT = 3;
    private static final int VIEW_TYPE_SECTION_HEADER = 0;
    private static final int VIEW_TYPE_TRAILER_ITEM = 1;
    private static final int VIEW_TYPE_REVIEW_ITEM = 2;

    MovieAsset movieAsset;

    private int cursorViewType = 0;

    public VideoAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    /*@Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }*/

    /*@Override
    public int getItemViewType(int position) {
        switch (movieAsset.VIEW_TYPE) {

            case VIEW_TYPE_TRAILER_ITEM:
                return VIEW_TYPE_TRAILER_ITEM;
            case VIEW_TYPE_REVIEW_ITEM:
                return VIEW_TYPE_REVIEW_ITEM;
            default:
                return VIEW_TYPE_SECTION_HEADER;
        }

    }*/

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        //movieAsset = ((MovieAsset)cursor);
        /*int viewType = cursor.getInt(cursor.getColumnIndex("VIEW_TYPE"));//getItemViewType(cursor.getPosition());
        int layout = -1;

        switch(viewType){
            case VIEW_TYPE_SECTION_HEADER:
                layout = R.layout.item_movie_detail_section_header;
                break;
            case VIEW_TYPE_TRAILER_ITEM:
                layout = R.layout.item_movie_detail_trailer;
                break;
            case VIEW_TYPE_REVIEW_ITEM:
                layout = R.layout.item_movie_detail_review;
                break;
        }*/

        //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie_detail_trailer, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.itemTextView.setText(cursor.getString(cursor.getColumnIndex(VideosContract.Columns.NAME)));
    }

    public static class ViewHolder {
        public final TextView itemTextView;

        public ViewHolder(View view) {
            itemTextView = (TextView) view.findViewById(R.id.item_section_text);
        }

    }
}
