package com.example.shubham.navigationdrawer;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class AddToBasket extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    TextView title,item_name,item_price,item_desc,total_price,item_quantity;
    String restname,itemtype,itemname,itemprice,itemdesc;
    ImageView minus,plus;
    static int count=0;
    static int basic_price;
    String c,co,quantity,basic;
    Button basket;
    CartSharedPref session;
    AllSharedPref all;
    MyCartDatabse myDatabase;
    SQLiteDatabase sql;
    ContentValues cv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_to_basket);
        session = new CartSharedPref(getApplicationContext());
        all = new AllSharedPref(getApplicationContext());

        Intent i= getIntent();
        restname = i.getStringExtra("restname");
        itemtype = i.getStringExtra("itemtype");
        itemname = i.getStringExtra("itemname");
        itemprice = i.getStringExtra("itemprice");
        itemdesc = i.getStringExtra("itemdesc");

        toolbar = (Toolbar) findViewById(R.id.toolbar_add_to_basket);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.drawable.back);


        Toast.makeText(AddToBasket.this, restname, Toast.LENGTH_SHORT).show();



        title = (TextView)findViewById(R.id.textviewtoolbarbasket);
        title.setText(itemtype);

        item_name = (TextView)findViewById(R.id.item_name);
        item_desc = (TextView)findViewById(R.id.item_description);
        item_price = (TextView)findViewById(R.id.item_price);
        item_name.setText(itemname);
        item_desc.setText(itemdesc);
        item_price.setText(itemprice);

        total_price = (TextView)findViewById(R.id.total_price);
        total_price.setText(itemprice);
        basic = total_price.getText().toString();
        basic_price = Integer.parseInt(basic);

        co = itemprice;

        item_quantity = (TextView)findViewById(R.id.item_quantity);


        minus = (ImageView)findViewById(R.id.minus);
        plus = (ImageView)findViewById(R.id.plus);

        minus.setOnClickListener(this);
        plus.setOnClickListener(this);

        basket = (Button)findViewById(R.id.btn_basket);
        basket.setOnClickListener(this);

        myDatabase = new MyCartDatabse(this,"mydb",null,1);
        sql = myDatabase.getWritableDatabase();
        cv = new ContentValues();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.minus:
                c = item_quantity.getText().toString();
                count = Integer.parseInt(c);
                if(count>1){
                    count = count-1;
                    co = Integer.toString(count);
                    item_quantity.setText(co);
                    count = basic_price*count;
                    co = Integer.toString(count);
                    total_price.setText(co);

                }
                break;
            case R.id.plus:
                c =  item_quantity.getText().toString();
                count = Integer.parseInt(c);
                if(count>=1 && count<20){
                    count = count+1;
                     co = Integer.toString(count);
                    item_quantity.setText(co);
                    count = count * basic_price;
                    co = Integer.toString(count);
                    total_price.setText(co);
                }
                break;

            case R.id.btn_basket:
                quantity= item_quantity.getText().toString();
                all.Quantity(quantity);
                all.Rest(restname);

                String count = "SELECT count(*) FROM cart";
                Cursor mcursor = sql.rawQuery(count, null);
                if(mcursor!=null) {
                    mcursor.moveToFirst();
                    if (mcursor.getInt(0) == 0) {
                      //  Toast.makeText(AddToBasket.this, "Its null", Toast.LENGTH_SHORT).show();
                        cv.put("itemname", itemname);
                        cv.put("itemdesc", itemdesc);
                        cv.put("baseprice", itemprice);
                        cv.put("totalprice", co);
                        cv.put("quantity", quantity);

                        Long result = sql.insert("cart", "null", cv);
                      //  Toast.makeText(AddToBasket.this, "" + result, Toast.LENGTH_SHORT).show();
                    }
                    else if(mcursor.getInt(0)>0){

                      //  Toast.makeText(AddToBasket.this, "It is not null", Toast.LENGTH_SHORT).show();
                        String query = "SELECT count(*) FROM cart WHERE itemname=?";
                        mcursor = sql.rawQuery(query, new String[]{itemname});
                        if (mcursor.getCount()> 0) {
                            String q = "SELECT quantity,baseprice FROM cart WHERE itemname=?";
                            mcursor = sql.rawQuery(q, new String[]{itemname});
                            if (mcursor.getCount() > 0) {
                                while (mcursor.moveToNext()) {
                                    String qua = mcursor.getString(mcursor.getColumnIndex("quantity"));
                                    String price = mcursor.getString(mcursor.getColumnIndex("baseprice"));
                                   // Toast.makeText(AddToBasket.this, price , Toast.LENGTH_SHORT).show();
                                    int qu = Integer.parseInt(qua);
                                      int pr = Integer.parseInt(price);
                                    int qu2 = Integer.parseInt(quantity);
                                    int qu3 = qu + qu2;
                                       int pr2 = qu3 * pr;
                                    String qu4 = String.valueOf(qu3);
                                      String pr3 = String.valueOf(pr2);

                                     cv.put("totalprice", pr3);
                                    cv.put("quantity", qu4);
                                    sql.update("cart", cv, "itemname=?", new String[]{itemname});
                                 //   Toast.makeText(AddToBasket.this, "Updated", Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(AddToBasket.this, qua, Toast.LENGTH_SHORT).show();
                                }
                            }
                            //  Toast.makeText(AddToBasket.this, "Already Exist", Toast.LENGTH_SHORT).show();


                        }

                        String ins_else = "SELECT itemname FROM cart WHERE itemname=?";
                        mcursor = sql.rawQuery(ins_else, new String[]{itemname});
                        if(mcursor.getCount()<=0){
                            cv.put("itemname", itemname);
                            cv.put("itemdesc", itemdesc);
                            cv.put("baseprice", itemprice);
                            cv.put("totalprice", co);
                            cv.put("quantity", quantity);
                            Long result = sql.insert("cart", "null", cv);
                           // Toast.makeText(AddToBasket.this, "Database has value other than same", Toast.LENGTH_SHORT).show();
                            // Toast.makeText(AddToBasket.this, ""+result, Toast.LENGTH_SHORT).show();

                        }

                    }
                }


                Intent  i = new Intent(AddToBasket.this,ProductItem.class);
                i.putExtra("quantity",quantity);
                i.putExtra("itemtype",itemtype);
                i.putExtra("restname",restname);
                startActivity(i);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddToBasket.this, ProductItem.class);
        startActivity(intent);
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
