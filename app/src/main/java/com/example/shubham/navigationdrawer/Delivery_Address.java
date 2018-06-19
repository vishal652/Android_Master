package com.example.shubham.navigationdrawer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Delivery_Address extends AppCompatActivity implements View.OnClickListener {

    AllSharedPref all;
    RestrauntLocationManagerPref ses;
    UserSessionManager session;
    TextInputLayout city,location,del_comp,del_flat,del_apartment,del_postal,del_landmark;
    String city1,location1,citymain,locmain,company,flatno,apartment,postcode,landmark,phone;
    EditText del_city,del_loc;
    Button save_add,delete_add;
    String city_name,location_name,company_name,flat_name,apartment_name,landmark_name,postcode_name,select;
    public static final String DATA_URL = "http://tiredbuzz.16mb.com/address_book/address_delete.php?uid=";
    String data,ur,uid,activ,rand,check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery__address);

        all = new AllSharedPref(getApplicationContext());
        ses = new RestrauntLocationManagerPref(getApplicationContext());
        session = new UserSessionManager(getApplicationContext());

        HashMap<String, String> act = session.getUserDetails();
        phone = act.get(UserSessionManager.KEY_PHONE);

        HashMap<String, String> act_n = all.getActivityName();
        activ = act_n.get(AllSharedPref.KEY_ACTIVITYNAME);
        rand = act_n.get(AllSharedPref.KEY_RAND);

        HashMap<String,String> sel = all.getActivityName();
        select = sel.get(AllSharedPref.KEY_SELECT);




        save_add = (Button)findViewById(R.id.btn_save_add);
        delete_add = (Button)findViewById(R.id.btn_del_add);
        city = (TextInputLayout)findViewById(R.id.del_city);
        location = (TextInputLayout)findViewById(R.id.del_loc);
        del_comp = (TextInputLayout)findViewById(R.id.del_company);
        del_flat = (TextInputLayout)findViewById(R.id.del_flatno);
        del_apartment = (TextInputLayout)findViewById(R.id.del_apartment);
        del_postal = (TextInputLayout)findViewById(R.id.del_postal);
        del_landmark = (TextInputLayout)findViewById(R.id.del_others);

        del_city = (EditText)findViewById(R.id.city_del);
        del_loc = (EditText)findViewById(R.id.loc_del);
        Intent in = getIntent();
        uid = in.getStringExtra("uid");

        if(select!=null && activ!=null){
            if(select.equals("select_del")&&activ.equals("Show_delete")){
                delete_add.setVisibility(View.VISIBLE);
                delete_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        add_del();
                    }
                });

                city_name = in.getStringExtra("city_name");
                location_name = in.getStringExtra("location_name");
                company_name = in.getStringExtra("company_name");
                flat_name = in.getStringExtra("flat_name");
                apartment_name = in.getStringExtra("apartment_name");
                landmark_name = in.getStringExtra("landmark_name");
                postcode_name = in.getStringExtra("postcode_name");

                city.getEditText().setText(city_name);
                location.getEditText().setText(location_name);
                del_comp.getEditText().setText(company_name);
                del_flat.getEditText().setText(flat_name);
                del_apartment.getEditText().setText(apartment_name);
                del_postal.getEditText().setText(postcode_name);
                del_landmark.getEditText().setText(landmark_name);
            }

        }

        if(activ.equals("Address_Book")||select==null||activ.equals("Base")||activ.equals("Address")||activ.equals("CheckoutAdd")||activ.equals("Login_Form")){
            delete_add.setVisibility(View.INVISIBLE);

        }


        HashMap<String, String> loc = ses.getLocaDetails();
        city1= loc.get(RestrauntLocationManagerPref.KEY_CITY);
        location1= loc.get(RestrauntLocationManagerPref.KEY_LOCATION);

        if(city1==null && location1==null){

        }
        else
        {
            del_loc.setClickable(false);
            del_loc.setFocusable(false);
            del_city.setFocusable(false);
            del_city.setClickable(false);
            del_city.setText(city1);
            del_loc.setText(location1);
        }

        save_add.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
            if(select!=null){
                if(select.equals("select_del")&&activ.equals("Show_delete")){
                    if(delete_add.getVisibility()==View.VISIBLE){
                        savdel();
                    }
                }
            }

            if(activ.equals("Address_Book")||select==null||activ.equals("Base")||activ.equals("Address")||activ.equals("CheckoutAdd")||activ.equals("Login_Form")){
                if(delete_add.getVisibility()==View.INVISIBLE){
                    savdel();
                }
        }



    }

    public void add_del(){
         new deleteadd().execute();
    }


    class deleteadd extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {

            try {
                ur = DATA_URL+ URLEncoder.encode(uid, "UTF-8");

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
                return data;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String result) {

            String jsonStr = result;
            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);
                    String query_result = jsonObj.getString("query_result");
                    if (query_result.equals("SUCCESS")) {
                        all.Rand("random");
                        Intent intent = new Intent(Delivery_Address.this, Address_Book.class);
                        startActivity(intent);


                        Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                    } else if (query_result.equals("FAILURE")) {
                        Toast.makeText(getApplicationContext(), "Sorry some error occured", Toast.LENGTH_SHORT).show();
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

    public void savdel(){
        citymain = city.getEditText().getText().toString().trim();
        locmain = location.getEditText().getText().toString().trim();
        company = del_comp.getEditText().getText().toString().trim();
        flatno = del_flat.getEditText().getText().toString().trim();
        apartment = del_apartment.getEditText().getText().toString().trim();
        postcode = del_postal.getEditText().getText().toString().trim();
        landmark = del_landmark.getEditText().getText().toString().trim();


        if(TextUtils.isEmpty(citymain)){
            city.setError("This field is mandatory!");
        }
        if(TextUtils.isEmpty(locmain)){
            location.setError("This field is mandatory!");
        }
        if(TextUtils.isEmpty(company)){
            del_comp.setError("This field is mandatory");
        }
        if(TextUtils.isEmpty(flatno)){
            del_flat.setError("This field is mandatory");
        }
        if(TextUtils.isEmpty(apartment)){
            del_apartment.setError("This field is mandatory");
        }
        if(TextUtils.isEmpty(postcode)){
            del_postal.setError("This field is mandatory");
        }
        if(TextUtils.isEmpty(landmark)){
            del_landmark.setError("This field is mandatory");
        }


        city.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 1) {
                    city.setErrorEnabled(true);
                    city.setError("This field is mandatory!");

                }

                if (s.length() > 0) {
                    city.setError(null);
                    city.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        location.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 1) {
                    location.setErrorEnabled(true);
                    location.setError("This field is mandatory!");
                }

                if (s.length() > 0) {
                    location.setError(null);
                    location.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        del_comp.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 1) {
                    del_comp.setErrorEnabled(true);
                    del_comp.setError("This field is mandatory!");
                }

                if (s.length() > 0) {
                    del_comp.setError(null);
                    del_comp.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        del_flat.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 1) {
                    del_flat.setErrorEnabled(true);
                    del_flat.setError("This field is mandatory!");
                }

                if (s.length() > 0) {
                    del_flat.setError(null);
                    del_flat.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        del_apartment.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 1) {
                    del_apartment.setErrorEnabled(true);
                    del_apartment.setError("This field is mandatory!");
                }

                if (s.length() > 0) {
                    del_apartment.setError(null);
                    del_apartment.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        del_postal.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 1) {
                    del_postal.setErrorEnabled(true);
                    del_postal.setError("This field is mandatory!");
                }

                if (s.length() > 0) {
                    del_postal.setError(null);
                    del_postal.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        del_landmark.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 1) {
                    del_landmark.setErrorEnabled(true);
                    location.setError("This field is mandatory!");
                }

                if (s.length() > 0) {
                    del_landmark.setError(null);
                    del_landmark.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if(!TextUtils.isEmpty(citymain) && !TextUtils.isEmpty(locmain) && !TextUtils.isEmpty(company) && !TextUtils.isEmpty(flatno) && !TextUtils.isEmpty(apartment) && !TextUtils.isEmpty(postcode) && !TextUtils.isEmpty(landmark) ){
            if(select!=null){
                if(select.equals("select_del")&&activ.equals("Show_delete")){

                    if(delete_add.getVisibility()==View.VISIBLE){
                        up_add(uid,citymain,locmain,company,flatno,apartment,landmark,postcode);
                        // Toast.makeText(Delivery_Address.this, "Wait for it", Toast.LENGTH_SHORT).show();
                    }
                }
            }
                if(activ.equals("Address_Book")||select==null||activ.equals("Base")||activ.equals("Address")||activ.equals("CheckoutAdd")||activ.equals("Login_Form")){
                    if(delete_add.getVisibility()==View.INVISIBLE){
                        new saveadd().execute(phone,citymain,locmain,company,flatno,apartment,postcode,landmark);
                    }


            }



        }

    }


    public class saveadd extends AsyncTask<String, Void, String> {

        private Dialog loadingDialog;

        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog = ProgressDialog.show(Delivery_Address.this, "Please wait", "Loading...");


        }

        @Override
        protected String doInBackground(String... arg0) {
            String phoneNumber = arg0[0];
            String city = arg0[1];
            String loc = arg0[2];
            String company = arg0[3];
            String flat = arg0[4];
            String apartment = arg0[5];
            String postcode = arg0[6];
            String landmark = arg0[7];

            String link;
            String data;
            BufferedReader bufferedReader;
            String result;

            try {
                data = "?city=" + URLEncoder.encode(city, "UTF-8");
                data += "&location=" + URLEncoder.encode(loc, "UTF-8");
                data += "&phonenumber=" + URLEncoder.encode(phoneNumber, "UTF-8");
                data += "&company=" + URLEncoder.encode(company, "UTF-8");
                data += "&flat_no=" + URLEncoder.encode(flat, "UTF-8");
                data += "&apartment=" + URLEncoder.encode(apartment, "UTF-8");
                data += "&postcode=" + URLEncoder.encode(postcode, "UTF-8");
                data += "&other_address=" + URLEncoder.encode(landmark, "UTF-8");

                link = "http://tiredbuzz.16mb.com/address_book/address_add.php" + data;
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
                                Intent intent = new Intent(Delivery_Address.this, Address_Book.class);
                                startActivity(intent);


                         Toast.makeText(getApplicationContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
                    } else if (query_result.equals("FAILURE")) {
                        Toast.makeText(getApplicationContext(), "Sorry some error occured", Toast.LENGTH_SHORT).show();
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






    @Override
    public void onBackPressed() {
        HashMap<String, String> act = all.getActivityName();
        String activ = act.get(AllSharedPref.KEY_ACTIVITYNAME);
        check  = act.get(AllSharedPref.KEY_CHECK);
        String d = "actdel";

        if(activ.equals("Address_Book")||activ.equals("Login_Form")||activ.equals("Base")||activ.equals("Show_delete")||activ.equals("Address")||activ.equals("Checkout")||activ.equals("Login_Form")){
            all.Rand("random");
            Intent intent = new Intent(Delivery_Address.this,Address_Book.class);

            intent.putExtra("act",d);
            startActivity(intent);
        }
        super.onBackPressed();
    }



    private void up_add(final String uid,String city,String location,String company,String flat,String apartment,String landmark,String postcode) {

        class UpdateAdd extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(Delivery_Address.this, "Please wait", "Updating Address...");

            }

            @Override
            protected String doInBackground(String... params) {
                String uid = params[0];
                String city = params[1];
                String location = params[2];
                String company = params[3];
                String flat = params[4];
                String apartment = params[5];
                String landmark = params[6];
                String postcode = params[7];

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("uid", uid));
                nameValuePairs.add(new BasicNameValuePair("city_name", city));
                nameValuePairs.add(new BasicNameValuePair("location_name", location));
                nameValuePairs.add(new BasicNameValuePair("company_name", company));
                nameValuePairs.add(new BasicNameValuePair("flat_name", flat));
                nameValuePairs.add(new BasicNameValuePair("apartment_name", apartment));
                nameValuePairs.add(new BasicNameValuePair("landmark_name", landmark));
                nameValuePairs.add(new BasicNameValuePair("postcode_name", postcode));
                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://tiredbuzz.16mb.com/address_book/address_update.php");
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
                    Toast.makeText(Delivery_Address.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                }
                else{
                    String s = result.trim();
                    loadingDialog.dismiss();
                    if(s.equalsIgnoreCase("success")){
                        Toast.makeText(Delivery_Address.this, "Your Address have been sucessfully updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Delivery_Address.this, Address_Book.class);
                        startActivity(intent);


                    }else {
                        Toast.makeText(getApplicationContext(), "Cannot connect to server.Please try again!", Toast.LENGTH_LONG).show();
                    }
                }

            }
        }

        UpdateAdd la = new UpdateAdd();
        la.execute(uid,city,location,company,flat,apartment,landmark,postcode);
    }
}
