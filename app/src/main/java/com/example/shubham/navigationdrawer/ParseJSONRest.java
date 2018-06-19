package com.example.shubham.navigationdrawer;

/**
 * Created by Shubham on 29-07-2016.
 */
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Belal on 9/22/2015.
 */
public class ParseJSONRest {

    public static String[] restname;
    public static String[] city;
    public static String[] location;
    public static String[] image;
    public static String[] type;
    public static String[] deliverytime;

    public static int count=0;
    public static final String KEY_RESTNAME = "restname";
    public static final String KEY_CITY = "city";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_IMAGE="image";
    public static final String KEY_TYPE="type";
    public static final String KEY_TIME="deliverytime";
    public static final String JSON_ARRAY = "result";




    private JSONArray users = null;

    private String json;

    public ParseJSONRest(String json){
        this.json = json;
    }

    protected void parseJSONRest(){

        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);
            users = jsonObject.getJSONArray(JSON_ARRAY);

             count = users.length();


            restname = new String[users.length()];
            city = new String[users.length()];
            location = new String[users.length()];
            image =new String[users.length()];
            type = new String[users.length()];
            deliverytime= new String[users.length()];


            for(int i=0;i<users.length();i++){
                JSONObject jo = users.getJSONObject(i);
                restname[i] = jo.getString(KEY_RESTNAME);
                city[i] = jo.getString(KEY_CITY);
                location[i] = jo.getString(KEY_LOCATION);
                image[i]=jo.getString(KEY_IMAGE);
                type[i]=jo.getString(KEY_TYPE);
                deliverytime[i]=jo.getString(KEY_TIME);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch(NullPointerException e){
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static int getVar(){
        return count;
    }
}