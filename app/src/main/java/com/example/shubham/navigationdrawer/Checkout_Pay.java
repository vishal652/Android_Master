package com.example.shubham.navigationdrawer;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class Checkout_Pay extends AppCompatActivity implements View.OnClickListener {

    boolean connected=false;
    TextView check_pay;
    AllSharedPref all;
    String tot,check;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button pay;
    Checkout_Shared_Pref pref;
    String restname;
    public static final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_pay);

        all = new AllSharedPref(getApplicationContext());
        pref = new Checkout_Shared_Pref(getApplicationContext());
        checkInternetConenction();

        check_pay = (TextView)findViewById(R.id.checkout_total_pay);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroupPay);
        pay = (Button)findViewById(R.id.btn_order_summ);
        radioButton = (RadioButton)findViewById(R.id.radiobtnPay);

        HashMap<String,String> pi = pref.getDeliverDetails();
        check = pi.get(Checkout_Shared_Pref.KEY_PICKCHECK);


        HashMap<String, String> rest = all.getOpenDeliveryMenuDetails();
        restname = rest.get(AllSharedPref.KEY_RESTNAME);




        if(connected){
            check_pay.setVisibility(View.VISIBLE);

        }
        if(check_pay.getVisibility()==View.VISIBLE){
            HashMap<String, String> act = all.getActivityName();
            tot = act.get(AllSharedPref.KEY_GTOTAL);
            check_pay.setText(tot);
        }

        pay.setOnClickListener(this);
    }

    private boolean checkInternetConenction() {

        ConnectivityManager connec =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);


        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||

                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            // Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            connected = true;

            return true;
        }else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
            //  Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            connected = false;
            return false;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        checkInternetConenction();
        if(connected){
            if(radioButton.isChecked()==false){
                Toast.makeText(Checkout_Pay.this, "Please choose your payment type!", Toast.LENGTH_SHORT).show();
            }
            else{
                switch (radioButton.getId()){
                    case R.id.radiobtnPay:
                        String paymethod = radioButton.getText().toString();
                        pref.PayMethod(paymethod);
                        Intent intent = new Intent(Checkout_Pay.this,Checkout_Details.class);
                        startActivity(intent);
                        break;
                }

            }

        }
    }

    @Override
    public void onBackPressed() {
        try{
            if(check.equals("pickup")){
                Intent i = new Intent(Checkout_Pay.this,Checkout.class);
                startActivity(i);
            }
            else if(!check.equals("pickup")){
                Intent intent = new Intent(Checkout_Pay.this,Checkout_Addn_Phone.class);
                startActivity(intent);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        scheduleNotification(getNotification("You are so close to " + restname + " to complete your order"), 5000);
        super.onStop();
    }


    private void scheduleNotification(Notification notification, int delay) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification(String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.plus);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.plus));
        Intent intent = new Intent(Checkout_Pay.this,Basket.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        builder.setContentTitle("TiredBuzz");
        builder.setContentText(content);
      //  builder.setSubText("Tap to view documentation about notifications.");

        return builder.build();
    }
}
