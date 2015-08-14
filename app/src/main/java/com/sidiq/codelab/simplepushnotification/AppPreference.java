package com.sidiq.codelab.simplepushnotification;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Sidiq on 14/08/2015.
 */
public class AppPreference {
    Activity activity;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private String KEY_USERNAME = "username";
    private String KEY_PREF_NAME = "simple_gcm";

    public AppPreference(Activity activity){
        this.activity = activity;
        preferences = activity.getSharedPreferences(KEY_PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setUsername(String username){
        editor.putString(KEY_USERNAME, username);
        editor.commit();
    }

    public String getUsername(){
        return preferences.getString(KEY_USERNAME, "");
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }

}
