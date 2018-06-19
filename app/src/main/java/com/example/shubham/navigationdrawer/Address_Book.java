package com.example.shubham.navigationdrawer;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class Address_Book extends Open {

    ListView listadd;
    Toolbar toolbar;
    boolean connected =false;
    UserSessionManager session;
    public static final String DATA_URL = "http://tiredbuzz.16mb.com/address_book/address_book.php?phonenumber=";
    public static final String DATA_UR = "http://tiredbuzz.16mb.com/accountdetails.php?emailaddress=";
    public static final String KEY_NAME = "fullName";
    public static final String KEY_PHONE = "phoneNumber";
    public static final String JSON_ARRAY = "result";
    String ur,phonenumber,data,city,location,activ,data1;
    RestrauntLocationManagerPref sess;
    AllSharedPref all;
    String email,phone,name,check,select,rand;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_address__book, null, false);
        mDrawerLayout.addView(contentView, 0);
        navigationView.getMenu().getItem(5).setChecked(true);

        listadd = (ListView)findViewById(R.id.list_address);
        session = new UserSessionManager(getApplicationContext());
        all = new AllSharedPref(getApplicationContext());
        sess = new RestrauntLocationManagerPref(getApplicationContext());

        HashMap<String,String> sel = all.getActivityName();
        select = sel.get(AllSharedPref.KEY_SELECT);

        HashMap<String, String> loc = sess.getLocaDetails();



        toolbar = (Toolbar) findViewById(R.id.toolbar_address_book);
        setSupportActionBar(toolbar);







        SharedPreferences myPrefs = this.getSharedPreferences("Loc", MODE_WORLD_READABLE);
        city = myPrefs.getString("city1",null);
        location = myPrefs.getString("location1",null);

        if (location != null && city != null )
        {
            navigationView.getMenu().findItem(R.id.restaraunts).setVisible(true);
        }
        else {
            navigationView.getMenu().findItem(R.id.restaraunts).setVisible(false);
        }

        if(session.isUserLoggedIn()){
            navigationView.getMenu().findItem(R.id.address_book).setVisible(true);
        }
        else{
            navigationView.getMenu().findItem(R.id.address_book).setVisible(false);
        }

        HashMap<String, String> user = session.getUserDetails();
        phonenumber = user.get(UserSessionManager.KEY_PHONE);
        email = user.get(UserSessionManager.KEY_ONLYMAIL);




        HashMap<String, String> actm = all.getActivityName();
        activ = actm.get(AllSharedPref.KEY_ACTIVITYNAME);
        rand = actm.get(AllSharedPref.KEY_RAND);
        check = actm.get(AllSharedPref.KEY_CHECK);

        if(activ.equals("CheckoutAdd")||activ.equals("Checkout")){

            mDrawerLayout.closeDrawer(GravityCompat.START);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            navigationView.setVisibility(View.INVISIBLE);
            getSupportActionBar().setTitle("");
            toolbar.setNavigationIcon(R.drawable.back);
        }
        else{
            getSupportActionBar().setTitle("");
            toolbar.setNavigationIcon(R.drawable.ic_menu);
        }


        if(activ.equals("Checkout")||activ.equals("CheckoutAdd")){
//            Intent i = getIntent();
//            email = i.getStringExtra("emailcheckout");
            new accountdetails().execute();
        }
        else if(activ.equals("Address_Book")||activ.equals("Address")||activ.equals("Login_Form")||activ.equals("Base")||activ.equals("Show_delete")||activ.equals("Account")){
            new address().execute();
        }



    }

    class address extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {

            try {
                if (activ.equals("Checkout")||activ.equals("CheckoutAdd")){
                    ur = DATA_URL + URLEncoder.encode(phone,"UTF-8");
                }
                else if(activ.equals("Address_Book")||activ.equals("Address")||activ.equals("Login_Form")||activ.equals("Base")||activ.equals("Show_delete")||activ.equals("Account")){
                    ur = DATA_URL+ URLEncoder.encode(phonenumber, "UTF-8");
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (NullPointerException e){
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
                Log.e("Url",ur);
                Log.e("DATA: ",data);

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
                Toast.makeText(Address_Book.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
            else if(data!=null){
                showJSON(data);
            }

        }


    }

    private void showJSON(String json){
        ParseDeliveryAddress pd = new ParseDeliveryAddress(json);
        pd.parseJSONDelivery();

        int i =ParseDeliveryAddress.getVar();
        if(i!=0) {
            AddressBookList ab = new AddressBookList(this, ParseDeliveryAddress.uid,ParseDeliveryAddress.city, ParseDeliveryAddress.location, ParseDeliveryAddress.company,ParseDeliveryAddress.flat_no,ParseDeliveryAddress.apartment,ParseDeliveryAddress.postcode,ParseDeliveryAddress.other_address);
            listadd.setAdapter(ab);
            if(activ.equals("CheckoutAdd")){
              //  Toast.makeText(Address_Book.this, "Thanks its working bro", Toast.LENGTH_SHORT).show();
                listadd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        all.KAdd("kadd");
                        String uid = ((TextView) view.findViewById(R.id.uid)).getText().toString();
                        String company_name = ((TextView) view.findViewById(R.id.company_add)).getText().toString();
                        String flat_name = ((TextView) view.findViewById(R.id.flatno_add)).getText().toString();
                        String apartment_name = ((TextView) view.findViewById(R.id.apartment_add)).getText().toString();
                        String location_name = ((TextView) view.findViewById(R.id.location_add)).getText().toString();
                        String landmark_name = ((TextView) view.findViewById(R.id.other_add)).getText().toString();
                        String postcode_name = ((TextView) view.findViewById(R.id.postcode_add)).getText().toString();
                        String city_name = ((TextView) view.findViewById(R.id.city_add)).getText().toString();

                       // Toast.makeText(Address_Book.this, uid, Toast.LENGTH_SHORT).show();


                        Intent in = new Intent(Address_Book.this, Checkout_Addn_Phone.class);
                        in.putExtra("company_name",company_name);
                        in.putExtra("flat_name",flat_name);
                        in.putExtra("apartment_name",apartment_name);
                        in.putExtra("location_name",location_name);
                        in.putExtra("landmark_name",landmark_name);
                        in.putExtra("postcode_name",postcode_name);
                        in.putExtra("city_name",city_name);
                        startActivity(in);
                    }
                });

            }
            else if(!activ.equals("CheckoutAdd")){
                listadd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        all.ActName("Show_delete");
                        String uid = ((TextView) view.findViewById(R.id.uid)).getText().toString();
                        String company_name = ((TextView) view.findViewById(R.id.company_add)).getText().toString();
                        String flat_name = ((TextView) view.findViewById(R.id.flatno_add)).getText().toString();
                        String apartment_name = ((TextView) view.findViewById(R.id.apartment_add)).getText().toString();
                        String location_name = ((TextView) view.findViewById(R.id.location_add)).getText().toString();
                        String landmark_name = ((TextView) view.findViewById(R.id.other_add)).getText().toString();
                        String postcode_name = ((TextView) view.findViewById(R.id.postcode_add)).getText().toString();
                        String city_name = ((TextView) view.findViewById(R.id.city_add)).getText().toString();

                        Toast.makeText(Address_Book.this, uid, Toast.LENGTH_SHORT).show();

                        Intent in = new Intent(Address_Book.this, Delivery_Address.class);
                        all.Select("select_del");
                        in.putExtra("uid",uid);
                        in.putExtra("company_name",company_name);
                        in.putExtra("flat_name",flat_name);
                        in.putExtra("apartment_name",apartment_name);
                        in.putExtra("location_name",location_name);
                        in.putExtra("landmark_name",landmark_name);
                        in.putExtra("postcode_name",postcode_name);
                        in.putExtra("city_name",city_name);
                        startActivity(in);
                    }
                });
            }


        }
        else{
            try{

                Intent in = getIntent();
                String d = in.getStringExtra("act");
                if(d=="actdel"){
                    HashMap<String, String> loc = sess.getLocaDetails();
                    if (location != null && city != null )
                    {
                        navigationView.getMenu().findItem(R.id.restaraunts).setVisible(true);
                        String sessioncityname = loc.get(RestrauntLocationManagerPref.KEY_CITY);
                        String sessionlocation = loc.get(RestrauntLocationManagerPref.KEY_LOCATION);
                        Intent intent = new Intent(Address_Book.this, Base.class);
                        intent.putExtra("city", sessioncityname);
                        intent.putExtra("location", sessionlocation);
                        startActivity(intent);
                    }
                    else {
                        navigationView.getMenu().findItem(R.id.restaraunts).setVisible(false);
                        Intent intent = new Intent(Address_Book.this,Address.class);
                        startActivity(intent);
                    }
                }

                if(rand.equals("random")&&select.equals("select_del")&&(activ.equals("Show_delete")||activ.equals("Base")||activ.equals("Address")||activ.equals("Login_Form"))){
                    HashMap<String, String> loc = sess.getLocaDetails();
                    if (location != null && city != null )
                    {
                        navigationView.getMenu().findItem(R.id.restaraunts).setVisible(true);
                        String sessioncityname = loc.get(RestrauntLocationManagerPref.KEY_CITY);
                        String sessionlocation = loc.get(RestrauntLocationManagerPref.KEY_LOCATION);
                        Intent intent = new Intent(Address_Book.this, Base.class);
                        intent.putExtra("city", sessioncityname);
                        intent.putExtra("location", sessionlocation);
                        startActivity(intent);
                    }
                    else {
                        navigationView.getMenu().findItem(R.id.restaraunts).setVisible(false);
                        Intent intent = new Intent(Address_Book.this,Address.class);
                        startActivity(intent);
                    }
                }



                else if((select==null||activ.equals("Base")||activ.equals("Address")||rand.equals("Address"))&&d!="actdel"){
                    Intent intent = new Intent(Address_Book.this,Delivery_Address.class);
                    startActivity(intent);
                    Toast.makeText(Address_Book.this, "Sorry nothing is found.", Toast.LENGTH_SHORT).show();
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            }







        }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HashMap<String, String> loc = sess.getLocaDetails();
        if (activ.equals("Address")||activ.equals("Login_Form")||activ.equals("Account")||activ.equals("Address_Book")||activ.equals("Base")||activ.equals("Show_delete")) {

            if(!check.equals("In_Checkout")){
                if (location != null && city != null )
                {
                    navigationView.getMenu().findItem(R.id.restaraunts).setVisible(true);
                    all.ActName("Address_Book");
                    String sessioncityname = loc.get(RestrauntLocationManagerPref.KEY_CITY);
                    String sessionlocation = loc.get(RestrauntLocationManagerPref.KEY_LOCATION);
                    Intent intent = new Intent(Address_Book.this, Base.class);
                    intent.putExtra("city", sessioncityname);
                    intent.putExtra("location", sessionlocation);
                    startActivity(intent);
                }
                else {
                    navigationView.getMenu().findItem(R.id.restaraunts).setVisible(false);
                    Intent intent = new Intent(Address_Book.this,Address.class);
                    startActivity(intent);
                }

            }
            else if(check.equals("In_Checkout")){
                Intent i3 = new Intent(Address_Book.this, Checkout.class);
                startActivity(i3);
            }


        }
        else if(activ.equals("Checkout")){
            Intent i2 = new Intent(Address_Book.this, Checkout.class);
            startActivity(i2);
        }
        else if(activ.equals("CheckoutAdd")){
            Intent i3 = new Intent(Address_Book.this,Checkout_Addn_Phone.class);
            startActivity(i3);
        }
    }





    class accountdetails extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {

            try {
                ur = DATA_UR+email;
            } catch(NullPointerException e){
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
                data1 = stringBuilder.toString();

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
            if(data1==null){
                Toast.makeText(Address_Book.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
            else if(data1!=null){
                showJSON1(data1);
            }

        }

    }

    private void showJSON1(String response){

        name =  "";
        phone = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(JSON_ARRAY);
            JSONObject collegeData = result.getJSONObject(0);
            name = collegeData.getString(KEY_NAME);
            phone = collegeData.getString(KEY_PHONE);




        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }


        session.createUserLoginSession(email,phone,name);
        session.isUserLoggedIn();
        new address().execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.del_plus, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.add_del:
                checkInternetConenction();
                if(connected){
                    if(activ.equals("CheckoutAdd")){
                        all.ActName("CheckoutAdd");
                        all.KAdd("fu");
                        Intent intent = new Intent(Address_Book.this,Delivery_Address.class);
                        startActivity(intent);
                        return true;
                    }
                    else if(!activ.equals("CheckoutAdd")){
                        all.ActName("Address_Book");
                        Intent intent = new Intent(Address_Book.this,Delivery_Address.class);
                        startActivity(intent);
                        return true;
                    }
                }
                else {
                    Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                }

            case android.R.id.home:
                if(activ.equals("Checkout")||activ.equals("CheckoutAdd")){
                    onBackPressed();
                }
                else{
                    mDrawerLayout.openDrawer(GravityCompat.START);
                    return true;
                }



        }

        return super.onOptionsItemSelected(item);
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
}

