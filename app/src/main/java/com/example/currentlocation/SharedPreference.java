package com.example.currentlocation;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {
    SharedPreferences sharedPreference;

    public SharedPreference(Context context){
        sharedPreference = context.getSharedPreferences("location_pref" ,MODE_PRIVATE);
    }

    public void clear()
    {
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor myEdit = sharedPreference.edit();
        myEdit.clear();
        myEdit.apply();
    }

    public void insert_current_location(String current_location)
    {
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor myEdit = sharedPreference.edit();
        myEdit.putString("current_location" ,current_location);
        myEdit.apply();
    }

    public String get_current_location()
    {
        return sharedPreference.getString("current_location" ,"0.0");
    }
}
