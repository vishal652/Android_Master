package com.example.shubham.navigationdrawer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.UUID;

public class Checkout_Details extends AppCompatActivity implements View.OnClickListener {

    boolean connected=false;
    TextView type_detail,time_detail,address_detail,payment_detail,checkout_tot,subtotal_bef_dis,dicount,service_fee,subtotal,delivery_fee,grand_total;
    AllSharedPref all;
    String tot,type,time,wholeadd,paymethod,amount,pick;
    double dis,stotal,srv_fee,del_fee,gtot;
    double amt;
    Checkout_Shared_Pref pref;
    UserSessionManager session;
    TextView edit_type,edit_add,edit_pay_method;
    View view1;
    LinearLayout lnadd;
    Button place_order;
    private static int inc = 0;
    long no;
    MyCartDatabse myDatabase;
    SQLiteDatabase sql;
    ContentValues cv;
    public static String[] quan =null;
    public static  String[] itemname=null;
    public static String[] baseprice=null;
    String number,uname,uquan,uprice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_details);


        type_detail = (TextView) findViewById(R.id.type_detail);
        time_detail = (TextView) findViewById(R.id.time_detail);
        address_detail = (TextView) findViewById(R.id.address_detail);
        payment_detail = (TextView) findViewById(R.id.payment_detail);
        checkout_tot = (TextView) findViewById(R.id.checkout_total_place_order);
        subtotal_bef_dis = (TextView) findViewById(R.id.subtot_bef_dis);
        dicount = (TextView) findViewById(R.id.discount);
        subtotal = (TextView) findViewById(R.id.subtotal);
        service_fee = (TextView) findViewById(R.id.service_fee);
        delivery_fee = (TextView) findViewById(R.id.delivery_fee);
        grand_total = (TextView) findViewById(R.id.total_place_order);
        edit_type = (TextView) findViewById(R.id.edit_type);
        edit_add = (TextView) findViewById(R.id.edit_add);
        edit_pay_method = (TextView) findViewById(R.id.edit_payment_method);
        view1 = (View) findViewById(R.id.view1);
        lnadd = (LinearLayout) findViewById(R.id.lnadd);
        place_order = (Button) findViewById(R.id.btn_place_order);


        myDatabase = new MyCartDatabse(this,"mydb",null,1);
        sql = myDatabase.getWritableDatabase();
        cv = new ContentValues();

        checkInternetConenction();

        all = new AllSharedPref(getApplicationContext());
        pref = new Checkout_Shared_Pref(getApplicationContext());
        session = new UserSessionManager(getApplicationContext());

        HashMap<String, String> ty = pref.getDeliverDetails();
        type = ty.get(Checkout_Shared_Pref.KEY_DELIVER);
        time = ty.get(Checkout_Shared_Pref.KEY_DEL_TIME);
        paymethod = ty.get(Checkout_Shared_Pref.KEY_PAY_METHOD);
        amount = ty.get(Checkout_Shared_Pref.KEY_SUBTOT);
        pick = ty.get(Checkout_Shared_Pref.KEY_PICKCHECK);


        amt = Double.parseDouble(amount);


        HashMap<String, String> h = session.getUserDetails();
        wholeadd = h.get(UserSessionManager.KEY_LASTADD);



        if (connected) {
            try {
                if (pick.equals("pickup")) {
                    view1.setVisibility(View.INVISIBLE);
                    lnadd.setVisibility(View.INVISIBLE);
                } else if (pick.equals("del")) {
                    view1.setVisibility(View.VISIBLE);
                    lnadd.setVisibility(View.VISIBLE);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            checkout_tot.setVisibility(View.VISIBLE);
            type_detail.setText(type);
            time_detail.setText(time);
            address_detail.setText(wholeadd);
            payment_detail.setText(paymethod + " Payment");
            getTotal();
            discount();
            subtot();
            srvicefee();
            cal_del_fee();
            cal_grand_tot();


        }


        if (checkout_tot.getVisibility() == View.VISIBLE) {
            HashMap<String, String> act = all.getActivityName();
            tot = act.get(AllSharedPref.KEY_GTOTAL);
            checkout_tot.setText(tot);
        }

        edit_add.setOnClickListener(this);
        edit_type.setOnClickListener(this);
        edit_pay_method.setOnClickListener(this);
        place_order.setOnClickListener(this);



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

    public void getTotal(){
        subtotal_bef_dis.setText("Rs. "+amount);
    }

    public void discount(){
        dis= 0;
        dicount.setText("-Rs. "+String.valueOf(dis));
    }

    public void subtot(){
        stotal = amt-dis;
        subtotal.setText("Rs. "+String.valueOf(stotal));
    }

    public void srvicefee(){
        srv_fee = 0;
        service_fee.setText("Rs. "+String.valueOf(srv_fee));
    }

    public void cal_del_fee(){
        del_fee =0;
        delivery_fee.setText("Rs. "+String.valueOf(del_fee));
    }

    public void cal_grand_tot(){
        gtot = stotal+srv_fee+del_fee;
        grand_total.setText("Rs. "+String.valueOf(gtot));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edit_add:
                all.KAdd("zero");
                Intent i1 = new Intent(Checkout_Details.this,Checkout_Addn_Phone.class);
                startActivity(i1);
                break;

            case R.id.edit_payment_method:
                Intent i2 = new Intent(Checkout_Details.this,Checkout_Pay.class);
                startActivity(i2);
                break;

            case R.id.edit_type:
                Intent i3 = new Intent(Checkout_Details.this,Checkout.class);
                startActivity(i3);
                break;

            case R.id.btn_place_order:
                no = getId();
                number = String.valueOf(no);
                getRecords();
                Toast.makeText(this, number, Toast.LENGTH_SHORT).show();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Checkout_Details.this,Checkout_Pay.class);
        startActivity(intent);
        super.onBackPressed();
    }

    private static long getId(){

        long id = Long.parseLong(String.valueOf(System.currentTimeMillis())
                .substring(1,10)
                .concat(String.valueOf(inc)));
        inc = (inc+1)%10;
        return id;
    }

    public void getRecords(){

        sql = myDatabase.getReadableDatabase();
        Cursor cursor = sql.rawQuery("select * from cart ",null);
        quan = new String[cursor.getCount()];
        itemname = new String[cursor.getCount()];
        baseprice = new String[cursor.getCount()];


        int i = 0;
        if(cursor.getCount()>0){
            while(cursor.moveToNext()){
                uquan = cursor.getString(5);
                uname = cursor.getString(1);
                uprice = cursor.getString(4);


                quan[i] = uquan;
                itemname[i] = uname;
                baseprice[i] = uprice;


                new Signup().execute(number, uname, uprice, uquan);

                i++;

                Toast.makeText(this, uname + uprice+ uquan  , Toast.LENGTH_SHORT).show();
            }
        }
        else{

        }

        cursor.close();


    }



    public class Signup extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... arg0) {
            String order_id = arg0[0];
            String itemname = arg0[1];
            String itemprice = arg0[2];
            String quantity = arg0[3];

            String link;
            String data;
            BufferedReader bufferedReader;
            String result;

            try {
                data = "?order_id=" + URLEncoder.encode(number, "UTF-8");
                data += "&itemname=" + URLEncoder.encode(uname, "UTF-8");
                data += "&itemprice=" + URLEncoder.encode(uprice, "UTF-8");
                data += "&quantity=" + URLEncoder.encode(uquan, "UTF-8");

                link = "http://tiredbuzz.16mb.com/orders/orders_desc.php" + data;
                URL url = new URL(link);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                result = bufferedReader.readLine();
                return result;
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {


            String jsonStr = result;
            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);
                    String query_result = jsonObj.getString("query_result");
                    if (query_result.equals("SUCCESS")) {
                        Toast.makeText(Checkout_Details.this, "Successfull", Toast.LENGTH_SHORT).show();


                    } else if (query_result.equals("FAILURE")) {
                        Toast.makeText(getApplicationContext(), " EmailAddress or PhoneNumber Already exists.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Couldn't connect to remote database.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Could not connect to Internet.Please Check you internet connection.", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Couldn't get any JSON data.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
