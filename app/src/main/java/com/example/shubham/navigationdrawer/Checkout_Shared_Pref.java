package com.example.shubham.navigationdrawer;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Shubham on 11-10-2016.
 */

public class Checkout_Shared_Pref {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    public static final String PREFER_NAME = "Checkout";
    public static final String KEY_DELIVER = "deliver";
    public static final String KEY_DEL_TIME = "deltime";
    public static final String KEY_SUBTOT = "subtotal";
    public static final String KEY_PAY_METHOD = "paymethod";
    public static final String KEY_PICKCHECK = "check_pick";



    public Checkout_Shared_Pref(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void PickCheck(String che){
        editor.putString(KEY_PICKCHECK,che);
        editor.commit();
    }

    public void SubTot(String tot){
        editor.putString(KEY_SUBTOT,tot);
        editor.commit();
    }

    public void PayMethod(String payme){
        editor.putString(KEY_PAY_METHOD,payme);
        editor.commit();
    }

    public void Deliver(String del , String time){
        editor.putString(KEY_DELIVER,del);
        editor.putString(KEY_DEL_TIME,time);
        editor.commit();
    }

    public HashMap<String, String> getDeliverDetails(){
        HashMap<String, String> details = new HashMap<String, String>();
        details.put(KEY_DEL_TIME, pref.getString(KEY_DEL_TIME, null));
        details.put(KEY_DELIVER, pref.getString(KEY_DELIVER, null));
        details.put(KEY_PAY_METHOD, pref.getString(KEY_PAY_METHOD,null));
        details.put(KEY_SUBTOT, pref.getString(KEY_SUBTOT,null));
        details.put(KEY_PICKCHECK, pref.getString(KEY_PICKCHECK,null));
        return details;
    }

    public void ClearData(){
        editor.clear();
        editor.commit();
    }
}
