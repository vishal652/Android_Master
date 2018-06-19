package com.example.shubham.navigationdrawer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.Timer;

/**
 * Created by Shubham on 17-07-2016.
 */
public class Account extends Open {

    UserSessionManager session;
    TextView name,email,number;
    Toolbar toolbar;
    ImageView imageViewRound,accountnamechange;
    Button logout;
    private Dialog dialog;
    RestrauntLocationManagerPref sess;
    String emailAddress,ur,data,fullname,phonenumber,sessionemail,city,location,activ;
    public static final String DATA_URL = "http://tiredbuzz.16mb.com/accountdetails.php?emailaddress=";
    public static final String KEY_NAME = "fullName";
    public static final String KEY_PHONE = "phoneNumber";
    public static final String JSON_ARRAY = "result";
    AllSharedPref all;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.account, null, false);
        mDrawerLayout.addView(contentView, 0);
        navigationView.getMenu().getItem(4).setChecked(true);
      //  navigationView.getMenu().setGroupVisible(R.id.g1, true);
        all = new AllSharedPref(getApplicationContext());


        Intent i = getIntent();
        emailAddress = i.getStringExtra("emailaddress");

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

        sess= new RestrauntLocationManagerPref(getApplicationContext());
        session = new UserSessionManager(getApplicationContext());
        logout = (Button)findViewById(R.id.btnlogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(Account.this, "Please wait", "Logging Out...");
                Thread timer = new Thread() {
                    @Override
                    public void run() {
                        super.run();


                        try {
                            sleep(1000);


                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                           session.logoutUser();
                           Intent intent = new Intent(Account.this,LoginFrom.class);
                           startActivity(intent);
                        }
                    }
                };
                timer.start();
            }
        });

        number = (TextView)findViewById(R.id.changnumber);



        if(session.isUserLoggedIn()){
            navigationView.getMenu().findItem(R.id.address_book).setVisible(true);
        }
        else{
            navigationView.getMenu().findItem(R.id.address_book).setVisible(false);
        }

        email =(TextView)findViewById(R.id.changeemail);
        name = (TextView)findViewById(R.id.yourname);


        HashMap<String, String> user = session.getUserDetails();
        sessionemail = user.get(UserSessionManager.KEY_ONLYMAIL);




        toolbar = (Toolbar) findViewById(R.id.toolbar_account);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_menu);


//        imageViewRound=(ImageView)findViewById(R.id.imageView_round);
//        try{
//            Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.apple);
//            imageViewRound.setImageBitmap(icon);
//        }catch(OutOfMemoryError e){
//            e.printStackTrace();
//        }



        accountnamechange = (ImageView)findViewById(R.id.changeaccontname);
        accountnamechange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent change = new Intent(Account.this, AccountNameChange.class);
                startActivity(change);
            }
        });

        new accountdetails().execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HashMap<String, String> loc = sess.getLocaDetails();
        HashMap<String, String> act = all.getActivityName();
        activ = act.get(AllSharedPref.KEY_ACTIVITYNAME);
        if (activ.equals("Login_Form")|| activ.equals("Address")||activ.equals("Account")||activ.equals("Address_book")) {

            if (location != null && city != null )
            {
                navigationView.getMenu().findItem(R.id.restaraunts).setVisible(true);
                all.ActName("Account");
                String sessioncityname = loc.get(RestrauntLocationManagerPref.KEY_CITY);
                String sessionlocation = loc.get(RestrauntLocationManagerPref.KEY_LOCATION);
                Intent intent = new Intent(Account.this, Base.class);
                intent.putExtra("emailaddress",emailAddress);
                intent.putExtra("city", sessioncityname);
                intent.putExtra("location", sessionlocation);
                startActivity(intent);
            }
            else {
                navigationView.getMenu().findItem(R.id.restaraunts).setVisible(false);
                Intent intent = new Intent(Account.this,Address.class);
                startActivity(intent);
            }


        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                // session.createRestSession(city1,location1);
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    class accountdetails extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {

            try {
                ur = DATA_URL+sessionemail;
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
                data = stringBuilder.toString();

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
                Toast.makeText(Account.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
            else if(data!=null){
                showJSON(data);
            }

        }

    }

    private void showJSON(String response){

        fullname =  "";
        phonenumber = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(JSON_ARRAY);
            JSONObject collegeData = result.getJSONObject(0);
            fullname = collegeData.getString(KEY_NAME);
            phonenumber = collegeData.getString(KEY_PHONE);

            email.setText(sessionemail);
            number.setText(phonenumber);
            name.setText(fullname);


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        String ph = number.getText().toString();
        String em = email.getText().toString();
        String fn = name.getText().toString();
       // Toast.makeText(Account.this, ph, Toast.LENGTH_SHORT).show();
        session.createUserLoginSession(em,ph,fn);
        session.isUserLoggedIn();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
