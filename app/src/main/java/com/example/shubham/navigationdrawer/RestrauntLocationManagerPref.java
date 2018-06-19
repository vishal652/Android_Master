package com.example.shubham.navigationdrawer;

/**
 * Created by Shubham on 19-07-2016.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class RestrauntLocationManagerPref {

    SharedPreferences pref;

    Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    public static final String PREFER_NAME = "Loc";

    //  public static final String IS_USER_LOGIN = "IsUserLoggedIn";

    public static final String KEY_CITY = "city1";

    public static final String KEY_LOCATION = "location1";

    //   public static final String KEY_NAME = "name";

    // public static final String KEY_PASSWORD = "txtpassword";


    // Constructor
    public RestrauntLocationManagerPref(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //Create login session
    public void createRestSession(String uCity, String uLocation) {
        // Storing login value as TRUE
        // editor.putBoolean(IS_USER_LOGIN, true);

        // Storing name in preferences
        editor.putString(KEY_CITY, uCity);

        editor.putString(KEY_LOCATION, uLocation);
        // Storing email in preferences
        // editor.putString(KEY_EMAIL, uEmail);

        // commit changes
        editor.commit();
    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     */
//    public boolean checkLogin() {
//        // Check login status
//        if (!this.isUserLoggedIn()) {
//
//            // user is not logged in redirect him to Login Activity
//            Intent i = new Intent(_context, Open.class);
//
//            // Closing all the Activities from stack
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            // Add new Flag to start new Activity
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            // Staring Login Activity
//            _context.startActivity(i);
//
//            return true;
//
//        }
//        return false;
//    }


    /**
     * Get stored session data
     */
    public HashMap<String, String> getLocaDetails() {

        //Use hashmap to store user credentials
        HashMap<String, String> loc = new HashMap<String, String>();

        // user name
        loc.put(KEY_CITY, pref.getString(KEY_CITY, null));

        loc.put(KEY_LOCATION, pref.getString(KEY_LOCATION, null));

        // user email id
        //  user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // return user
        return loc;
    }
}
    /**
     * Clear session details
     */
//    public void logoutUser() {
//
//        // Clearing all user data from Shared Preferences
//        editor.clear();
//        editor.commit();
//
//        // After logout redirect user to MainActivity
//        Intent i = new Intent(_context, Restaurant.class);
//
//        // Closing all the Activities
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        // Add new Flag to start new Activity
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        // Staring Login Activity
//        _context.startActivity(i);
//    }
//}

    // Check for login
//    public boolean isUserLoggedIn(){
//        return pref.getBoolean(IS_USER_LOGIN, false);
//    }
//}