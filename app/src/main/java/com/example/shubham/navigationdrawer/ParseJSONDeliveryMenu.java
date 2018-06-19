package com.example.shubham.navigationdrawer;

/**
 * Created by Shubham on 29-07-2016.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Belal on 9/22/2015.
 */
public class ParseJSONDeliveryMenu {

    public static String[] item_type;


    public static final String KEY_ITEMTYPE = "item type";
    public static final String JSON_ARRAY = "result";
    public static int count=0;




    private JSONArray users = null;

    private String json;

    public ParseJSONDeliveryMenu(String json){
        this.json = json;
    }

    protected void parseJSONDelivery(){

        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);
            users = jsonObject.getJSONArray(JSON_ARRAY);

            count= users.length();

            item_type = new String[users.length()];

            for(int i=0;i<users.length();i++){
                JSONObject jo = users.getJSONObject(i);
                item_type[i] = jo.getString(KEY_ITEMTYPE);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch(NullPointerException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static int getVar(){
        return count;
    }



}