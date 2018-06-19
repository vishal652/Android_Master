package com.example.shubham.navigationdrawer;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.HashMap;

/**
 * Created by Shubham on 05-07-2016.
 */
public class Invite_Friends extends Open {
    private Toolbar toolbar;
    ImageView ivwhats ,ivgmail, ivmsg , ivmore;
    Button invite;
    RestrauntLocationManagerPref session;
    UserSessionManager sess;
    String city,location,activ;
    AllSharedPref all;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.invite_friends, null, false);
        mDrawerLayout.addView(contentView, 0);
        navigationView.getMenu().getItem(7).setChecked(true);
      //  navigationView.getMenu().setGroupVisible(R.id.g1, true);

        session= new RestrauntLocationManagerPref(getApplicationContext());
        sess = new UserSessionManager(getApplicationContext());
        all = new AllSharedPref(getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.toolbar_invite);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_menu);

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

        if(sess.isUserLoggedIn()){
            navigationView.getMenu().findItem(R.id.address_book).setVisible(true);
        }
        else{
            navigationView.getMenu().findItem(R.id.address_book).setVisible(false);
        }

        invite = (Button) findViewById(R.id.btnmore);
        ivwhats = (ImageView) findViewById(R.id.whatsapp);
        ivgmail = (ImageView)findViewById(R.id.gmail);
        ivmsg = (ImageView)findViewById(R.id.message);
        ivmore = (ImageView)findViewById(R.id.more);

        ivmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sedIntent = new Intent();
                sedIntent.setAction(Intent.ACTION_SEND);
                sedIntent.putExtra(Intent.EXTRA_TEXT, "www.foodbuck.com");
                sedIntent.setType("text/plain");
                startActivity(Intent.createChooser(sedIntent, getResources().getText(R.string.send_to)));
            }
        });

        ivmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address","");
                smsIntent.putExtra("sms_body","www.foodbuck.com");
                startActivity(smsIntent);
            }
        });

        ivgmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto: "));
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "FoodBck Link");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hey there! Check this app for free and get Rs. 50 on installation");
                startActivity(Intent.createChooser(emailIntent, "Invite via..."));

            }
        });

        ivwhats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ComponentName name = new ComponentName("com.whatsapp", "com.whatsapp.ContactPicker");
                Intent oShareIntent = new Intent();
                oShareIntent.setComponent(name);
                oShareIntent.setType("text/plain");
                oShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "www.foodbuck.com");
                startActivity(oShareIntent);
            }
        });

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sedIntent = new Intent();
                sedIntent.setAction(Intent.ACTION_SEND);
                sedIntent.putExtra(Intent.EXTRA_TEXT, "www.foodbuck.com");
                sedIntent.setType("text/plain");
                startActivity(Intent.createChooser(sedIntent, getResources().getText(R.string.send_to)));
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HashMap<String, String> loc = session.getLocaDetails();
        HashMap<String, String> act = all.getActivityName();
        activ = act.get(AllSharedPref.KEY_ACTIVITYNAME);
        if (activ.equals("Address")||activ.equals("Login_Form")||activ.equals("Account")||activ.equals("Address_Book")) {

            if (location != null && city != null )
            {
                navigationView.getMenu().findItem(R.id.restaraunts).setVisible(true);
                all.ActName("Address");
                String sessioncityname = loc.get(RestrauntLocationManagerPref.KEY_CITY);
                String sessionlocation = loc.get(RestrauntLocationManagerPref.KEY_LOCATION);
                Intent intent = new Intent(Invite_Friends.this, Base.class);
                intent.putExtra("city", sessioncityname);
                intent.putExtra("location", sessionlocation);
                startActivity(intent);
            }
            else {
                navigationView.getMenu().findItem(R.id.restaraunts).setVisible(false);
                Intent intent = new Intent(Invite_Friends.this,Address.class);
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






}
