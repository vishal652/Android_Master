package com.example.shubham.navigationdrawer;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;


/**
 * Created by Shubham on 05-07-2016.
 */
public class Address extends Open{

    Button showRest;
    Toolbar toolbar;
    String city1,location1;
    RestrauntLocationManagerPref session;
    int backButtonCount;
    UserSessionManager sess;
    AllSharedPref all;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.address, null, false);
        mDrawerLayout.addView(contentView, 0);
        navigationView.getMenu().getItem(0).setChecked(true);



        session = new RestrauntLocationManagerPref(getApplicationContext());
        sess = new UserSessionManager(getApplicationContext());
        all = new AllSharedPref(getApplicationContext());
        all.ActName("Address");
        all.CheckOnly("Address");
        all.Rand("Address");
        all.KAdd("fu");

        final Spinner spinnercity = (Spinner) findViewById(R.id.cityname);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.city_name, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnercity.setAdapter(adapter);

        spinnercity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city1 = spinnercity.getSelectedItem().toString();
              //  session.createRestSession(city1,location1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final Spinner spinnerlocation = (Spinner) findViewById(R.id.citylocation);
        ArrayAdapter<CharSequence> adapterloc = ArrayAdapter.createFromResource(this,
                R.array.city_location, android.R.layout.simple_spinner_item);
        adapterloc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerlocation.setAdapter(adapterloc);

        spinnerlocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                location1 = spinnerlocation.getSelectedItem().toString();
              //  session.createRestSession(city1,location1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       // session.createRestSession(city1,location1);

        SharedPreferences myPrefs = this.getSharedPreferences("Loc", MODE_WORLD_READABLE);
        String city = myPrefs.getString("city1",null);
        String location = myPrefs.getString("location1",null);

        if (location != null && city != null )
        {
            navigationView.getMenu().findItem(R.id.restaraunts).setVisible(true);
        }
        else {
            navigationView.getMenu().findItem(R.id.restaraunts).setVisible(false);
        }

        if(sess.isUserLoggedIn()){
            navigationView.getMenu().findItem(R.id.address_book).setVisible(true);
        }
        else{
            navigationView.getMenu().findItem(R.id.address_book).setVisible(false);
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar_add);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.getMenu().clear();


        showRest = (Button)findViewById(R.id.btnshow_rest);
        showRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.createRestSession(city1,location1);
                Intent intent = new Intent(Address.this,Base.class);
                intent.putExtra("city",city1);
                intent.putExtra("location",location1);
                startActivity(intent);
            }
        });

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

    @Override
    public void onBackPressed() {

        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }

    }

}

