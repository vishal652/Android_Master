package com.example.shubham.navigationdrawer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class Checkout extends AppCompatActivity {
    boolean connected=false;
    String gtotal;
    TextView checkout_tot,juxtc,deltime,picktime;
    RadioGroup radioGroup;
    UserSessionManager session;
    String DATA_URL = "http://tiredbuzz.16mb.com/restraunts/del_time.php?restname=";
    String ur;
    AllSharedPref pref;
    String restname,data,s1,s2,to;
    Button checkout;
    RadioButton selectedRadioButton;
    Checkout_Shared_Pref ch;
    Checkout_Shared_Pref checkpref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        deltime = (TextView)findViewById(R.id.deltime);
        picktime = (TextView)findViewById(R.id.picktime);
        checkout = (Button)findViewById(R.id.btn_checkout);

        pref = new AllSharedPref(getApplicationContext());
        ch = new Checkout_Shared_Pref(getApplicationContext());
        checkpref = new Checkout_Shared_Pref(getApplicationContext());

        pref.KAdd("zero");
        HashMap<String, String> rest = pref.getOpenDeliveryMenuDetails();
        restname = rest.get(AllSharedPref.KEY_RESTNAME);
        Toast.makeText(Checkout.this, restname, Toast.LENGTH_SHORT).show();


        juxtc = (TextView)findViewById(R.id.just_c);
        radioGroup = (RadioGroup)findViewById(R.id.radiogrp);

        session = new UserSessionManager(getApplicationContext());

        checkout_tot = (TextView)findViewById(R.id.checkout_total);



        checkInternetConenction();
        if(connected){
            new DeliveryTime().execute();
            checkout_tot.setVisibility(View.VISIBLE);
            juxtc.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.VISIBLE);
            checkout.setVisibility(View.VISIBLE);
            checkout_tot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view,"Connected Bro",Snackbar.LENGTH_LONG).show();


                }
            });
            checkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(radioGroup.getCheckedRadioButtonId()== -1){
                        Toast.makeText(Checkout.this, "Please choose you delivery type first", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        selectedRadioButton = (RadioButton)findViewById(selectedId);

                        if(selectedId==R.id.radio_pick){
                            String st = selectedRadioButton.getText().toString();
                            String ti = picktime.getText().toString();
                            ch.Deliver(st,ti);
                            if(session.isUserLoggedIn()){
                                checkpref.PickCheck("pickup");
                                pref.CheckOnly("In_Checkout");
                                Intent intent = new Intent(Checkout.this,Checkout_Pay.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(Checkout.this, "No login", Toast.LENGTH_SHORT).show();
                                pref.CheckOnly("In_Checkout");
                                pref.ActName("Checkout");
                                Intent log = new Intent(Checkout.this,LoginFrom.class);
                                startActivity(log);
                            }

                        }
                        else{
                            String st = selectedRadioButton.getText().toString();
                            String ti = deltime.getText().toString();
                            ch.Deliver(st,ti);

                            if(session.isUserLoggedIn()){
                                checkpref.PickCheck("del");
                                pref.CheckOnly("In_Checkout");
                                Intent intent = new Intent(Checkout.this,Checkout_Addn_Phone.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(Checkout.this, "No login", Toast.LENGTH_SHORT).show();
                                pref.CheckOnly("In_Checkout");
                                pref.ActName("Checkout");
                                Intent log = new Intent(Checkout.this,LoginFrom.class);
                                startActivity(log);
                            }
                        }

                    }


                }
            });

        }
        else{
            checkout_tot.setVisibility(View.INVISIBLE);
            juxtc.setVisibility(View.INVISIBLE);
            radioGroup.setVisibility(View.INVISIBLE);
            checkout.setVisibility(View.INVISIBLE);
        }
    }

    private boolean checkInternetConenction() {

        ConnectivityManager connec =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);


        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||

                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
              //  Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
                connected = true;

            return true;
        }else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
          //  Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            connected = false;
            return false;
        }
        return false;
    }


    class DeliveryTime extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                ur = DATA_URL+ URLEncoder.encode(restname, "UTF-8").replace("+", "%20");

            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            }

            try {
                URL url = new URL(ur);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = (InputStream)httpURLConnection.getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line=bufferedReader.readLine())!=null){
                    stringBuilder.append(line);
                }
                data = stringBuilder.toString();
                Log.e("Data : ",data);

                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray jsonArray1 = jsonObject.getJSONArray("result");
                    JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                    s1 = jsonObject1.getString("deliverytime");
                    s2 = jsonObject1.getString("pickup_time");



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(data==null){
                Toast.makeText(Checkout.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
            if(data!=null){
                deltime.setText("in "+ s1);
                picktime.setText("ready in " +s2 + " min.");
                HashMap<String, String> act = pref.getActivityName();
                to = act.get(AllSharedPref.KEY_GTOTAL);
                checkout_tot.setText(to);
            }



        }
    }

    @Override
    public void onBackPressed() {
        HashMap<String, String> act = pref.getActivityName();
        String activ = act.get(AllSharedPref.KEY_ACTIVITYNAME);
        if(activ.equals("Basket")||activ.equals("Checkout")||activ.equals("Show_delete")||activ.equals("Address_Book")||activ.equals("CheckoutAdd")){
            Intent intent = new Intent(Checkout.this,Basket.class);
            startActivity(intent);
        }

        super.onBackPressed();
    }
}
