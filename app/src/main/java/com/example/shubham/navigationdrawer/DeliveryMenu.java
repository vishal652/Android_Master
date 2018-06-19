package com.example.shubham.navigationdrawer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class DeliveryMenu extends AppCompatActivity {

    private ListView listView;
    MyCartDatabse myDatabase;
    SQLiteDatabase sql;
    public static final String DATA_URL = "http://tiredbuzz.16mb.com/Churli/Churli.php?restname=";
    public static final String DATA_UR = "http://tiredbuzz.16mb.com//restraunts/getrestimage.php?restname=";
    String restname, cityloc, type, url, ur, data, im;
    TextView txrest, txloc, txtype, title;
    ImageView imrest;
    RestrauntLocationManagerPref session;
    Toolbar toolbar;
    boolean menus;
    CartSharedPref cart;
    AllSharedPref all;
    String resta_name, oldrest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_menu);

        all = new AllSharedPref(getApplicationContext());
        HashMap<String, String> rest = all.getOpenDeliveryMenuDetails();
        resta_name = rest.get(AllSharedPref.KEY_RESTNAME);

        HashMap<String, String> cartrest = all.getActivityName();
        oldrest = cartrest.get(AllSharedPref.KEY_NAMEREST);


        myDatabase = new MyCartDatabse(this, "mydb", null, 1);
        sql = myDatabase.getWritableDatabase();

        listView = (ListView) findViewById(R.id.listViewMenu);
        txrest = (TextView) findViewById(R.id.idrestname);
        txloc = (TextView) findViewById(R.id.restcitylocation);
        txtype = (TextView) findViewById(R.id.type);
        imrest = (ImageView) findViewById(R.id.restimage);


        toolbar = (Toolbar) findViewById(R.id.toolbar_delivery);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.drawable.back);


        Intent i = getIntent();
        restname = i.getStringExtra("restname");
        cityloc = i.getStringExtra("loc");
        type = i.getStringExtra("type");


        HashMap<String, String> loc = all.getOpenDeliveryMenuDetails();
        restname = loc.get(AllSharedPref.KEY_RESTNAME);
        cityloc = loc.get(AllSharedPref.KEY_CITYLOC);
        type = loc.get(AllSharedPref.KEY_TYPE);


        title = (TextView) findViewById(R.id.restnme);
        title.setText(restname + cityloc);

        txrest.setText(restname);
        txloc.setText(cityloc);
        txtype.setText(type);


        cart = new CartSharedPref(getApplicationContext());
        if (oldrest != null && resta_name != null) {
            if (!oldrest.equals(resta_name)) {
                SharedPreferences settings = getApplicationContext().getSharedPreferences("Reg", Context.MODE_PRIVATE);
                settings.edit().remove("address").commit();
                SharedPreferences set = getApplicationContext().getSharedPreferences("Checkout", Context.MODE_PRIVATE);
                set.edit().remove("check_pick").commit();

                sql = myDatabase.getReadableDatabase();
                sql.delete("cart", "1", null);
                sql.close();
                all.Quan(0);
            }
        }

        getData();

        session = new RestrauntLocationManagerPref(getApplicationContext());

        new getRestImege().execute();
    }

    @Override
    public void onBackPressed() {

        sql = myDatabase.getReadableDatabase();
        String q = "SELECT * FROM cart";
        Cursor cursor = sql.rawQuery(q, null);
        if (cursor.getCount() <= 0) {
            Intent intent = new Intent(DeliveryMenu.this, Base.class);
            startActivity(intent);
        } else {

            AlertDialog.Builder alert = new AlertDialog.Builder(DeliveryMenu.this);
            alert.setMessage("Are you sure you'd like to change restarunts? Your current order will be lost.");

            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    SharedPreferences settings = getApplicationContext().getSharedPreferences("Reg", Context.MODE_PRIVATE);
                    settings.edit().remove("address").commit();
                    SharedPreferences set = getApplicationContext().getSharedPreferences("Checkout", Context.MODE_PRIVATE);
                    set.edit().remove("check_pick").commit();

                    sql = myDatabase.getReadableDatabase();
                    sql.delete("cart", "1", null);
                    sql.close();
                    all.Quan(0);
                    Intent intent = new Intent(DeliveryMenu.this, Base.class);
                    startActivity(intent);
                }
            });

            alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog alet = alert.create();
            alet.show();
            Button nbutton = alet.getButton(DialogInterface.BUTTON_NEGATIVE);
            nbutton.setTextColor(getResources().getColor(R.color.dialog));
            Button pbutton = alet.getButton(DialogInterface.BUTTON_POSITIVE);
            pbutton.setTextColor(getResources().getColor(R.color.dialog));

        }
        cursor.close();


    }

    private void getData() {


        try {
            url = DATA_URL + URLEncoder.encode(restname, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(DeliveryMenu.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {

                        } else if (error instanceof ServerError) {

                        } else if (error instanceof NetworkError) {
                            Toast.makeText(DeliveryMenu.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {

                        }
                    }


                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showJSON(String json) {


        ParseJSONDeliveryMenu pd = new ParseJSONDeliveryMenu(json);
        pd.parseJSONDelivery();

        int i = ParseJSONDeliveryMenu.getVar();
        if (i != 0) {


            title.setText("Delivery Menu");
            menus = true;
            DeliveryMenuList cl = new DeliveryMenuList(this, ParseJSONDeliveryMenu.item_type);
            listView.setAdapter(cl);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    all.ActName("Delivery_Menu");
                    TextView textView = (TextView) view.findViewById(R.id.deliverymenu);
                    String itemtype = listView.getItemAtPosition(position).toString();

                    cart.ItemNdRest(restname, itemtype);

                    Intent intent = new Intent(DeliveryMenu.this, ProductItem.class);
                    intent.putExtra("restname", restname);
                    intent.putExtra("itemtype", itemtype);
                    startActivity(intent);


                }
            });


        } else {
            Toast.makeText(DeliveryMenu.this, "Sorry No Data Found", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
            case R.id.search:
                break;
            case R.id.settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    class getRestImege extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {

            try {
                ur = DATA_UR + URLEncoder.encode(restname, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            try {
                URL url = new URL(ur);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = (InputStream) httpURLConnection.getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
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
                Toast.makeText(DeliveryMenu.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
            else if(data!=null){
                showJSON1(data);
            }

        }


    }

    private void showJSON1(String response){

        im = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");
            JSONObject collegeData = result.getJSONObject(0);
            im = collegeData.getString("image");



        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        new DownloadImageTask((ImageView) findViewById(R.id.restimage))
                .execute(im);


    }


}
