package com.example.shubham.navigationdrawer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class ProductItem extends AppCompatActivity {

    public static final String DATA_URL = "http://tiredbuzz.16mb.com/Churli/itemname.php?restname=";
    Toolbar toolbar;
    MyCartDatabse myDatabase;
    SQLiteDatabase sql;
    String restname,itemtype,ur,data,q;
    TextView title,quan;
    FrameLayout frame;
    ListView listView;
    AllSharedPref all;
    CartSharedPref cart;
    FloatingActionButton fabcart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_item);

        myDatabase = new MyCartDatabse(this,"mydb",null,1);
        sql = myDatabase.getReadableDatabase();

        all = new AllSharedPref(getApplicationContext());


        listView = (ListView)findViewById(R.id.listViewproductitem);
        frame = (FrameLayout)findViewById(R.id.frame);

        Intent i= getIntent();
        restname = i.getStringExtra("restname");
        itemtype = i.getStringExtra("itemtype");


        cart= new CartSharedPref(getApplicationContext());
        HashMap<String, String> loc = cart.getItemndRest();
        restname= loc.get(CartSharedPref.KEY_RESTNAME);
        itemtype= loc.get(CartSharedPref.KEY_ITEMTYPE);

        toolbar = (Toolbar) findViewById(R.id.toolbar_product_item);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.drawable.back);

        title = (TextView)findViewById(R.id.productname);
        title.setText(itemtype);
        quan = (TextView)findViewById(R.id.quan);
        frame = (FrameLayout)findViewById(R.id.frame);

        new Product().execute();
        q = i.getStringExtra("quantity");
        SharedPreferences myPrefs = this.getSharedPreferences("All", MODE_WORLD_READABLE);
        int first = myPrefs.getInt("quan",0);

        try{
            if(q!=null){
                try{
                    int q1 = Integer.parseInt(q);
                    if(first == 0 ){
                        all.Quan(q1);

                        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
                        frame.startAnimation(anim);
                        frame.setVisibility(View.VISIBLE);

                        quan = (TextView)findViewById(R.id.quan);
                        String q2= String.valueOf(q1);
                        quan.setText(q2);
                    }
                    else if(first!=0){

                        int three1 = first + q1;

                        all.Quan(three1);

                        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
                        frame.startAnimation(anim);
                        frame.setVisibility(View.VISIBLE);

                        quan = (TextView)findViewById(R.id.quan);
                        String three = String.valueOf(three1);
                        quan.setText(three);
                    }

                }catch(NumberFormatException e){
                    e.printStackTrace();
                }

            }

            else if(q==null && first!=0)
            {

                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
                frame.startAnimation(anim);
                frame.setVisibility(View.VISIBLE);

                quan = (TextView)findViewById(R.id.quan);
                String first1 = String.valueOf(first);
                quan.setText(first1);
            }

        }catch(NumberFormatException e){
            e.printStackTrace();
        }

      fabcart = (FloatingActionButton)findViewById(R.id.fabcart);
      fabcart.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              all.ActName("Product_Item");
              Intent i = new Intent(ProductItem.this, Basket.class);
              startActivity(i);
          }
      });



    }

    class Product extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {

            try {
                ur = DATA_URL+ URLEncoder.encode(restname, "UTF-8").replace("+", "%20")+"&itemtype="+URLEncoder.encode(itemtype,"UTF-8").replace("+", "%20");
            } catch (UnsupportedEncodingException e) {

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
            if(data==null){
                Toast.makeText(ProductItem.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
            else if(data!=null){
                showJSON(data);
            }
        }
    }

    private void showJSON(String json){
        ParseJSONProduct pd = new ParseJSONProduct(json);
        pd.parseJSONProduct();

        int i =ParseJSONProduct.getVar();
            if(i!=0) {
                ProductItemList cl = new ProductItemList(this, ParseJSONProduct.itemname, ParseJSONProduct.description, ParseJSONProduct.price,ProductItemList.quanx);
                listView.setAdapter(cl);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        all.ActName("Product_Item");

                        String itemname = ((TextView)view.findViewById(R.id.item_name)).getText().toString();
                        String itemdesc = ((TextView)view.findViewById(R.id.item_description)).getText().toString();
                        String itemprice = ((TextView)view.findViewById(R.id.item_price)).getText().toString();

                        int before = parent.getPositionForView(view);
                        String s= Integer.toString(before);
                        all.Pos(s);
                        all.ItemName(itemname);
                        //Toast.makeText(getApplicationContext(),s +": "+itemname,Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ProductItem.this,AddToBasket.class);
                        intent.putExtra("itemtype",itemtype);
                        intent.putExtra("itemname",itemname);
                        intent.putExtra("restname",restname);
                        intent.putExtra("itemdesc",itemdesc);
                        intent.putExtra("itemprice",itemprice);
                        startActivity(intent);
                    }
                });
            }
        else{
                Toast.makeText(ProductItem.this, "Sorry nothing is found.", Toast.LENGTH_SHORT).show();
            }

    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ProductItem.this,DeliveryMenu.class);
        startActivity(i);

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
