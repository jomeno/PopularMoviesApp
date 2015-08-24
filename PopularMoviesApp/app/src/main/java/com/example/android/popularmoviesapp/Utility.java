package com.example.android.popularmoviesapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Jomeno on 8/24/2015.
 */
public class Utility {
    public static String getSortOrder(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_sort_key), context.getString(R.string.pref_sort_default));
    }
}
