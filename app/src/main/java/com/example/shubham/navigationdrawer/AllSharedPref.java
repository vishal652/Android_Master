package com.example.shubham.navigationdrawer;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Shubham on 10-08-2016.
 */
public class AllSharedPref {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    public static final String PREFER_NAME = "All";
    public static final String KEY_RESTNAME = "restname";
    public static final String KEY_CITYLOC = "cityloc";
    public static final String KEY_TYPE = "type";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_QUAN = "quan";
    public static final String KEY_POSITION = "pos";
    public static final String KEY_ITEMNAME = "itemname";
    public static final String KEY_ACTIVITYNAME = "activity";
    public static final String KEY_GTOTAL="gtotal";
    public static final String KEY_NAMEREST = "rest";
    public static final String KEY_SELECT = "select";
    public static final String KEY_CHECK = "forcheck";
    public static final String KEY_RAND= "random";
    public static final String KEY_ADD = "random2";

    public static final String KEY_COMP = "company_name";
    public static final String KEY_FLAT = "flat_name";
    public static final String KEY_APART = "apartment_name";
    public static final String KEY_LOC = "location_name";
    public static final String KEY_LAND = "landmark_name";
    public static final String KEY_POST = "postcode_name";
    public static final String KEY_CIT = "city_name";


    public AllSharedPref(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void AddressOne(String comp,String flat,String apart,String loc,String land,String post,String cit){
        editor.putString(KEY_COMP,comp);
        editor.putString(KEY_FLAT,flat);
        editor.putString(KEY_APART,apart);
        editor.putString(KEY_LOC,loc);
        editor.putString(KEY_LAND,land);
        editor.putString(KEY_POST,post);
        editor.putString(KEY_CIT,cit);

    }



    public void KAdd(String kadd){
        editor.putString(KEY_ADD,kadd);
        editor.commit();
    }
    public void Rand(String rand){
        editor.putString(KEY_RAND,rand);
        editor.commit();
    }

    public void CheckOnly(String check){
        editor.putString(KEY_CHECK,check);
        editor.commit();
    }

    public void Select(String sel){
        editor.putString(KEY_SELECT,sel);
        editor.commit();
    }

    public void Rest(String rest){
        editor.putString(KEY_NAMEREST,rest);
        editor.commit();
    }

    public void Quan(int quan){
        editor.putInt(KEY_QUAN,quan);
        editor.commit();
    }

    public void Quantity(String quantity){
        editor.putString(KEY_QUANTITY,quantity);
        editor.commit();
    }

    public void Pos(String pos){
        editor.putString(KEY_POSITION,pos);
        editor.commit();
    }

    public void ItemName(String itemname){
        editor.putString(KEY_ITEMNAME,itemname);
        editor.commit();
    }

    public void ActName(String activ){
        editor.putString(KEY_ACTIVITYNAME,activ);
        editor.commit();
    }

    public void Gtotal(String gtotal){
        editor.putString(KEY_GTOTAL,gtotal);
        editor.commit();
    }

    public void OpenDeliveryMenuSession( String restname , String cityloc, String type){

        editor.putString(KEY_RESTNAME, restname);

        editor.putString(KEY_CITYLOC, cityloc);

        editor.putString(KEY_TYPE, type);

        editor.commit();
    }


    public HashMap<String, String> getAddress(){

        HashMap<String, String> ge = new HashMap<String, String>();

        ge.put(KEY_COMP, pref.getString(KEY_COMP, null));
        ge.put(KEY_FLAT, pref.getString(KEY_FLAT, null));
        ge.put(KEY_CIT, pref.getString(KEY_CIT,null));
        ge.put(KEY_LOC, pref.getString(KEY_LOC,null));
        ge.put(KEY_LAND, pref.getString(KEY_LAND,null));
        ge.put(KEY_APART, pref.getString(KEY_APART,null));
        ge.put(KEY_POST, pref.getString(KEY_POST,null));

        return ge;
    }

    public HashMap<String, String> getPosQuan(){

        HashMap<String, String> ge = new HashMap<String, String>();

        ge.put(KEY_QUANTITY, pref.getString(KEY_QUANTITY, null));

        ge.put(KEY_POSITION, pref.getString(KEY_POSITION, null));

        ge.put(KEY_ITEMNAME, pref.getString(KEY_ITEMNAME,null));

        return ge;
    }





    public HashMap<String, String> getOpenDeliveryMenuDetails(){

        HashMap<String, String> open = new HashMap<String, String>();

        open.put(KEY_RESTNAME, pref.getString(KEY_RESTNAME, null));

        open.put(KEY_CITYLOC, pref.getString(KEY_CITYLOC, null));

        open.put(KEY_TYPE, pref.getString(KEY_TYPE, null));

        return open;
    }

    public HashMap<String, String> getActivityName() {


        HashMap<String, String> act = new HashMap<String, String>();
        act.put(KEY_ACTIVITYNAME, pref.getString(KEY_ACTIVITYNAME, null));
        act.put(KEY_GTOTAL,pref.getString(KEY_GTOTAL,null));
        act.put(KEY_NAMEREST,pref.getString(KEY_NAMEREST,null));
        act.put(KEY_SELECT,pref.getString(KEY_SELECT,null));
        act.put(KEY_CHECK,pref.getString(KEY_CHECK,null));
        act.put(KEY_RAND,pref.getString(KEY_RAND,null));
        act.put(KEY_ADD,pref.getString(KEY_ADD,null));

        return act;
    }


}
