package com.example.shubham.navigationdrawer;


import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shubham.navigationdrawer.CallFragment;
import com.example.shubham.navigationdrawer.GroupFragment;
import com.example.shubham.navigationdrawer.PeopleFragment;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class Base extends Open {


    private Toolbar toolbar;
    UserSessionManager session;
    private ListView listView;
    public static final String DATA_URL = "http://tiredbuzz.16mb.com/restraunts/restraunt.php?city=";
    public static final String URL_LEFT = "&location=";
    String ur,city1,location1,data;
    TextView  count;
    String restname,cityloc,type,image;
    AllSharedPref sess;
    RestrauntLocationManagerPref ses;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.base, null, false);
        mDrawerLayout.addView(contentView, 0);
        navigationView.getMenu().findItem(R.id.restaraunts).setVisible(true);
        navigationView.getMenu().getItem(1).setChecked(true);

        count = (TextView) findViewById(R.id.count);
        session = new UserSessionManager(getApplicationContext());
        ses= new RestrauntLocationManagerPref(getApplicationContext());
        sess = new AllSharedPref(getApplicationContext());
        sess.ActName("Address");
        sess.CheckOnly("Address");
        sess.Rand("Address");
        sess.KAdd("fu");


        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.openOptionsMenu();


        listView = (ListView) findViewById(R.id.listView);
        Intent intent = getIntent();
        city1 = intent.getStringExtra("city");
        location1 = intent.getStringExtra("location");
        actionBar.setTitle(city1);



        HashMap<String, String> loc = ses.getLocaDetails();
        city1= loc.get(RestrauntLocationManagerPref.KEY_CITY);
        location1= loc.get(RestrauntLocationManagerPref.KEY_LOCATION);
        actionBar.setTitle(city1);

        new Product().execute();

        if(session.isUserLoggedIn()){
            //  navigationView.getMenu().setGroupVisible(R.id.g8, true);
            navigationView.getMenu().findItem(R.id.address_book).setVisible(true);

        }
        else{
            Toast.makeText(Base.this, "Not working", Toast.LENGTH_SHORT).show();
            navigationView.getMenu().findItem(R.id.address_book).setVisible(false);
          //  navigationView.getMenu().setGroupVisible(R.id.g8, false);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
//       searchView.setSearchableInfo(
//               searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.search:

                return true;
            case R.id.settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    class Product extends AsyncTask<String,String,String> {
        private Dialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(Base.this, "Please wait", "Loading...");
            dialog.setCancelable(true);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                ur = DATA_URL+ URLEncoder.encode(city1, "UTF-8")+URL_LEFT+  URLEncoder.encode(location1, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
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
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            if(data==null){
                LinearLayout ln = (LinearLayout)findViewById(R.id.noin);
                ln.setVisibility(View.VISIBLE);
                Toast.makeText(Base.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
            else{
                listView.setVisibility(View.VISIBLE);
                View v = (View)findViewById(R.id.v);
                v.setVisibility(View.VISIBLE);
                showJSON(data);
            }



        }
    }


    private void showJSON(String json){

        ParseJSONRest pj = new ParseJSONRest(json);
        pj.parseJSONRest();

        sess = new AllSharedPref(getApplicationContext());
        int i = ParseJSONRest.getVar();
        if(i==0){

            count.setText("Sorry! No Restaurant delivers to you here.");
        }
        else if(i==1) {
            count.setText(String.valueOf(i)+ " restaurant deliver to you");
            RestList cl = new RestList(this, ParseJSONRest.restname, ParseJSONRest.location, ParseJSONRest.image, ParseJSONRest.type, ParseJSONRest.deliverytime);
            listView.setAdapter(cl);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    sess.ActName("Base");
                    TextView textView = (TextView) view.findViewById(R.id.idrestname);
                    restname = listView.getItemAtPosition(position).toString();
                    cityloc = ((TextView) view.findViewById(R.id.restcitylocation)).getText().toString();
                    type = ((TextView) view.findViewById(R.id.type)).getText().toString();
                    image = ((ImageView) view.findViewById(R.id.restimage)).getImageMatrix().toString();

                    sess.OpenDeliveryMenuSession(restname,cityloc,type);

                    Intent intent = new Intent(Base.this, DeliveryMenu.class);
                    intent.putExtra("restname", restname);
                    intent.putExtra("loc", cityloc);
                    intent.putExtra("type", type);
                    startActivity(intent);


                }
            });
        }
        else {
            count.setText(String.valueOf(i)+ " restaurants deliver to you");
            RestList cl = new RestList(this, ParseJSONRest.restname, ParseJSONRest.location, ParseJSONRest.image, ParseJSONRest.type, ParseJSONRest.deliverytime);
            listView.setAdapter(cl);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    sess.ActName("Base");
                    TextView textView = (TextView) view.findViewById(R.id.idrestname);
                    restname = listView.getItemAtPosition(position).toString();
                    cityloc = ((TextView) view.findViewById(R.id.restcitylocation)).getText().toString();
                    type = ((TextView) view.findViewById(R.id.type)).getText().toString();
                    image = ((ImageView) view.findViewById(R.id.restimage)).getImageMatrix().toString();

                    sess.OpenDeliveryMenuSession(restname,cityloc,type);

                    Intent intent = new Intent(Base.this, DeliveryMenu.class);
                    intent.putExtra("restname", restname);
                    intent.putExtra("loc", cityloc);
                    intent.putExtra("type", type);
                    startActivity(intent);


                }
            });
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Base.this,Address.class);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Product().execute();
    }


}

