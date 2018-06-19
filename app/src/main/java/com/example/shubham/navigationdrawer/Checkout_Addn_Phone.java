package com.example.shubham.navigationdrawer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class Checkout_Addn_Phone extends AppCompatActivity implements View.OnClickListener {

    boolean connected=false;
    AllSharedPref all;
    UserSessionManager session;
    String tot,ph,email,kadd,wholeadd;
    TextView checkout_total_add,choose_add,adrs,phoneNo;
    Button select;
    String city_name,company_name,location_name,flat_name,apartment_name,landmark_name,postcode_name,activ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_addn_phone);

        checkout_total_add = (TextView)findViewById(R.id.checkout_total_add);
        choose_add = (TextView)findViewById(R.id.choose_add);
        adrs = (TextView)findViewById(R.id.address);
        phoneNo = (TextView)findViewById(R.id.phoneNumber);
        select = (Button)findViewById(R.id.btn_select_add_phone);

        checkInternetConenction();
        all = new AllSharedPref(getApplicationContext());
        session = new UserSessionManager(getApplicationContext());

        HashMap<String,String> h = session.getUserDetails();
        ph = h.get(UserSessionManager.KEY_PHONE);
        email = h.get(UserSessionManager.KEY_ONLYMAIL);

        HashMap<String,String> m = all.getActivityName();
        kadd = m.get(AllSharedPref.KEY_ADD);
        wholeadd = h.get(UserSessionManager.KEY_LASTADD);
        activ = m.get(AllSharedPref.KEY_ACTIVITYNAME);

        SharedPreferences settings = getApplicationContext().getSharedPreferences("Checkout", Context.MODE_PRIVATE);
        settings.edit().remove("check_pick").commit();


        if(connected){
            checkout_total_add.setVisibility(View.VISIBLE);
            phoneNo.setText(ph);

        }
        if(checkout_total_add.getVisibility()==View.VISIBLE){
            HashMap<String, String> act = all.getActivityName();
            tot = act.get(AllSharedPref.KEY_GTOTAL);
            checkout_total_add.setText(tot);
        }


        select.setOnClickListener(this);
        choose_add.setOnClickListener(this);


        try{
                if(kadd.equals("kadd")){
                    Intent ige = getIntent();
                    adrs.setVisibility(View.VISIBLE);
                    city_name = ige.getStringExtra("city_name");
                    location_name = ige.getStringExtra("location_name");
                    company_name = ige.getStringExtra("company_name");
                    flat_name = ige.getStringExtra("flat_name");
                    apartment_name = ige.getStringExtra("apartment_name");
                    landmark_name = ige.getStringExtra("landmark_name");
                    postcode_name = ige.getStringExtra("postcode_name");
                    Toast.makeText(this, city_name, Toast.LENGTH_SHORT).show();
                    adrs.setText(company_name + "\n" + "Flat No. / House No.: " + flat_name + "\n" + apartment_name + "\n" + location_name
                            +"\n" + "Landmark: " + landmark_name + "\n" + postcode_name + "\n" + city_name);

                    String ad = adrs.getText().toString();
                    session.WholeAdd(ad);
                }
                else if(kadd.equals("fu")||kadd.equals("zero")){
//                    adrs.setVisibility(View.INVISIBLE);
                    if(wholeadd!=null){
                        adrs.setVisibility(View.VISIBLE);
                        adrs.setText(wholeadd);
                    }
                    else {
                        adrs.setVisibility(View.INVISIBLE);
                    }
                }



        }catch (NullPointerException e){
            e.printStackTrace();
        }



    }

    private boolean checkInternetConenction() {

        ConnectivityManager connec =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);


        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||

                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
           // Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
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

    @Override
    public void onClick(View view) {
        checkInternetConenction();
        switch (view.getId()){
            case R.id.choose_add:
                if(connected){
                    all.ActName("CheckoutAdd");
                    Intent in = new Intent(Checkout_Addn_Phone.this,Address_Book.class);
                    in.putExtra("emailcheckout",email);
                    startActivity(in);
                }
                else{
                    Toast.makeText(Checkout_Addn_Phone.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_select_add_phone:
                if(connected){
                    if(adrs.getVisibility()==View.INVISIBLE){
                        Toast.makeText(this, "Please Choose your Address first", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Intent intent = new Intent(Checkout_Addn_Phone.this,Checkout_Pay.class);
                        startActivity(intent);
                    }
                }
                else{
                    Toast.makeText(Checkout_Addn_Phone.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                }
                break;

        }

    }
    @Override
    public void onBackPressed() {
        Intent in = new Intent(this,Checkout.class);
        startActivity(in);
        super.onBackPressed();
    }
}
