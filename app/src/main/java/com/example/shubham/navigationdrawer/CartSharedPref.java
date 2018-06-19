package com.example.shubham.navigationdrawer;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Shubham on 15-08-2016.
 */
public class CartSharedPref {

    SharedPreferences pref;

    SharedPreferences.Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    public static final String KEY_RESTNAME = "restname";

    public static final String KEY_ITEMTYPE = "itemtype";



    public static final String PREFER_NAME = "basket";

    public static final String KEY_QUANTITY = "quantity";

    public CartSharedPref(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void addtobasket(String quantity) {

        editor.putString(KEY_QUANTITY, quantity);
        editor.commit();
    }

    public HashMap<String, String> getbasket() {

        HashMap<String, String> add = new HashMap<String, String>();

        add.put(KEY_QUANTITY, pref.getString(KEY_QUANTITY, null));

        return add;
    }

    public void ItemNdRest(String restname,String itemtype){
        editor.putString(KEY_RESTNAME,restname);
        editor.putString(KEY_ITEMTYPE,itemtype);
        editor.commit();
    }

    public HashMap<String,String> getItemndRest(){
        HashMap<String,String> rest = new HashMap<String, String>();
        rest.put(KEY_RESTNAME, pref.getString(KEY_RESTNAME, null));
        rest.put(KEY_ITEMTYPE, pref.getString(KEY_ITEMTYPE,null));
        return rest;
    }
}
