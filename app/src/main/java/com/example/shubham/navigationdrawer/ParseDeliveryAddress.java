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
public class ParseDeliveryAddress {

    public static String[] uid;
    public static String[] city;
    public static String[] location;
    public static String[] company;
    public static String[] flat_no;
    public static String[] apartment;
    public static String[] postcode;
    public static String[] other_address;

    public static final String KEY_ID = "uid";
    public static final String KEY_CITY = "city";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_COMPANY = "company";
    public static final String KEY_FLATNO = "flat_no";
    public static final String KEY_APARTMENT = "apartment";
    public static final String KEY_POSTCODE = "postcode";
    public static final String KEY_OTHERADD = "other_address";
    public static final String JSON_ARRAY = "result";
    public static int count=0;

    private JSONArray users = null;

    private String json;

    public ParseDeliveryAddress(String json){
        this.json = json;
    }

    protected void parseJSONDelivery(){

        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);
            users = jsonObject.getJSONArray(JSON_ARRAY);

            count= users.length();

            uid = new String[users.length()];
            city = new String[users.length()];
            location = new String[users.length()];
            company = new String[users.length()];
            flat_no = new String[users.length()];
            apartment = new String[users.length()];
            postcode = new String[users.length()];
            other_address = new String[users.length()];


            for(int i=0;i<users.length();i++){
                JSONObject jo = users.getJSONObject(i);
                uid[i] = jo.getString(KEY_ID);
                city[i] = jo.getString(KEY_CITY);
                location[i] = jo.getString(KEY_LOCATION);
                company[i] = jo.getString(KEY_COMPANY);
                flat_no[i] = jo.getString(KEY_FLATNO);
                apartment[i] = jo.getString(KEY_APARTMENT);
                postcode[i] = jo.getString(KEY_POSTCODE);
                other_address[i] = jo.getString(KEY_OTHERADD);

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