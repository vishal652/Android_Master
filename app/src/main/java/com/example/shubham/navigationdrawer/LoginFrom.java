package com.example.shubham.navigationdrawer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LoginFrom extends Open implements View.OnClickListener{

    public static final String DATA_URL = "http://tiredbuzz.16mb.com/accountdetails.php?emailaddress=";
    public static final String KEY_NAME = "fullName";
    public static final String KEY_PHONE = "phoneNumber";
    public static final String JSON_ARRAY = "result";
    private static final String PREFER_NAME = "Reg";
    UserSessionManager session;
    private SharedPreferences sharedPreferences;
    private EditText editTextUserName;
    private EditText editTextPassword;
    public static final String USER_NAME = "USERNAME";
    String emailAddress,fullname,phonenumber;
    String passWord,s,activ,city,location;
    RestrauntLocationManagerPref sess;
    Toolbar toolbar;
    private Button buttonLogin, buttonsignup, buttonforgot;
    AllSharedPref all;
    TextView tooltext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.login_form, null, false);
        mDrawerLayout.addView(contentView, 0);
        navigationView.getMenu().getItem(4).setChecked(true);
        // navigationView.getMenu().setGroupVisible(R.id.g1, true);

        session = new UserSessionManager(getApplicationContext());
        all = new AllSharedPref(getApplicationContext());
        sess = new RestrauntLocationManagerPref(getApplicationContext());
        tooltext = (TextView)findViewById(R.id.text_toolbar);

        HashMap<String, String> act = all.getActivityName();
        activ = act.get(AllSharedPref.KEY_ACTIVITYNAME);

        LinearLayout lnl = (LinearLayout)findViewById(R.id.lnl);
        FrameLayout fm = (FrameLayout)findViewById(R.id.frame_login);
        TextView tot = (TextView)findViewById(R.id.checkout_total_login);

        toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);
        if(activ.equals("Checkout")){
            fm.setVisibility(View.VISIBLE);
            HashMap<String, String> ac = all.getActivityName();
            String to = ac.get(AllSharedPref.KEY_GTOTAL);
            tot.setVisibility(View.VISIBLE);
            tot.setText(to);
            mDrawerLayout.closeDrawer(GravityCompat.START);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            lnl.setVisibility(View.VISIBLE);
            navigationView.setVisibility(View.INVISIBLE);
            getSupportActionBar().setTitle("");
            toolbar.setNavigationIcon(R.drawable.back);
            tooltext.setText("Checkout");
        }
        else{

            getSupportActionBar().setTitle("");
            toolbar.setNavigationIcon(R.drawable.ic_menu);
        }



        buttonforgot =  (Button)findViewById(R.id.btnLinkToForgotPassword);
        buttonforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent fpass= new Intent(LoginFrom.this,Forgot_Password.class);

                startActivity(fpass);
            }
        });



        sharedPreferences = getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);


        editTextUserName = (EditText) findViewById(R.id.loginemail);
        editTextPassword = (EditText) findViewById(R.id.loginpassword);
        buttonsignup = (Button)findViewById(R.id.btnLinkToRegisterScreen);
        buttonLogin = (Button) findViewById(R.id.btnLogin);

        buttonsignup.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);


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

    }


    private void login(){
        emailAddress = editTextUserName.getText().toString().trim();
        passWord = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(emailAddress)) {
            editTextUserName.setError("This field is Mandatory");
           // Toast.makeText(getApplicationContext(), "Email cannot be set to empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(passWord)) {
            editTextPassword.setError("This field is Mandatory");
           // Toast.makeText(getApplicationContext(), "Password cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(emailAddress) && TextUtils.isEmpty(passWord)){
            editTextUserName.setError("This field is Mandatory");
        }
        loginto(emailAddress,passWord);


    }






    @Override
    public void onClick(View v) {
        if(v == buttonLogin){
            login();

        }
        else{
            Intent signup = new Intent(LoginFrom.this,RegisterForm.class);
            if(activ.equals("Checkout")){
                all.ActName("Checkout");
            }else{
                all.ActName("Login_Form");
            }

            startActivity(signup);
        }
    }

    private void loginto(final String emailAddress, String passWord) {

        class LoginAsync extends AsyncTask<String, Void, String>{

        private Dialog loadingDialog;

        @Override
        protected void onPreExecute() {
        super.onPreExecute();
        loadingDialog = ProgressDialog.show(LoginFrom.this, "Please wait", "Loading...");

        }

            @Override
            protected String doInBackground(String... params) {
                String emailAddress = params[0];
                String passWord = params[1];
                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("emailaddress", emailAddress));
                nameValuePairs.add(new BasicNameValuePair("password", passWord));
                String result = null;

                try {
                    
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://tiredbuzz.16mb.com/login.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();
                    is = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){

                    if(result==null){
                        loadingDialog.dismiss();
                        Toast.makeText(LoginFrom.this,"No Internet Connection!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        loadingDialog.dismiss();
                        s = result.trim();
                        if(s.equalsIgnoreCase("success")) {
                            session.isUserLoggedIn();
                            session.OnlyMail(emailAddress);
                            getData();
                           // session.createUserLoginSession(emailAddress,phonenumber,fullname);
                            navigationView.getMenu().findItem(R.id.address_book).setVisible(true);

                          //  HashMap<String, String> loc = sess.getLocaDetails();
                            HashMap<String, String>  act = all.getActivityName();

                             activ = act.get(AllSharedPref.KEY_ACTIVITYNAME);
                            if (activ.equals("Address")||activ.equals("Login_Form")) {
                                all.ActName("Login_Form");
//                                String sessioncityname = loc.get(RestrauntLocationManagerPref.KEY_CITY);
//                                String sessionlocation = loc.get(RestrauntLocationManagerPref.KEY_LOCATION);
                                navigationView.getMenu().findItem(R.id.address_book).setVisible(true);
                                Intent intent = new Intent(LoginFrom.this, Account.class);
                                intent.putExtra("emailaddress",emailAddress);
//                                intent.putExtra("city", sessioncityname);
//                                intent.putExtra("location", sessionlocation);
                                startActivity(intent);

                            }
                            else if(activ.equals("Checkout")){
                                all.ActName("Checkout");
                                Intent i2 = new Intent(LoginFrom.this, Checkout_Addn_Phone.class);
                                session.OnlyMail(emailAddress);
                                i2.putExtra("emailcheckout",emailAddress);
                                startActivity(i2);
                            }


                        }
                        else {
                            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate);
                            buttonforgot.startAnimation(anim);
                            buttonforgot.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
                        }
                    }


                }
            }

        LoginAsync la = new LoginAsync();
        la.execute(emailAddress, passWord);
        }

    public void getData() {


        String url = DATA_URL+ emailAddress ;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginFrom.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
       // Toast.makeText(LoginFrom.this, fullname, Toast.LENGTH_SHORT).show();
        session.createUserLoginSession(emailAddress,phonenumber,fullname);
        session.isUserLoggedIn();

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
                if(activ.equals("Checkout")){
                    onBackPressed();
                }
                else{
                    mDrawerLayout.openDrawer(GravityCompat.START);
                    return true;
                }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HashMap<String, String> loc = sess.getLocaDetails();
        HashMap<String, String> act = all.getActivityName();
        activ = act.get(AllSharedPref.KEY_ACTIVITYNAME);
        if (activ.equals("Address")||activ.equals("Login_Form")) {

            if (location != null && city != null )
            {
                navigationView.getMenu().findItem(R.id.restaraunts).setVisible(true);
                all.ActName("Address");
                String sessioncityname = loc.get(RestrauntLocationManagerPref.KEY_CITY);
                String sessionlocation = loc.get(RestrauntLocationManagerPref.KEY_LOCATION);
                Intent intent = new Intent(LoginFrom.this, Base.class);
                intent.putExtra("emailaddress",emailAddress);
                intent.putExtra("city", sessioncityname);
                intent.putExtra("location", sessionlocation);
                startActivity(intent);
            }
            else {
                navigationView.getMenu().findItem(R.id.restaraunts).setVisible(false);
                Intent intent = new Intent(LoginFrom.this,Address.class);
                startActivity(intent);
            }


        }
        else if(activ.equals("Checkout")){
            Intent i2 = new Intent(LoginFrom.this, Checkout.class);
            startActivity(i2);
        }

    }

}