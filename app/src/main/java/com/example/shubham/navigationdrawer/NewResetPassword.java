package com.example.shubham.navigationdrawer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class NewResetPassword extends AppCompatActivity {

    EditText newpass,retypepass;
    Button reset;
    Toolbar toolbar;
    String emailAddress;
    String passWord, repassWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_reset_password);
        toolbar = (Toolbar) findViewById(R.id.toolbar_resetnewpass);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainpage = new Intent(NewResetPassword.this,Forgot_Password.class);
                startActivity(mainpage);
            }
        });


        newpass = (EditText)findViewById(R.id.newpassword);

        retypepass = (EditText)findViewById(R.id.retypenewpass);
        reset = (Button)findViewById(R.id.reset_pass_new);

        Intent intent = getIntent();
        emailAddress = intent.getStringExtra("EMAIL");


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                passWord = newpass.getText().toString().trim();
                repassWord = retypepass.getText().toString().trim();

                if (TextUtils.isEmpty(passWord)) {

                    newpass.setError("This field is mandatory");
                    return;
                }

                if (TextUtils.isEmpty(repassWord)) {
                    retypepass.setError("This field is mandatory");
                    return;
                }

                if(!passWord.equals(repassWord)){

                  Toast.makeText(NewResetPassword.this, "Entered Password do not match with the new password", Toast.LENGTH_SHORT).show();
                    return;
                }

                repass(emailAddress,passWord);

            }
        });
    }

    private void repass(final String emailAddress, String passWord) {

        class RepassAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(NewResetPassword.this, "Please wait", "Loading...");

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
                            "http://tiredbuzz.16mb.com/resetpassword.php");
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
                    Toast.makeText(NewResetPassword.this, "No INternet Connection!", Toast.LENGTH_SHORT).show();
                }
                else{
                    String s = result.trim();
                    loadingDialog.dismiss();
                    if(s.equalsIgnoreCase("success")){
                        Toast.makeText(NewResetPassword.this, "Your password have been sucessfully updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NewResetPassword.this, LoginFrom.class);
                        startActivity(intent);


                    }else {
                        Toast.makeText(getApplicationContext(), "Cannot connect to server.Please try again!", Toast.LENGTH_LONG).show();
                    }
                }

            }
        }

        RepassAsync la = new RepassAsync();
        la.execute(emailAddress, passWord);
    }
}
