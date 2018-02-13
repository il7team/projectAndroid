package com.bitm.alfa_travel_mate.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by nexttel_1 on 5/17/2017.
 */

public class Utils {

    public static String userId="";
    public final static String PREF_USER_ID = "user_id";
    public final static String FIRE_EVENTS = "Events";
    public final static String FIRE_MOMENTS = "Moments";
    public final static String FIRE_USERS = "Users";
    public final static String FIRE_EXPENSES = "ExpenceData";

    public static void saveToPrefs(Context context, String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public static String getFromPrefs(Context context, String key, String defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            return sharedPrefs.getString(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

}
