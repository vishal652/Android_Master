package com.example.shubham.navigationdrawer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ImageView ivl, ivwifi;
    TextView txl, txno;
    private Context _context;
    boolean isInternetPresent = false;
    MainActivity cd;
    Button btntry;
    ConnectivityManager connectivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ivl = (ImageView) findViewById(R.id.ivlogo);
        txl = (TextView) findViewById(R.id.tvlogo);
        cd = new MainActivity(getApplicationContext());

        // Showing up Logo

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate);
        ivl.startAnimation(anim);

        Animation animtext = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.text_logo);
        txl.startAnimation(animtext);


        // Internet Conection

        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            Thread timer = new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(4000);


                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        Intent i = new Intent(MainActivity.this, Address.class);
                        startActivity(i);
                    }
                }
            };
            timer.start();

            //	Toast.makeText(getApplicationContext(), "Connected to Internet", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(getApplicationContext(), "Not Connected to Internet", Toast.LENGTH_LONG).show();
            ivwifi = (ImageView) findViewById(R.id.ivwifi);
            ivwifi.setVisibility(View.VISIBLE);
            txno = (TextView) findViewById(R.id.tvnoconn);
            txno.setVisibility(View.VISIBLE);
            btntry = (Button) findViewById(R.id.btntry);
            btntry.setVisibility(View.VISIBLE);

            btntry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    NetworkInfo mobile = connManager .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                    if (wifi.isConnected()|| mobile.isConnected() ){
                        //if wifi connected
                        Intent i2 = new Intent(MainActivity.this, Address.class);
                        startActivity(i2);

                    }
                    else{
                        Toast toast= Toast.makeText(getApplicationContext(),
                                "Not connected", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 800);
                        toast.show();
                    }
                }
            });

        }

    }

    public MainActivity() {

    }


    public MainActivity(Context context) {
        this._context = context;
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);


       if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }
}
