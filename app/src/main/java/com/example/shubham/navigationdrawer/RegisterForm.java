package com.example.shubham.navigationdrawer;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Shubham on 23-06-2016.
 */
public class RegisterForm extends Open {

    EditText nme,phn,eml,pass;
    Button btnregister;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    UserSessionManager session;
    String fullName,phoneNumber,emailAddress;
    RestrauntLocationManagerPref sess;
    AllSharedPref all;
    String location,city;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.register_form);

            session = new UserSessionManager(getApplicationContext());
            all = new AllSharedPref(getApplicationContext());
            sharedPreferences = getSharedPreferences("Reg", MODE_PRIVATE);

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


            // get editor to edit in file
            editor = sharedPreferences.edit();
            nme = (EditText) findViewById(R.id.name);
            phn = (EditText) findViewById(R.id.phone);
            eml = (EditText) findViewById(R.id.email);
            pass = (EditText) findViewById(R.id.password);
            btnregister = (Button) findViewById(R.id.btnRegister);

            btnregister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                     fullName = nme.getText().toString();
                     String passWord = pass .getText().toString();
                     phoneNumber = phn.getText().toString();
                     emailAddress = eml.getText().toString();

                    if (TextUtils.isEmpty(fullName)) {
                        Toast.makeText(RegisterForm.this, "Please enter your name!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(!validateMobile(phoneNumber)){
                        Toast.makeText(RegisterForm.this, "Invalid number", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(!isValidEmail(emailAddress)){
                        Toast.makeText(RegisterForm.this, "Wrong Email Address!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(passWord)) {
                        Toast.makeText(RegisterForm.this, "Enter Password", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (passWord.length() < 6) {
                        Toast.makeText(RegisterForm.this, "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    editor.putString(UserSessionManager.KEY_NAME, fullName);
                    editor.putString(UserSessionManager.KEY_EMAIL,emailAddress);
                    editor.putString(UserSessionManager.KEY_PASSWORD,passWord);
                    editor.putString(UserSessionManager.KEY_PHONE,phoneNumber);
                    editor.commit();  // commit the values


               // Toast.makeText(RegisterForm.this, "Signing up...", Toast.LENGTH_SHORT).show();
                    new Signup().execute(fullName,  passWord, phoneNumber, emailAddress);
                }
            });



            Button btl = (Button) findViewById(R.id.btnLinkToLoginScreen);
            btl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i1 = new Intent(RegisterForm.this, LoginFrom.class);
                    startActivity(i1);
                }
            });
        }

    public static boolean validateMobile( String mobileee ) {
        String expression = "[0-9]{10}";
        CharSequence inputStr2 = mobileee;
        Pattern pattern2 = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher2 = pattern2.matcher(inputStr2);
        if (matcher2.matches() && (mobileee.length() == 10)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValidEmail (String validmail)
    {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = validmail;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }


    public class Signup extends AsyncTask<String, Void, String> {

        private Dialog loadingDialog;

        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog = ProgressDialog.show(RegisterForm.this, "Please wait", "Loading...");


        }

        @Override
        protected String doInBackground(String... arg0) {
            String fullName = arg0[0];
            String passWord = arg0[1];
            String phoneNumber = arg0[2];
            String emailAddress = arg0[3];

            String link;
            String data;
            BufferedReader bufferedReader;
            String result;

            try {
                data = "?fullname=" + URLEncoder.encode(fullName, "UTF-8");
                data += "&password=" + URLEncoder.encode(passWord, "UTF-8");
                data += "&phonenumber=" + URLEncoder.encode(phoneNumber, "UTF-8");
                data += "&emailaddress=" + URLEncoder.encode(emailAddress, "UTF-8");

                link = "http://tiredbuzz.16mb.com/signup.php" + data;
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
            loadingDialog.dismiss();

            String jsonStr = result;
            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);
                    String query_result = jsonObj.getString("query_result");
                    if (query_result.equals("SUCCESS")) {
                        sess= new RestrauntLocationManagerPref(getApplicationContext());
                        HashMap<String, String> loc = sess.getLocaDetails();
                        HashMap<String, String>  act = all.getActivityName();

                        String activ = act.get(AllSharedPref.KEY_ACTIVITYNAME);
                        if(activ.equals("Login_Form")){
                            if (location != null && city != null )
                            {
                                all.ActName("Login_Form");
                                navigationView.getMenu().findItem(R.id.restaraunts).setVisible(true);
                                String sessioncityname = loc.get(RestrauntLocationManagerPref.KEY_CITY);
                                String sessionlocation = loc.get(RestrauntLocationManagerPref.KEY_LOCATION);
                                session.createUserLoginSession(emailAddress,phoneNumber,fullName);
                                session.OnlyMail(emailAddress);
                                session.isUserLoggedIn();
                                Intent intent = new Intent(getApplicationContext(), Base.class);
                                intent.putExtra("city",sessioncityname);
                                intent.putExtra("location",sessionlocation);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getApplicationContext().startActivity(intent);
                            }
                            else {
                                navigationView.getMenu().findItem(R.id.restaraunts).setVisible(false);
                                session.createUserLoginSession(emailAddress,phoneNumber,fullName);
                                session.OnlyMail(emailAddress);
                                session.isUserLoggedIn();
                                Intent intent = new Intent(RegisterForm.this,Address.class);
                                startActivity(intent);
                            }

                        }
                        else if(activ.equals("Checkout")){
                            all.ActName("Checkout");
                            Intent i2 = new Intent(RegisterForm.this, Address_Book.class);
                            i2.putExtra("emailcheckout",emailAddress);
                            startActivity(i2);
                        }

                       // Toast.makeText(getApplicationContext(), "Signup successfull. Welcome.", Toast.LENGTH_SHORT).show();
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
