package com.example.shubham.navigationdrawer;

/**
 * Created by Shubham on 19-07-2016.
 */

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
public class UserSessionManager {



    SharedPreferences pref;

    Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    public static final String PREFER_NAME = "Reg";

    public static final String IS_USER_LOGIN = "IsUserLoggedIn";

    public static final String KEY_PHONE = "phone";

    public static final String KEY_EMAIL = "Email";

    public static final String KEY_NAME = "name";

    public static final String KEY_ONLYMAIL = "onlyemail";
    public static final String KEY_PASSWORD = "txtpassword";
    public static final String KEY_LASTADD = "address";
    RestrauntLocationManagerPref sess;



    // Constructor
    public UserSessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void WholeAdd(String address){
        editor.putString(KEY_LASTADD,address);
        editor.commit();
    }

    public void OnlyMail(String mail){
        editor.putString(KEY_ONLYMAIL,mail);
        editor.commit();
    }

    //Create login session
    public void createUserLoginSession( String uEmail , String uPhone, String uName){
        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_EMAIL, uEmail);
        editor.putString(KEY_PHONE,uPhone);
        // Storing name in preferences
        editor.putString(KEY_NAME, uName);


        // Storing email in preferences

        // commit changes
        editor.commit();
    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     * */
    public boolean checkLogin(){
        // Check login status
        if(!this.isUserLoggedIn()){
            sess= new RestrauntLocationManagerPref(_context);
            HashMap<String, String> loc = sess.getLocaDetails();

            String sessioncityname = loc.get(RestrauntLocationManagerPref.KEY_CITY);

            // get name
            String sessionlocation = loc.get(RestrauntLocationManagerPref.KEY_LOCATION);

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginFrom.class);
            i.putExtra("city",sessioncityname);
            i.putExtra("location",sessionlocation);

            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);

            return true;

        }
return false;
    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){

        //Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();

        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        user.put(KEY_PHONE, pref.getString(KEY_PHONE, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        user.put(KEY_ONLYMAIL,pref.getString(KEY_ONLYMAIL,null));

        user.put(KEY_LASTADD,pref.getString(KEY_LASTADD,null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        sess= new RestrauntLocationManagerPref(_context);
        HashMap<String, String> loc = sess.getLocaDetails();

        String sessioncityname = loc.get(RestrauntLocationManagerPref.KEY_CITY);

        // get name
        String sessionlocation = loc.get(RestrauntLocationManagerPref.KEY_LOCATION);

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to MainActivity
        Intent i = new Intent(_context, Base.class);
        i.putExtra("city",sessioncityname);
        i.putExtra("location",sessionlocation);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }


    // Check for login
    public boolean isUserLoggedIn(){
        return pref.getBoolean(IS_USER_LOGIN, false);
    }
}