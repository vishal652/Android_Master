package com.example.shubham.navigationdrawer;

/**
 * Created by Shubham on 29-07-2016.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Belal on 9/22/2015.
 */
public class ParseJSONProduct {

    public static String[] itemname;
    public static String[] description;
    public static String[] price;

    public static int count=0;
    public static final String KEY_ITEMNAME = "item_name";
    public static final String KEY_DESC = "description";
    public static final String KEY_PRICE = "price";
    public static final String JSON_ARRAY = "result";

    private JSONArray users = null;

    private String json;

    public ParseJSONProduct(String json){
        this.json = json;
    }

    protected void parseJSONProduct(){

        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);
            users = jsonObject.getJSONArray(JSON_ARRAY);

            count = users.length();

            itemname = new String[users.length()];
            description = new String[users.length()];
            price = new String[users.length()];

            for(int i=0;i<users.length();i++){
                JSONObject jo = users.getJSONObject(i);
                itemname[i] = jo.getString(KEY_ITEMNAME);
                description[i] = jo.getString(KEY_DESC);
                price[i] = jo.getString(KEY_PRICE);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static int getVar(){
        return count;
    }
}