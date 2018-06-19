package com.example.shubham.navigationdrawer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shubham on 26-07-2016.
 */
public class Forgot_Password extends AppCompatActivity {

    TextInputLayout forgotemail,forgotphone;
    Button forgot;
    Toolbar toolbar;

    String emailAddress,phoneNumber;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        forgotemail = (TextInputLayout)findViewById(R.id.forgotemail);
        forgotphone = (TextInputLayout)findViewById(R.id.forgotphone);
        forgot =  (Button)findViewById(R.id.reset_pass);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               forgotpass();
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar_forgotpass);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainpage = new Intent(Forgot_Password.this,LoginFrom.class);
                startActivity(mainpage);
            }
        });
    }

    public void forgotpass(){
        emailAddress = forgotemail.getEditText().getText().toString().trim();
        phoneNumber = forgotphone.getEditText().getText().toString().trim();

        if(TextUtils.isEmpty(emailAddress)){
            forgotemail.setError("This field is mandatory!");
        }
        if(TextUtils.isEmpty(phoneNumber)){
            forgotphone.setError("This field is mandatory!");
        }
        forgotemail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 1) {
                    forgotemail.setErrorEnabled(true);
                    forgotemail.setError("This field is mandatory!");

                }

                if (s.length() > 0) {
                    forgotemail.setError(null);
                    forgotemail.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        forgotphone.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 1) {
                    forgotphone.setErrorEnabled(true);
                    forgotphone.setError("This field is mandatory!");
                }

                if (s.length() > 0) {
                    forgotphone.setError(null);
                    forgotphone.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if(!TextUtils.isEmpty(emailAddress) && !TextUtils.isEmpty(phoneNumber)){
            forgotinto(emailAddress,phoneNumber);
        }

    }

    private void forgotinto(final String emailAddress, final String phoneNumber) {

        class ForgotAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(Forgot_Password.this, "Please wait", "Loading...");

            }

            @Override
            protected String doInBackground(String... params) {
                String emailAddress = params[0];
                String phoneNumber = params[1];
                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("emailaddress", emailAddress));
                nameValuePairs.add(new BasicNameValuePair("phonenumber", phoneNumber));
                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://tiredbuzz.16mb.com/forgotpass.php");
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
                    Toast.makeText(Forgot_Password.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                }
                else{
                    String s = result.trim();
                    loadingDialog.dismiss();
                    if(s.equalsIgnoreCase("success")){
                        Intent newpass = new Intent(Forgot_Password.this,NewResetPassword.class);
                        newpass.putExtra("EMAIL", emailAddress.toString());
                        startActivity(newpass);


                    }else {
                        Toast.makeText(getApplicationContext(), "Invalid Email or phone", Toast.LENGTH_LONG).show();
                    }
                }

            }
        }

        ForgotAsync la = new ForgotAsync();
        la.execute(emailAddress, phoneNumber);
    }
}
