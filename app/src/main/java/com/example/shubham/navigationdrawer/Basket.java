package com.example.shubham.navigationdrawer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Shubham on 02-09-2016.
 */
public class Basket extends AppCompatActivity implements View.OnClickListener {

    TextView subtotal_bef_dis,dicount,subtotal,service_fee,service_tax,VAT,delivery_fee,grand_total;
    MyCartDatabse myDatabase;
    Button choose_del_type;
    SQLiteDatabase sql;
    ContentValues cv;
    String grandt;
    Toolbar toolbar;
    ArrayList<AddToBasket> cart;
    ListView list;
    ArrayList<String> data;
    public static String[] quan =null;
    public static  String[] itemname=null;
    public static String[] baseprice=null;
    TextView edit_order,add_items;
    public boolean imrow;
    ViewCartList vc;
    String amt;
    int amount,dis,stotal,del_fee,srv_fee;
    double vat,tax,gtot;
    AllSharedPref all;
    Checkout_Shared_Pref pref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basket);


        toolbar = (Toolbar) findViewById(R.id.tool_basket);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.drawable.back);

        all = new AllSharedPref(getApplicationContext());
        pref = new Checkout_Shared_Pref(getApplicationContext());


        myDatabase = new MyCartDatabse(this,"mydb",null,1);
        sql = myDatabase.getWritableDatabase();
        cv = new ContentValues();
        list = (ListView)findViewById(R.id.listcart);
        getRecords();
        list.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });





//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//                this,
//                android.R.layout.simple_list_item_1,
//                data );

//        list.setAdapter(arrayAdapter);

        edit_order = (TextView)findViewById(R.id.edit_order);
        edit_order.setOnClickListener(this);
        add_items = (TextView)findViewById(R.id.add_items);
        add_items.setOnClickListener(this);
        subtotal_bef_dis = (TextView)findViewById(R.id.subtot_bef_dis);
        getTotal();
        dicount = (TextView)findViewById(R.id.discount);
        discount();
        subtotal = (TextView)findViewById(R.id.subtotal);
        subtot();
        service_fee = (TextView)findViewById(R.id.service_fee);
        srvicefee();
        service_tax = (TextView)findViewById(R.id.service_tax);
        srvicetax();
        VAT = (TextView)findViewById(R.id.VAT);
        cal_vat();
        delivery_fee = (TextView)findViewById(R.id.delivery_fee);
        cal_del_fee();
        grand_total = (TextView)findViewById(R.id.total);
        cal_grand_tot();

        choose_del_type = (Button)findViewById(R.id.btn_choose_delivery_type);
        choose_del_type.setOnClickListener(this);


    }

    public void getRecords(){

         sql = myDatabase.getReadableDatabase();
         Cursor cursor = sql.rawQuery("select * from cart ",null);
         quan = new String[cursor.getCount()];
         itemname = new String[cursor.getCount()];
         baseprice = new String[cursor.getCount()];
       // String fieldToAdd=null;
        int i = 0;
        if(cursor.getCount()>0){
            while(cursor.moveToNext()){
                String uquan = cursor.getString(5);
                String uname = cursor.getString(1);
                String uprice = cursor.getString(4);

                quan[i] = uquan;
                itemname[i] = uname;
                baseprice[i] = uprice;
                i++;
//            fieldToAdd=cursor.getString(1)+ " " + cursor.getString(4) + " " + cursor.getString(5);
//            data.add(fieldToAdd);
             //   Toast.makeText(this, quan + itemname[i] , Toast.LENGTH_SHORT).show();
            }
            vc = new ViewCartList(this,quan,itemname,baseprice);
            list.setAdapter(vc);
        }
        else{

        }

        cursor.close();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_items:
                Intent i3 = new Intent(Basket.this,ProductItem.class);
                startActivity(i3);
                break;
            case R.id.edit_order:
                edit_order.setText("Apply Changes");
                break;
            case R.id.btn_choose_delivery_type:

                double s = amount + tax + vat;
                pref.SubTot(String.valueOf(s));

                grandt = grand_total.getText().toString();
                all.ActName("Basket");
                Intent intent = new Intent(Basket.this,Checkout.class);
                all.Gtotal(grandt);
              //  intent.putExtra("total",grandt);
                startActivity(intent);
        }


    }

    public void getTotal(){
        Cursor cursor = sql.rawQuery("SELECT SUM(totalprice) FROM cart",null);
        if(cursor.moveToFirst()){
            amount = cursor.getInt(0);
            subtotal_bef_dis.setText("Rs. "+String.valueOf(amount));
        }
        else{
            amount = -1;
        }

    }

    public void discount(){
        dis= 0;
        dicount.setText("-Rs. "+String.valueOf(dis));
    }

    public void subtot(){
        stotal = amount-dis;
        subtotal.setText("Rs. "+String.valueOf(stotal));
    }

    public void srvicefee(){
        srv_fee = 0;
        service_fee.setText("Rs. "+String.valueOf(srv_fee));
    }

    public void srvicetax(){
         tax = (6*stotal)/100;
         service_tax.setText("Rs. "+String.valueOf(tax));
    }

    public void cal_vat(){
        vat = (14.5*stotal)/100;
        VAT.setText("Rs. "+String.valueOf(vat));
    }

    public void cal_del_fee(){
        del_fee =0;
        delivery_fee.setText("Rs. "+String.valueOf(del_fee));
    }

    public void cal_grand_tot(){
        gtot = stotal+srv_fee+tax+vat+del_fee;
        grand_total.setText("Rs. "+String.valueOf(gtot));
    }

    @Override
    public void onBackPressed() {
        HashMap<String, String> act = all.getActivityName();
        String activ = act.get(AllSharedPref.KEY_ACTIVITYNAME);
        Intent i = new Intent(Basket.this,ProductItem.class);
        startActivity(i);
//        if(activ.equals("Product_Item")||activ.equals("Checkout")||activ.equals("Show_delete")||activ.equals("Address_Book")||activ.equals("CheckoutAdd")){
//
//        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
